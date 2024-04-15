import grpc from 'k6/net/grpc';
import http from 'k6/http';
import {check, group, sleep} from 'k6';

const productGrpcClient = new grpc.Client();
const orderGrpcClient = new grpc.Client();

export const options = {
  scenarios: {
    rampUpAndDown: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        {duration: '3m', target: 25},
        {duration: '5m', target: 10},
        {duration: '4m', target: 15},
        {duration: '4m', target: 0},
      ],
      startTime: '0m',
      exec: 'scenario1',
    },
    constantLoad: {
      executor: 'constant-vus',
      vus: 10,
      duration: '12m',
      startTime: '4m',
      exec: 'scenario1',
    },
  }
  // stages: [
  //   {duration: '2m', target: 10},  // ramp up to 10 users
  //   {duration: '3m', target: 10},  // stay at 10 users
  //   {duration: '2m', target: 0},   // ramp down to 0 users
  // ],
};

export const scenario1 = () => {
  productGrpcClient.connect('localhost:5102', {plaintext: true, reflect: true});
  orderGrpcClient.connect('localhost:5101', {plaintext: true, reflect: true});

  group("Product Service Calls", function () {
    randomizedRequest(
        () => grpcCall(productGrpcClient, 'product.ProductService/GetProduct',
            {id: '1'}),
        10
    );
  });

  group("Product Availability Calls", function () {
    randomizedRequest(
        () => grpcCall(orderGrpcClient,
            'order.productavailability.ProductAvailabilityService/CheckProductAvailability',
            {}),
        5
    );
  });

  group("HTTP Inventory Calls", function () {
    const payload = JSON.stringify({sku: '1', newQuantity: 10});
    const params = {headers: {'Content-Type': 'application/json'}};
    randomizedRequest(
        () => httpCall('http://localhost:8081/inventory', payload, params),
        10
    );
  });

  productGrpcClient.close();
  orderGrpcClient.close();

  // randomize sleep time between 0.4 and 1.2
  sleep(Math.random() * 0.8 + 0.4);
};

function randomizedRequest(fn, maxRequests) {
  const numRequests = Math.floor(Math.random() * maxRequests) + 1; // Between 1 and maxRequests
  console.log('Number of requests:', numRequests);
  for (let i = 0; i < numRequests; i++) {
    fn();
  }
}

function grpcCall(client, method, payload) {
  const response = client.invoke(method, payload);
  check(response, {
    'status is OK': (r) => r.status === grpc.StatusOK,
  });
  console.log('Response grpc:', JSON.stringify(response.message));
}

function httpCall(url, payload, params) {
  const response = http.post(url, payload, params);
  check(response, {
    'status is 200': (r) => r.status === 200,
  });
  console.log('Response http:', response.body);
}

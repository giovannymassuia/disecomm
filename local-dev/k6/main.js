import grpc from 'k6/net/grpc';
import http from 'k6/http';
import {check, group, sleep} from 'k6';
import { Trend, Counter } from 'k6/metrics';

const productGrpcClient = new grpc.Client();
const orderGrpcClient = new grpc.Client();

const responseTime = new Trend('response_time', true);
const requestFailures = new Counter('request_failures');

// Instructions for running the test
// kubernetes resources: k6 run --env ENV=k8s main.js
// docker-compose: k6 run --env ENV=docker main.js

export const options = {
  thresholds: {
    'group_duration{group:::grpc-product}': [],
    'group_duration{group:::grpc-order}': [],
    'group_duration{group:::http-inventory}': [],
    'group_duration{group:::graphql-api-gateway}': [],
  },
  scenarios: {
    // rampUpAndDown: {
    //   executor: 'ramping-vus',
    //   startVUs: 0,
    //   stages: [
    //     {duration: '3m', target: 25},
    //     {duration: '5m', target: 10},
    //     {duration: '4m', target: 15},
    //     {duration: '4m', target: 0},
    //   ],
    //   startTime: '0m',
    //   exec: 'scenario1',
    // },
    //
    // constantLoad: {
    //   executor: 'constant-vus',
    //   vus: 10,
    //   duration: '12m',
    //   startTime: '4m',
    //   exec: 'scenario1',
    // },

    smallLoad: {
      executor: 'constant-vus',
      vus: 5,
      duration: '30s',
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
 const {ENV} = __ENV;

  let inventory_http_url = 'localhost:8081';
  let graphql_http_url = 'localhost:8083';
  let product_grpc_url = 'localhost:5102';
  let order_grpc_url = 'localhost:5101';

  if(ENV === 'docker') {
    graphql_http_url = 'host.docker.internal:8083';
    inventory_http_url = 'host.docker.internal:8081';
    product_grpc_url = 'host.docker.internal:5102';
    order_grpc_url = 'host.docker.internal:5101';
  } else if (ENV === 'k8s') {
    graphql_http_url = 'api-gateway.127.0.0.1.nip.io:8888';
    inventory_http_url = 'inventory-management.127.0.0.1.nip.io:8888';
    product_grpc_url = 'product-catalog.grpc.127.0.0.1.nip.io:8888';
    order_grpc_url = 'order-management.grpc.127.0.0.1.nip.io:8888';
  }

  productGrpcClient.connect(`${product_grpc_url}`, {plaintext: true, reflect: true});
  orderGrpcClient.connect(`${order_grpc_url}`, {plaintext: true, reflect: true});

  group("grpc-product", function () {
    randomizedRequest(
        () => grpcCall(productGrpcClient, 'product.ProductService/GetProduct',
            {id: '1'}),
        10
    );
  });

  group("grpc-order", function () {
    randomizedRequest(
        () => grpcCall(orderGrpcClient,
            'order.productavailability.ProductAvailabilityService/CheckProductAvailability',
            {}),
        5
    );
  });

  group("http-inventory", function () {
    const payload = JSON.stringify({sku: '1', newQuantity: 10});
    const params = {headers: {'Content-Type': 'application/json'}};
    randomizedRequest(
        () => httpCall('post', `http://${inventory_http_url}/inventory/on-hand`,
            payload, params, 201),
        10
    );
  });

  group("graphql-api-gateway", function () {
    const query = `
      query {
        product(id: "1") {
          id
          name
          price
          inStock
          inventory(types: [ON_HAND, AVAILABLE]) {
            type
            quantity
          }
        }
      }`;
    const payload = JSON.stringify({query});
    const params = {headers: {'Content-Type': 'application/json'}};
    randomizedRequest(
        () => httpCall('post', `http://${graphql_http_url}/graphql`,
            payload, params),
        5
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
  console.log('Response grpc:', response.status,
      JSON.stringify(response.message));
}

function httpCall(method, url, payload, params, expectedStatus = 200) {
  const response = http[method](url, payload, params);

  // Record response time
  responseTime.add(response.timings.duration);

  const checkResult = check(response, {
   [`status is ${expectedStatus}`]: (r) => r.status === expectedStatus,
    'acceptable response time': (r) => r.timings.duration < 300,
  });

  // Conditional logging for failures
  if (!checkResult) {
    requestFailures.add(1);
    console.error('Failed response:', {
      status: response.status,
      body: response.body,
      headers: response.headers,
      duration: response.timings.duration
    });
  } else {
    console.log('Successful response:', {
      status: response.status,
      body: response.body,
      headers: response.headers,
      duration: response.timings.duration
    });
  }
}

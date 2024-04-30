package io.giovannymassuia.disecomm.apigateway.controller.rest;

import io.giovannymassuia.disecomm.apigateway.graphql.model.ProductGraphqlModel;
import io.giovannymassuia.disecomm.protobuf.product.GetProductRequest;
import io.giovannymassuia.disecomm.protobuf.product.GetProductResponse;
import io.giovannymassuia.disecomm.protobuf.product.ProductServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    @GrpcClient("product-catalog-service")
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;


    @RequestMapping()
    public ResponseEntity<?> test() {
        GetProductResponse response = productServiceBlockingStub.getProduct(
            GetProductRequest.newBuilder()
                .setId("1")
                .build());

        log.info("Response from product service (REST): {}", response);

        return ResponseEntity.ok(ProductGraphqlModel.builder()
                                     .setId(response.getProduct().getId())
                                     .setName(response.getProduct().getName())
                                     .setInStock(response.getProduct().getInStock())
                                     .setPrice(response.getProduct().getPrice())
                                     .build());
    }
}

package io.giovannymassuia.disecomm.productcatalog.grpc;

import io.giovannymassuia.disecomm.protobuf.order.productavailability.CheckProductAvailabilityRequest;
import io.giovannymassuia.disecomm.protobuf.order.productavailability.ProductAvailabilityServiceGrpc;
import io.giovannymassuia.disecomm.protobuf.product.GetProductRequest;
import io.giovannymassuia.disecomm.protobuf.product.GetProductResponse;
import io.giovannymassuia.disecomm.protobuf.product.ListProductsRequest;
import io.giovannymassuia.disecomm.protobuf.product.ListProductsResponse;
import io.giovannymassuia.disecomm.protobuf.product.Product;
import io.giovannymassuia.disecomm.protobuf.product.ProductServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {

    @GrpcClient("product-availability-service")
    private ProductAvailabilityServiceGrpc.ProductAvailabilityServiceBlockingStub productAvailabilityService;

    @Override
    public void getProduct(GetProductRequest request,
        StreamObserver<GetProductResponse> responseObserver) {

        responseObserver.onNext(GetProductResponse.newBuilder()
                                    .setProduct(Product.newBuilder()
                                                    .setId("1")
                                                    .setName("Product 1")
                                                    .setInStock(productAvailabilityService
                                                                    .checkProductAvailability(
                                                                        CheckProductAvailabilityRequest
                                                                            .newBuilder()
                                                                            .setProductId("1")
                                                                            .build()).getInStock()
                                                    ).build())
                                    .build());
        responseObserver.onCompleted();
    }

    @Override
    public void listProducts(ListProductsRequest request,
        StreamObserver<ListProductsResponse> responseObserver) {

        responseObserver.onNext(ListProductsResponse.newBuilder()
                                    .addProducts(Product.newBuilder()
                                                     .setId("1")
                                                     .setName("Product 1")
                                                     .build())
                                    .addProducts(Product.newBuilder()
                                                     .setId("2")
                                                     .setName("Product 2")
                                                     .build())
                                    .build());
        responseObserver.onCompleted();
    }
}

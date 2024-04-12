package io.giovannymassuia.disecomm.ordermanagement.grpc;

import io.giovannymassuia.disecomm.protobuf.order.productavailability.CheckProductAvailabilityRequest;
import io.giovannymassuia.disecomm.protobuf.order.productavailability.CheckProductAvailabilityResponse;
import io.giovannymassuia.disecomm.protobuf.order.productavailability.ProductAvailabilityServiceGrpc;
import io.grpc.stub.StreamObserver;
import java.time.LocalDate;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ProductAvailabilityGrpcService extends
    ProductAvailabilityServiceGrpc.ProductAvailabilityServiceImplBase {

    @Override
    public void checkProductAvailability(CheckProductAvailabilityRequest request,
        StreamObserver<CheckProductAvailabilityResponse> responseObserver) {

        responseObserver.onNext(CheckProductAvailabilityResponse.newBuilder()
                                    .setInStock(true)
                                    .setAvailableDate(
                                        LocalDate.now().toString())
                                    .setQuantityAvailable(5)
                                    .build());
        responseObserver.onCompleted();

    }
}

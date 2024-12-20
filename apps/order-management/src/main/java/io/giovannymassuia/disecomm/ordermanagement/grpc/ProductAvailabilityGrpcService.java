package io.giovannymassuia.disecomm.ordermanagement.grpc;

import io.giovannymassuia.disecomm.ordermanagement.utils.ChaosUtils;
import io.giovannymassuia.disecomm.protobuf.order.productavailability.CheckProductAvailabilityRequest;
import io.giovannymassuia.disecomm.protobuf.order.productavailability.CheckProductAvailabilityResponse;
import io.giovannymassuia.disecomm.protobuf.order.productavailability.ProductAvailabilityServiceGrpc;
import io.grpc.stub.StreamObserver;
import java.time.LocalDate;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class ProductAvailabilityGrpcService extends
    ProductAvailabilityServiceGrpc.ProductAvailabilityServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        ProductAvailabilityGrpcService.class);

    @Override
    public void checkProductAvailability(CheckProductAvailabilityRequest request,
        StreamObserver<CheckProductAvailabilityResponse> responseObserver) {

        ChaosUtils.applyChaos(false);

        LOGGER.info("Checking product availability for product with id: {}",
            request.getProductId());

        responseObserver.onNext(CheckProductAvailabilityResponse.newBuilder()
                                    .setInStock(true)
                                    .setAvailableDate(
                                        LocalDate.now().toString())
                                    .setQuantityAvailable(5)
                                    .build());

        LOGGER.info("Done checking product availability for product with id: {}",
            request.getProductId());

        responseObserver.onCompleted();

    }

}

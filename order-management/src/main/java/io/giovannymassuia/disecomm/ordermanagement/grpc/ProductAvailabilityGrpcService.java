package io.giovannymassuia.disecomm.ordermanagement.grpc;

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

        // add random delay between 100 and 2000 ms
        try {
            Thread.sleep((long) (Math.random() * 1900) + 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // random fibonacci calculation between 1 and 40
        int n = (int) (Math.random() * 50) + 1;
        long result = fibonacci(n);

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

    private long fibonacci(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}

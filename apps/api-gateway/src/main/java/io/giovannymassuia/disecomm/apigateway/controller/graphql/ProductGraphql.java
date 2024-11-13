package io.giovannymassuia.disecomm.apigateway.controller.graphql;

import io.giovannymassuia.disecomm.apigateway.graphql.api.ProductQueryResolver;
import io.giovannymassuia.disecomm.apigateway.graphql.api.ProductResolver;
import io.giovannymassuia.disecomm.apigateway.graphql.model.InventoryTypeGraphqlModel;
import io.giovannymassuia.disecomm.apigateway.graphql.model.ProductGraphqlModel;
import io.giovannymassuia.disecomm.apigateway.graphql.model.ProductInventoryGraphqlModel;
import io.giovannymassuia.disecomm.protobuf.product.GetProductRequest;
import io.giovannymassuia.disecomm.protobuf.product.GetProductResponse;
import io.giovannymassuia.disecomm.protobuf.product.ProductServiceGrpc;
import java.util.List;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class ProductGraphql implements ProductQueryResolver, ProductResolver {

    private final Logger log = LoggerFactory.getLogger(ProductGraphql.class);

    @GrpcClient("product-catalog-service")
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    @Override
    public ProductGraphqlModel product(String id) throws Exception {
        GetProductResponse response = productServiceBlockingStub.getProduct(
            GetProductRequest.newBuilder()
                .setId(id)
                .build());

        return ProductGraphqlModel.builder()
                   .setId(response.getProduct().getId())
                   .setName(response.getProduct().getName())
                   .setInStock(response.getProduct().getInStock())
                   .setPrice(response.getProduct().getPrice())
                   .build();
    }

    @Override
    public List<ProductGraphqlModel> products() throws Exception {
        return List.of();
    }

    @Override
    public List<ProductInventoryGraphqlModel> inventory(ProductGraphqlModel product,
        List<InventoryTypeGraphqlModel> types) throws Exception {
        log.info("Resolver inventory called with product {} and types {}", product, types);

        GetProductResponse response = productServiceBlockingStub.getProduct(
            GetProductRequest.newBuilder()
                .setId(product.getId())
                .build());

        log.info("Response from product service: {}", response);

        return types.stream()
                   .map(type -> ProductInventoryGraphqlModel.builder()
                                    .setType(type)
                                    .setQuantity(123)
                                    .build())
                   .toList();
    }

//    @Override
//    public ProductInventoryGraphqlModel inventory(ProductGraphqlModel product,
//        InventoryTypeGraphqlModel type) throws Exception {
//        log.info("Resolver inventory called with product {} and type {}", product, type);
//        return ProductInventoryGraphqlModel.builder()
//                   .setType(type)
//                   .setQuantity(123)
//                   .build();
//    }
}

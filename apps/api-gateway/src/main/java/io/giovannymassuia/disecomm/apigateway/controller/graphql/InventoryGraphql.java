package io.giovannymassuia.disecomm.apigateway.controller.graphql;

import io.giovannymassuia.disecomm.apigateway.graphql.api.InventoryMutationResolver;
import io.giovannymassuia.disecomm.apigateway.graphql.model.ProductInventoryInputGraphqlModel;
import io.giovannymassuia.disecomm.apigateway.graphql.model.UpdateOnHandQuantityResponseGraphqlModel;
import org.springframework.stereotype.Controller;

@Controller
public class InventoryGraphql implements InventoryMutationResolver {

    @Override
    public UpdateOnHandQuantityResponseGraphqlModel updateOnHandQuantity(
        ProductInventoryInputGraphqlModel input) throws Exception {
        return null;
    }
}

package io.giovannymassuia.disecomm.apigateway.controller.graphql;

import io.giovannymassuia.disecomm.apigateway.graphql.api.SchemaMutationResolver;
import io.giovannymassuia.disecomm.apigateway.graphql.api.SchemaQueryResolver;
import org.springframework.stereotype.Controller;

@Controller
public class HelloGraphql implements SchemaQueryResolver, SchemaMutationResolver {

    @Override
    public String ping() {
        return "pong";
    }

    @Override
    public String hey(String name) {
        return "Hey " + name;
    }

}

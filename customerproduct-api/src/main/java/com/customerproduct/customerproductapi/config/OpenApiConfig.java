package com.customerproduct.customerproductapi.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Customer & Product API",
                description = "API Endpoints to manage Customers and Products.",
                summary = "This API will manage customer with its family member and the products",
                termsOfService = "T&C",
                contact = @Contact(
                        name = "eTiqa",
                        email = "test@etiqa.com"

                ),
                license = @License(
                        name = "eTiqa License # "
                ),
                version = "v1"
        ),
        servers = {
                @Server (
                        description = "Dev",
                        url = "http://localhost:8080/customerproduct-api"
                ),
                @Server (
                        description = "Stage",
                        url = "http://localhost:8080"
                ),
                @Server (
                        description = "Prod",
                        url = "http://localhost:8080"
                )
        }
)
public class OpenApiConfig {
}

package br.ufrn.imd.api;

import br.ufrn.imd.security.SecurityUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TreeMap;

@Configuration
public class OpenApiConfiguration {

    @Value("${app.version}")
    private String version;

    @Value("${app.description}")
    private String description;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("API DevOps")
                        .description(description)
                        .version(version))
                .components(new Components().addSecuritySchemes("jwtAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .scheme("bearer")
                        .name(SecurityUtils.HEADER_STRING)
                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("jwtAuth"));
    }
}

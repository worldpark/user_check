package com.check.user_check.config.swagger;

import com.check.user_check.config.swagger.path.LoginPathItem;
import com.check.user_check.config.swagger.path.RefreshPathItem;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"dev"})
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "accessToken";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                );

        Info info = new Info()
                .title("출결 API")
                .version("1");

        Paths paths = new Paths()
                .addPathItem("/api/user/auth/login", new LoginPathItem().build())
                .addPathItem("/api/user/auth/refresh", new RefreshPathItem().build());

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components)
                .paths(paths)
                .info(info);
    }
}

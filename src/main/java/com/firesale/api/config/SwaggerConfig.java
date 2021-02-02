package com.firesale.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;

@Configuration
public class SwaggerConfig {
    protected static final Contact CONTACT = new Contact("Astrid-HR, Carlo Ligthart, Jim Geersinga, Mike van Leeuwen, 0548643,", "https://github.com/mikevanl/firesale", "");
    protected static final ApiInfo DEFAULT_API = new ApiInfo("swagger", "Swagger Documentation", "1.0", "urn:tos", CONTACT,
            "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList<>());
    protected static final Set<String> consumes = new HashSet<>(Arrays.asList("application/json"));
    protected static final Set<String> produces = new HashSet<>(Arrays.asList("application/json"));
    protected static final String BASIC_AUTH = "basicAuth";
    protected static final List<SecurityScheme> securitySchemes = Arrays.asList(new BasicAuth(BASIC_AUTH));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(securitySchemes)
                .apiInfo(DEFAULT_API)
                .consumes(consumes)
                .produces(produces)
                .securityContexts(Arrays.asList(securityContexts()))
                .securitySchemes(Arrays.asList(basicAuthScheme()));
    }

    private SecurityContext securityContexts() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(basicAuthReference()))
                .build();
    }

    private SecurityScheme basicAuthScheme() {
        return new BasicAuth(BASIC_AUTH);
    }

    private SecurityReference basicAuthReference() {
        return new SecurityReference(BASIC_AUTH, new AuthorizationScope[0]);
    }
}

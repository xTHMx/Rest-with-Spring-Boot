package br.tulio.projetospring.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //Carrega a classe antes, pois tem informação de configuração e dados sensiveis
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST com Spring Boot, Kubernets e Docker")
                        .version("1.0")
                        .description("API REST com Spring Boot, Kubernets e Docker")
                        .termsOfService("http://meusite.com")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://meusite.com")
                        )

                );
    };

}

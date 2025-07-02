package br.tulio.projetospring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.originPatters:default}") //recebe os valores do application.yaml
    private String corsOriginPatterns = "";

    @Override
    public void addCorsMappings(CorsRegistry registry) {  //Implementando o cors de forma global
        var allowedOriginPatterns = corsOriginPatterns.split(","); //gera uma lista com origens permitidas
        registry.addMapping("/**") //Aplica em todas as nossas requisições serão filtradas por CORS (/**) - todos endpoints
                .allowedOrigins(allowedOriginPatterns)
                //.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") //pode filtra metodos
                .allowedMethods("*") //todos metofos serão filtrados
                .allowCredentials(true); //permite envio de credenciais
    }

    //configura o content type
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        //Via EXTENSION http://localhost:8080/api/person/v1/1.xml( ou .json) não funciona mais

        //Via QUERY PARAM http://localhost:8080/api/person/v1/1?MediaType=xml(ou json)
        /*
        configurer.favorParameter(true)
                .parameterName("MediaType")
                .ignoreAcceptHeader(true)
                .useRegisteredExtensionsOnly(true)
                .defaultContentType(MediaType.APPLICATION_JSON)
                    .mediaType("xml", MediaType.APPLICATION_XML)
                    .mediaType("json", MediaType.APPLICATION_JSON);
        */

        //Via HEADER PARAM http://localhost:8080/api/person/v1/1 (altera nada, é pelo header)
        //Deve Adicionar os parametros no header (key) e (value)
        // Accept -> application/json ou /xml
        configurer.favorParameter(false)
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                    .mediaType("xml", MediaType.APPLICATION_XML)
                    .mediaType("json", MediaType.APPLICATION_JSON)
                    .mediaType("yaml", MediaType.APPLICATION_YAML);

    }
}

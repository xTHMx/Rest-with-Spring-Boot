package br.tulio.projetospring.integrationTests.controllers;

import br.tulio.projetospring.configs.TestConfigs;
import br.tulio.projetospring.integrationTests.AbstractIntegrationTest;
import br.tulio.projetospring.integrationTests.dto.AccountCredentialsDTO;
import br.tulio.projetospring.integrationTests.dto.TokenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.path.xml.XmlPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerXmlTest extends AbstractIntegrationTest {

    private static XmlMapper objectMapper;

    private static TokenDTO tokenDto;

    @BeforeAll
    static void setUp() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        tokenDto = new TokenDTO();
    }

    @Test
    @Order(1)
    void signIn() throws JsonProcessingException {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("leandro", "admin123");

        var context = given()                               //inicia uma requisição HTTP
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(credentials)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                        .extract().body().asString()
        ;

        tokenDto = objectMapper.readValue(context, TokenDTO.class);

        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());


    }

    @Test
    @Order(2)
    void refreshToken() throws JsonProcessingException {

        var context = given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("username", tokenDto.getUserName())
                .header("Authorization", "Bearer " + tokenDto.getRefreshToken())
                .when()
                    .put("{username}")
                .then()
                    .statusCode(200)
                        .extract().body().xmlPath()
        ;

        //Não consegui recuperar o objeto em si, mas como vinha a estrutura da response correta, apenas extrai os dados
        String accessToken = context.getString("ResponseEntity.body.accessToken");
        String refreshToken = context.getString("ResponseEntity.body.refreshToken");

        //System.out.println(accessToken);
        //System.out.println(refreshToken);

        tokenDto.setAccessToken(accessToken);
        tokenDto.setRefreshToken(refreshToken);

        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());

    }
}
package br.tulio.projetospring.integrationTests.controllers;

import br.tulio.projetospring.configs.TestConfigs;
import br.tulio.projetospring.integrationTests.AbstractIntegrationTest;
import br.tulio.projetospring.integrationTests.dto.AccountCredentialsDTO;
import br.tulio.projetospring.integrationTests.dto.TokenDTO;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerJsonTest extends AbstractIntegrationTest {

    private static TokenDTO tokenDto;

    @BeforeAll
    static void setUp() {
        tokenDto = new TokenDTO();
    }

    @Test
    @Order(1)
    void signIn() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("leandro", "admin123");

        tokenDto = given()                               //inicia uma requisição HTTP
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credentials)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .extract()
                        .body().as(TokenDTO.class)
        ;

        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());


    }

    @Test
    @Order(2)
    void refreshToken() {

        tokenDto = given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("username", tokenDto.getUserName())
                .header("Authorization", "Bearer " + tokenDto.getRefreshToken())
                .when()
                    .put("{username}")
                .then()
                    .statusCode(200)
                        .extract().body().jsonPath()             //extrai o body, converte em JsonPath (usado pra navegar no json)
                        .getObject("body", TokenDTO.class)  //Navega até o campo "body" e mapeia os valores em um objeto da classe TokenDTO
        ;


        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());

    }
}
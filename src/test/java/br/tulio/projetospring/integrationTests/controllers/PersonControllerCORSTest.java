package br.tulio.projetospring.integrationTests.controllers;

import br.tulio.projetospring.configs.TestConfigs;
import br.tulio.projetospring.integrationTests.AbstractIntegrationTest;
import br.tulio.projetospring.integrationTests.dto.AccountCredentialsDTO;
import br.tulio.projetospring.integrationTests.dto.PersonDTO;
import br.tulio.projetospring.integrationTests.dto.TokenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerCORSTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonDTO person;
    private static TokenDTO token;

    @BeforeAll //faz o setup antes de tudo
    static void setUp() {  //por isso precisa ser estatico
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
        token = new TokenDTO();
    }

    @Test
    @Order(1)
    void signIn() { //Devido a alteração na parte de segurança do codigo é necessario fazer login para fazer o resto
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("leandro", "admin123");

        token = given()                               //inicia uma requisição HTTP
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

        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson(); //passa dados ao person criado no setUp()

        specification = new RequestSpecBuilder()      //Basicamente monta o endpoint, com o header, params, etc, igual fazemos no postman
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.VALID_ORIGIN)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getAccessToken())
                .setBasePath("/people")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)                     //inicia uma requisição HTTP
                .contentType(MediaType.APPLICATION_JSON_VALUE) //Header param
                .body(person)                                  //header param
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body().asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Aleardo", createdPerson.getFirstName());
        assertEquals("Manacero", createdPerson.getLastName());
        assertEquals("São José do Rio Preto", createdPerson.getAddress());
        assertEquals("M", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());


    }

    @Test
    @Order(2)
    void createWithInvalidOriginTest() {
        mockPerson(); //passa dados ao person criado no setUp()

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.INVALID_ORIGIN)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getAccessToken())
                .setBasePath("/people")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE) //Header param
                .body(person)                                  //header param
                .when()
                    .post()
                .then()
                    .statusCode(403)
                .extract()
                    .body().asString();

        assertEquals("Invalid CORS request", content);

    }

    @Test
    @Order(3)
    void findByIDTest() throws JsonProcessingException {
        //não precisa do mock pois foi criado anteriormente
        //por isso existe uma ordem

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.VALID_ORIGIN)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getAccessToken())
                .setBasePath("/people")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE) //Header param
                .pathParams("id", person.getId())                             //header param
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                .extract()
                    .body().asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Aleardo", createdPerson.getFirstName());
        assertEquals("Manacero", createdPerson.getLastName());
        assertEquals("São José do Rio Preto", createdPerson.getAddress());
        assertEquals("M", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());

    }

    @Test
    @Order(4)
    void findByIDWithInvalidOriginTest() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.INVALID_ORIGIN)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + token.getAccessToken())
                .setBasePath("/people")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE) //Header param
                .pathParams("id", person.getId())                             //header param
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body().asString();

        assertEquals("Invalid CORS request", content);

    }

    /*
    @Test
    void update() {
    }
    //outros foram removidos
    */

    private void mockPerson() {
        person.setFirstName("Aleardo");
        person.setLastName("Manacero");
        person.setAddress("São José do Rio Preto");
        person.setGender("Male");
        person.setEnabled(true);
    }

}
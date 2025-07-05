package br.tulio.projetospring.integrationTests.controllers;

import br.tulio.projetospring.configs.TestConfigs;
import br.tulio.projetospring.integrationTests.AbstractIntegrationTest;
import br.tulio.projetospring.integrationTests.controllers.mappers.YAMLMapper;
import br.tulio.projetospring.integrationTests.dto.PersonDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerYAMLTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper objectMapper;

    private static PersonDTO person;

    @BeforeAll //faz o setup antes de tudo
    static void setUp() {  //por isso precisa ser estatico
        objectMapper = new YAMLMapper();
        person = new PersonDTO();

    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson(); //passa dados ao person criado no setUp()

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.VALID_ORIGIN)
                .setBasePath("/people")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var createdPerson = given().config(
                    RestAssuredConfig.config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                    )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE) //Header param
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(person, objectMapper)                                  //header param
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body().as(PersonDTO.class, objectMapper);

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
    void updateTest() throws JsonProcessingException {
        person.setLastName("Manacero jr.");

        var createdPerson = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE) //Header param
                .accept(MediaType.APPLICATION_YAML_VALUE)      //Header param para receber em JSON
                .body(person, objectMapper)                            //header param
                .when()
                    .put()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body().as(PersonDTO.class, objectMapper);

        person = createdPerson;

        assertNotNull(createdPerson.getId());
        //outros removidos por ser redundande -> lembre-se da sequencia dos testes

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Aleardo", createdPerson.getFirstName());
        assertEquals("Manacero jr.", createdPerson.getLastName());
        assertEquals("São José do Rio Preto", createdPerson.getAddress());
        assertEquals("M", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());

    }

    @Test
    @Order(3)
    void findByIDTest() throws JsonProcessingException {
        //não precisa do mock pois foi criado anteriormente
        //por isso existe uma ordem

        //Não precisa de outra 'specification' já que n tem CORS e n vai alterar nada

        var createdPerson = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE) //Header param
                .accept(MediaType.APPLICATION_YAML_VALUE)      //Header param para receber em JSON
                .pathParams("id", person.getId())                             //header param
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body().as(PersonDTO.class, objectMapper);

        person = createdPerson;

        assertNotNull(createdPerson.getId());
        //outros removidos por ser redundande

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Aleardo", createdPerson.getFirstName());
        assertEquals("Manacero jr.", createdPerson.getLastName());
        assertEquals("São José do Rio Preto", createdPerson.getAddress());
        assertEquals("M", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());

    }

    @Test
    @Order(4)
    void disableTest() throws JsonProcessingException {

        var createdPerson = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)      //Header param para receber em JSON
                .contentType(MediaType.APPLICATION_YAML_VALUE) //Header param
                .pathParams("id", person.getId())                             //header param
                .when()
                    .patch("{id}")
                .then()
                    .statusCode(200)
                .extract()
                    .body().as(PersonDTO.class, objectMapper);

        person = createdPerson;

        assertNotNull(createdPerson.getId());
        //outros removidos por ser redundande

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Aleardo", createdPerson.getFirstName());
        assertEquals("Manacero jr.", createdPerson.getLastName());
        assertEquals("São José do Rio Preto", createdPerson.getAddress());
        assertEquals("M", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());

    }

    @Test
    @Order(5)
    void deleteTest() throws JsonProcessingException {

        given().config(
                RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)      //Header param para receber em JSON
                .pathParams("id", person.getId())                             //header param
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);


    }

    @Test
    @Order(6)
    void findAllTest() throws JsonProcessingException {

        var content = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE) //Header param
                .when()
                    .get("/all")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                    .body().as(PersonDTO[].class, objectMapper);

        List<PersonDTO> people = Arrays.asList(content);

        //primeira pessoa da lista
        PersonDTO firstPerson = people.getFirst();
        person = firstPerson;

        assertNotNull(firstPerson.getId());
        assertTrue(firstPerson.getId() > 0);

        assertEquals("John", firstPerson.getFirstName());
        assertEquals("Doe", firstPerson.getLastName());
        assertEquals("Rua Benedito, 14", firstPerson.getAddress());
        assertEquals("M", firstPerson.getGender());
        assertTrue(firstPerson.getEnabled());

        //quarta pessoa
        PersonDTO thirdPerson = people.get(2);
        person = thirdPerson;

        assertNotNull(thirdPerson.getId());
        assertTrue(thirdPerson.getId() > 0);

        assertEquals("Albert", thirdPerson.getFirstName());
        assertEquals("Eintein", thirdPerson.getLastName());
        assertEquals("Alexandria, bairro nobre, 223", thirdPerson.getAddress());
        assertEquals("M", thirdPerson.getGender());
        assertTrue(thirdPerson.getEnabled());

        //sexta pessoa
        PersonDTO fifthPerson = people.get(4);
        person = fifthPerson;

        assertNotNull(fifthPerson.getId());
        assertTrue(fifthPerson.getId() > 0);

        assertEquals("Albert", fifthPerson.getFirstName());
        assertEquals("Eintein", fifthPerson.getLastName());
        assertEquals("Alexandria, bairro nobre, 223", fifthPerson.getAddress());
        assertEquals("M", fifthPerson.getGender());
        assertTrue(fifthPerson.getEnabled());

    }

    private void mockPerson() {
        person.setFirstName("Aleardo");
        person.setLastName("Manacero");
        person.setAddress("São José do Rio Preto");
        person.setGender("Male");
        person.setEnabled(true);
    }

}
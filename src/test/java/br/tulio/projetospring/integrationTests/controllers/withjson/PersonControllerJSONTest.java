package br.tulio.projetospring.integrationTests.controllers.withjson;

import br.tulio.projetospring.configs.TestConfigs;
import br.tulio.projetospring.integrationTests.AbstractIntegrationTest;
import br.tulio.projetospring.integrationTests.dto.PersonDTO;
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


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJSONTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonDTO person;

    @BeforeAll //faz o setup antes de tudo
    static void setUp() {  //por isso precisa ser estatico
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

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

        var content = given(specification)
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
    void updateTest() throws JsonProcessingException {
        person.setLastName("Manacero jr.");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE) //Header param
                .body(person)                            //header param
                .when()
                    .put()
                .then()
                    .statusCode(200)
                .extract()
                    .body().asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
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
        //outros removidos por ser redundande

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Aleardo", createdPerson.getFirstName());
        assertEquals("Manacero jr.", createdPerson.getLastName());
        assertEquals("São José do Rio Preto", createdPerson.getAddress());
        assertEquals("M", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());

    }

    private void mockPerson() {
        person.setFirstName("Aleardo");
        person.setLastName("Manacero");
        person.setAddress("São José do Rio Preto");
        person.setGender("Male");
        person.setEnabled(true);
    }

}
package br.tulio.projetospring.integrationTests.controllers;

import br.tulio.projetospring.configs.TestConfigs;
import br.tulio.projetospring.integrationTests.AbstractIntegrationTest;
import br.tulio.projetospring.integrationTests.dto.AccountCredentialsDTO;
import br.tulio.projetospring.integrationTests.dto.PersonDTO;
import br.tulio.projetospring.integrationTests.dto.TokenDTO;
import br.tulio.projetospring.integrationTests.dto.wrapper.WrapperPersonDTO;
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

import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.*;
import static org.junit.Assert.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

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
                .body(person)                                  //header param
                .when()
                    .post()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
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
                .accept(MediaType.APPLICATION_JSON_VALUE)      //Header param para receber em JSON
                .body(person)                            //header param
                .when()
                    .put()
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
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
                .accept(MediaType.APPLICATION_JSON_VALUE)      //Header param para receber em JSON
                .pathParams("id", person.getId())                             //header param
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
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

    @Test
    @Order(4)
    void disableTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)      //Header param para receber em JSON
                .contentType(MediaType.APPLICATION_JSON_VALUE) //Header param
                .pathParams("id", person.getId())                             //header param
                .when()
                    .patch("{id}")
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
        assertFalse(createdPerson.getEnabled());

    }

    @Test
    @Order(5)
    void deleteTest() throws JsonProcessingException {

        given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)      //Header param para receber em JSON
                .pathParams("id", person.getId())                             //header param
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);


    }

    @Test
    @Order(6)
    void findAllTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE) //Header param
                .queryParams("page", 3, "size", 12, "direction", "asc") //passa os query params da request
                .when()
                    .get("/all")
                .then()
                    .statusCode(200)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                    .body().asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class); //usa o wrapper pra ler os JSON baseado na sua propriedade _embedded
        List<PersonDTO> people = wrapper.getEmbedder().getPeople(); //monta os DTOs usando o outro wrapper pra ler os dados dentro de People
        //List<PersonDTO> people = objectMapper.readValue(content, new TypeReference<List<PersonDTO>>(){}); //antes de usar HAL

        //primeira pessoa da lista
        PersonDTO firstPerson = people.getFirst();
        person = firstPerson;

        assertNotNull(firstPerson.getId());
        assertTrue(firstPerson.getId() > 0);

        assertEquals("Allayne", firstPerson.getFirstName()); //dados coletados de um request semelhante e com mesmos parametros
        assertEquals("Hallwell", firstPerson.getLastName());
        assertEquals("0 Debs Terrace", firstPerson.getAddress());
        assertEquals("M", firstPerson.getGender());
        assertFalse(firstPerson.getEnabled());

        //quarta pessoa
        PersonDTO thirdPerson = people.get(2);
        person = thirdPerson;

        assertNotNull(thirdPerson.getId());
        assertTrue(thirdPerson.getId() > 0);

        assertEquals("Allister", thirdPerson.getFirstName());
        assertEquals("Garber", thirdPerson.getLastName());
        assertEquals("4350 Fair Oaks Alley", thirdPerson.getAddress());
        assertEquals("M", thirdPerson.getGender());
        assertFalse(thirdPerson.getEnabled());

        //sexta pessoa
        PersonDTO fifthPerson = people.get(4);
        person = fifthPerson;

        assertNotNull(fifthPerson.getId());
        assertTrue(fifthPerson.getId() > 0);

        assertEquals("Aloisia", fifthPerson.getFirstName());
        assertEquals("McAvinchey", fifthPerson.getLastName());
        assertEquals("25 Cambridge Crossing", fifthPerson.getAddress());
        assertEquals("F", fifthPerson.getGender());
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
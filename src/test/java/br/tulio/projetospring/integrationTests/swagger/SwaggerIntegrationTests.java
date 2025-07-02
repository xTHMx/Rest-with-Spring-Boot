package br.tulio.projetospring.integrationTests.swagger;

import br.tulio.projetospring.configs.TestConfigs;
import br.tulio.projetospring.integrationTests.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTests extends AbstractIntegrationTest {

	@Test
	void shouldDisplaySwaggerUIPage() {
		var content = given()
						.basePath("/swagger-ui/index.html")
							.port(TestConfigs.SERVER_PORT)
						.when()
							.get()
						.then()
							.statusCode(200)
						.extract()
							.body().asString()
		;

		assertTrue(content.contains("Swagger UI"));
	}

}

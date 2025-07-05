package br.tulio.projetospring.integrationTests;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:9.1.0")
                .withDatabaseName("projeto_spring"); //flyway não acha o banco de dados se não passar o nome nos testes -> usuario teste n tem permissao

        private void startContainers() {
            //inicia todos os conteiners de forma segura e paralela
            Startables.deepStart(Stream.of(mysql)).join(); //join funciona como um wait para todos terminarem
        }

        private static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", mysql.getJdbcUrl(),
                    "spring.datasource.username", mysql.getUsername(),
                    "spring.datasource.password", mysql.getPassword()
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment environment = applicationContext.getEnvironment(); //pega o contexto do spring
            MapPropertySource testcontainers = new MapPropertySource("testcontainers",    //adiciona as propriedades do testconteiners
                    (Map) createConnectionConfiguration()
                    );
            environment.getPropertySources().addFirst(testcontainers); //adiciona essa configurações antes de qualquer outra
        }
    }
}

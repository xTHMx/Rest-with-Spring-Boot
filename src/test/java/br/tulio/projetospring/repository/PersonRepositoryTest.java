package br.tulio.projetospring.repository;

import br.tulio.projetospring.integrationTests.AbstractIntegrationTest;
import br.tulio.projetospring.models.Person;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class) //integra o junit com o spring
@DataJpaTest  //configura o teste pra rodar com jpa, usa automaticamente um banco de dados imbutido (H2) para teste
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //Jpa substitui por banco de dados inbutido (H2), essa annotation tira isso, já que usamos o docker
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PersonRepository repository;

    private static Person person;

    @BeforeAll
    static void setUp() {
        person = new Person();
    }

    @Test
    @Order(1)
    void findPeopleByName() {
        Pageable pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC, "firstName"));

        person = repository.findPeopleByName("ert", pageable).getContent().get(0);

        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Albert", person.getFirstName());
        assertEquals("Eintein", person.getLastName());
        assertEquals("Alexandria, bairro nobre, 223", person.getAddress());
        assertEquals("Macho", person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(2)
    void disablePerson() {
        Long id = person.getId();
        repository.disablePerson(id);

        var result = repository.findById(id);
        person = result.get();

        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Albert", person.getFirstName());
        assertEquals("Eintein", person.getLastName());
        assertEquals("Alexandria, bairro nobre, 223", person.getAddress());
        assertEquals("Macho", person.getGender());
        assertFalse(person.getEnabled());
    }


}
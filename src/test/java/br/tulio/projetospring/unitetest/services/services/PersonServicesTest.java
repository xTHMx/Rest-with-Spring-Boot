package br.tulio.projetospring.unitetest.services.services;

import br.tulio.projetospring.data.dto.v1.PersonDTO;
import br.tulio.projetospring.exception.RequiredObjectIsNullException;
import br.tulio.projetospring.models.Person;
import br.tulio.projetospring.repository.PersonRepository;
import br.tulio.projetospring.services.PersonServices;
import br.tulio.projetospring.unitetest.mapper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


/*
    Criando testes usando o mockito para testar os links do HATEOAS
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

    MockPerson input;

    @InjectMocks
    private PersonServices service;

    @Mock //funciona como o Autowired
    PersonRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByID() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        var result = service.findByID(1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        //test GET link
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/people/1")
                        && link.getType().equals("GET")
                )
        );

        //test POST link
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/people")
                        && link.getType().equals("POST")
                )
        );

        //test PUT link
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/people")
                        && link.getType().equals("PUT")
                )
        );

        //test DELETE link
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/people/1")
                        && link.getType().equals("DELETE")
                )
        );

        //test All GET Link
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/people/all")
                        && link.getType().equals("GET")
                )
        );

        //Test variables
        assertEquals("Address Test1", person.getAddress());
        assertEquals("First Name Test1", person.getFirstName());
        assertEquals("Last Name Test1", person.getLastName());
        assertEquals("Female", person.getGender());


    }

    @Test
    void create() {
        Person person = input.mockEntity(1);
        Person persisted = person;
        persisted.setId(1L);

        PersonDTO dto = input.mockDTO(1);

        when(repository.save(person)).thenReturn(persisted);

        var result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        //test GET link
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/people/1")
                        && link.getType().equals("GET")
                )
        );

        //test POST link
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/people")
                        && link.getType().equals("POST")
                )
        );

        //test PUT link
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/people")
                        && link.getType().equals("PUT")
                )
        );

        //test DELETE link
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/people/1")
                        && link.getType().equals("DELETE")
                )
        );

        //test All GET Link
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/people/all")
                        && link.getType().equals("GET")
                )
        );

        //Test variables
        assertEquals("Address Test1", person.getAddress());
        assertEquals("First Name Test1", person.getFirstName());
        assertEquals("Last Name Test1", person.getLastName());
        assertEquals("Female", person.getGender());

    }

    @Test
    void testCreateWithNullPerson(){
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                 service.create(null);
                }
        );

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void update() {
        Person person = input.mockEntity(1);
        Person persisted = person;
        persisted.setId(1L);

        PersonDTO dto = input.mockDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(person));

        when(repository.save(person)).thenReturn(persisted);

        var result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        //test GET link
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/people/1")
                        && link.getType().equals("GET")
                )
        );

        //test POST link
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/people")
                        && link.getType().equals("POST")
                )
        );

        //test PUT link
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/people")
                        && link.getType().equals("PUT")
                )
        );

        //test DELETE link
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/people/1")
                        && link.getType().equals("DELETE")
                )
        );

        //test All GET Link
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/people/all")
                        && link.getType().equals("GET")
                )
        );

        //Test variables
        assertEquals("Address Test1", person.getAddress());
        assertEquals("First Name Test1", person.getFirstName());
        assertEquals("Last Name Test1", person.getLastName());
        assertEquals("Female", person.getGender());
    }

    @Test
    void testUpdateWithNullPerson(){
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> {
                    service.update(null);
                }
        );

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        service.delete(1L);
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Person.class));
        verifyNoMoreInteractions(repository);

    }

    @Test
    void findAll() {
        List<Person> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<PersonDTO> people = service.findAll();

        assertNotNull(people);
        assertEquals(14, people.size());

        var person1 = people.get(1);

        assertNotNull(person1);
        assertNotNull(person1.getId());
        assertNotNull(person1.getLinks());

        //test GET link
        assertNotNull(person1.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/people/1")
                        && link.getType().equals("GET")
                )
        );

        //test POST link
        assertNotNull(person1.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/people")
                        && link.getType().equals("POST")
                )
        );

        //test PUT link
        assertNotNull(person1.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/people")
                        && link.getType().equals("PUT")
                )
        );

        //test DELETE link
        assertNotNull(person1.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/people/1")
                        && link.getType().equals("DELETE")
                )
        );

        //test All GET Link
        assertNotNull(person1.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/people/all")
                        && link.getType().equals("GET")
                )
        );

        //Test variables
        assertEquals("Address Test1", person1.getAddress());
        assertEquals("First Name Test1", person1.getFirstName());
        assertEquals("Last Name Test1", person1.getLastName());
        assertEquals("Female", person1.getGender());

        var person5 = people.get(5);

        assertNotNull(person5);
        assertNotNull(person5.getId());
        assertNotNull(person5.getLinks());

        //test GET link
        assertNotNull(person5.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/people/5")
                        && link.getType().equals("GET")
                )
        );

        //test POST link
        assertNotNull(person5.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/people")
                        && link.getType().equals("POST")
                )
        );

        //test PUT link
        assertNotNull(person5.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/people")
                        && link.getType().equals("PUT")
                )
        );

        //test DELETE link
        assertNotNull(person5.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/people/5")
                        && link.getType().equals("DELETE")
                )
        );

        //test All GET Link
        assertNotNull(person5.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/people/all")
                        && link.getType().equals("GET")
                )
        );

        //Test variables
        assertEquals("Address Test5", person5.getAddress());
        assertEquals("First Name Test5", person5.getFirstName());
        assertEquals("Last Name Test5", person5.getLastName());
        assertEquals("Female", person5.getGender());

        var person10 = people.get(10);

        assertNotNull(person10);
        assertNotNull(person10.getId());
        assertNotNull(person10.getLinks());

        //test GET link
        assertNotNull(person10.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/people/10")
                        && link.getType().equals("GET")
                )
        );

        //test POST link
        assertNotNull(person10.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/people")
                        && link.getType().equals("POST")
                )
        );

        //test PUT link
        assertNotNull(person10.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/people")
                        && link.getType().equals("PUT")
                )
        );

        //test DELETE link
        assertNotNull(person10.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/people/10")
                        && link.getType().equals("DELETE")
                )
        );

        //test All GET Link
        assertNotNull(person10.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/people/all")
                        && link.getType().equals("GET")
                )
        );

        //Test variables
        assertEquals("Address Test10", person10.getAddress());
        assertEquals("First Name Test10", person10.getFirstName());
        assertEquals("Last Name Test10", person10.getLastName());
        assertEquals("Male", person10.getGender());

    }
}
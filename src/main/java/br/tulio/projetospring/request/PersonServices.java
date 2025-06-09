package br.tulio.projetospring.request;

import br.tulio.projetospring.exception.ResourceNotFoundException;
import br.tulio.projetospring.models.Person;
import br.tulio.projetospring.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository repository;

    public Person findByID(Long id) {
        logger.info("Finding one person by ID...");

        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));
    }

    public Person create(Person person) {
        logger.info("Creating a person...");
        return repository.save(person);
    }

    public Person update(Person person) {
        logger.info("Updating a person...");

        //check if exists and gets it
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        //update values
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return repository.save(person);
    }

    public void delete(Long id) {
        logger.info("Deleting a person...");

        //check if exists
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        repository.delete(entity);
    }

    public List<Person> findAll(){
        List<Person> people = new ArrayList<Person>();

        return repository.findAll();
    }

}

package br.tulio.projetospring.request;

import br.tulio.projetospring.models.People;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PeopleServices {

    private final AtomicLong counter = new AtomicLong();

    private Logger logger = Logger.getLogger(PeopleServices.class.getName());

    public People findByID(String id) {
        logger.info("Finding one person by ID...");

        //mockup
        People person = new People(
                counter.incrementAndGet(),
                "John",
                "Doe",
                "Rua Benedito, 14",
                "Macho"
        );

        return person;
    }

    public People create(People person) {
        logger.info("Creating a person...");
        return person;
    }

    public People update(People person) {
        logger.info("Updating a person...");
        return person;
    }

    public void delete(String id) {
        logger.info("Deleting a person...");
    }

    public List<People> findAll(){
        List<People> people = new ArrayList<People>();

        for(int i = 0; i < 10; i++){
            People person = new People(
                    counter.incrementAndGet(),
                    "name"+i,
                    "lastname"+i,
                    "Address"+i,
                    "Macho"
            );
            people.add(person);
        }

        return people;

    }

}

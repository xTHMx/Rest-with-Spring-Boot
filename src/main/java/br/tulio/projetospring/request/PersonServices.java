package br.tulio.projetospring.request;

import br.tulio.projetospring.data.dto.v1.PersonDTO;
import br.tulio.projetospring.data.dto.v2.PersonDTOV2;
import br.tulio.projetospring.exception.ResourceNotFoundException;
import br.tulio.projetospring.mapper.custom.PersonMapper;
import br.tulio.projetospring.models.Person;
import br.tulio.projetospring.repository.PersonRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;

import static br.tulio.projetospring.mapper.ObjectMapper.parseListObjects;
import static br.tulio.projetospring.mapper.ObjectMapper.parseObject;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();

    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonMapper personConverter;

    public PersonDTO findByID(Long id) {
        logger.info("Finding one person by ID...");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        return parseObject(entity, PersonDTO.class);
    }

    public PersonDTO create(PersonDTO person) {
        logger.info("Creating a person...");

        var entity = parseObject(person, Person.class);

        return parseObject(repository.save(entity), PersonDTO.class);
    }

    public PersonDTO update(PersonDTO person) {
        logger.info("Updating a person...");

        //check if exists and gets it
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        //update values
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());


        return parseObject(repository.save(entity), PersonDTO.class);
    }

    public void delete(Long id) {
        logger.info("Deleting a person...");

        //check if exists
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        repository.delete(entity);
    }

    public List<PersonDTO> findAll(){
        return parseListObjects(repository.findAll(), PersonDTO.class);
    }


    /// v2
    // Somente para treino de versionamento
    public PersonDTOV2 createV2(PersonDTOV2 person) {
        logger.info("Creating a person V2...");

        //o Dozer não iria funcionar aqui, então foi necessario criar um mapper customizado
        var entity = personConverter.convertDTOToEntity(person);

        return personConverter.convertEntityToDTO(repository.save(entity));
    }

}

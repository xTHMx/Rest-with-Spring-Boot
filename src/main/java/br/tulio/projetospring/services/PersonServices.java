package br.tulio.projetospring.services;

import br.tulio.projetospring.controllers.PersonController;
import br.tulio.projetospring.data.dto.v1.PersonDTO;
import br.tulio.projetospring.data.dto.v2.PersonDTOV2;
import br.tulio.projetospring.exception.RequiredObjectIsNullException;
import br.tulio.projetospring.exception.ResourceNotFoundException;
import br.tulio.projetospring.mapper.custom.PersonMapper;
import br.tulio.projetospring.models.Person;
import br.tulio.projetospring.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;

import static br.tulio.projetospring.mapper.ObjectMapper.parseListObjects;
import static br.tulio.projetospring.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonMapper personConverter;

    public PersonDTO findByID(Long id) {
        logger.info("Finding one person by ID...");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public PersonDTO create(PersonDTO person) {
        logger.info("Creating a person...");

        if(person == null) throw new RequiredObjectIsNullException(); //não pode ser nulo

        var entity = parseObject(person, Person.class);

        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public PersonDTO update(PersonDTO person) {
        logger.info("Updating a person...");

        if(person == null) throw new RequiredObjectIsNullException(); //não pode ser nulo

        //check if exists and gets it
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        //update values
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting a person...");

        //check if exists
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        repository.delete(entity);
    }

    @Transactional //infoma ao spring para usar os conceitos ACED - Atomicidade para evitar colisão de dados e corrupção dos mesmos
    public PersonDTO disablePerson(Long id) {
        logger.info("Disabling a person...");


        repository.findById(id) //vai pegar o valor da tabela e jogar na cache
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        repository.disablePerson(id);

        var entity = repository.findById(id).get(); //vai pegar o valor já na cache -> hibernate tem cache L2 -> precisa limpar pelo @Modifying

        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public Page<PersonDTO> findAll(Pageable pageable) {
        logger.info("Finding all people...");

        var foundPeople = repository.findAll(pageable);

        var peopleWithLinks = foundPeople.map(
                person -> {
                    var dto = parseObject(person, PersonDTO.class);
                    addHateoasLinks(dto);
                    return dto;
                });

        return peopleWithLinks;
    }


    /// v2
    // Somente para treino de versionamento
    public PersonDTOV2 createV2(PersonDTOV2 person) {
        logger.info("Creating a person V2...");

        //o Dozer não iria funcionar aqui, então foi necessario criar um mapper customizado
        var entity = personConverter.convertDTOToEntity(person);

        return personConverter.convertEntityToDTO(repository.save(entity));
    }


    /// HATEOAS
    private void addHateoasLinks(PersonDTO dto) {
        //link GET verb
        dto.add(linkTo(methodOn(PersonController.class).findByID(dto.getId())).withSelfRel().withType("GET")); //withSelfRel() diz que referencia o mesmo endpoint
        dto.add(linkTo(methodOn(PersonController.class).findAll(1, 10, "desc")).withRel("findAll").withType("GET"));
        //link DELETE verb
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        //link POST verb
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        //link PUT verb
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        //link PUT verb
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
    }

}

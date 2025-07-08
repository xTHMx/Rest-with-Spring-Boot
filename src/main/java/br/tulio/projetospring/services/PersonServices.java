package br.tulio.projetospring.services;

import br.tulio.projetospring.controllers.PersonController;
import br.tulio.projetospring.data.dto.v1.PersonDTO;
import br.tulio.projetospring.data.dto.v2.PersonDTOV2;
import br.tulio.projetospring.exception.BadRequestException;
import br.tulio.projetospring.exception.FileStorageException;
import br.tulio.projetospring.exception.RequiredObjectIsNullException;
import br.tulio.projetospring.exception.ResourceNotFoundException;
import br.tulio.projetospring.file.importer.contract.FileImporter;
import br.tulio.projetospring.file.importer.factory.FileImporterFactory;
import br.tulio.projetospring.mapper.custom.PersonMapper;
import br.tulio.projetospring.models.Person;
import br.tulio.projetospring.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static br.tulio.projetospring.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper personConverter;

    @Autowired
    FileImporterFactory fileImporter;

    @Autowired
    PagedResourcesAssembler<PersonDTO> pagedAssembler;

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

    //Le arquivo de tabela e converte em Person para o banco de dados
    public List<PersonDTO> loadFromFile(MultipartFile file) throws Exception {
        logger.info("Loading person from file...");

        if(file.isEmpty()) throw new BadRequestException("Please select a valid file");

        try (InputStream inputStream = file.getInputStream()){
            String fileName = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new BadRequestException("File name is cannot be null"));

            FileImporter importer = this.fileImporter.getImporter(fileName);

            List<Person> entities = importer.importFile(inputStream).stream() //importer importa um inputStream e retorna list de DTOs que devemos iterar. Pra persistir no banco devemos converter em person -> DTOs nao podem.
                    .map(dto -> repository.save(parseObject(dto, Person.class))) // iteramos pelos DTOs e convertemos em person já adicionando ao banco
                    .toList(); //retorna lista de entidades

            return entities.stream()
                    .map(entity -> { //itera pela lista e converte para DTO + add HATEOAS links
                        var dto = parseObject(entity, PersonDTO.class);
                        addHateoasLinks(dto);
                        return dto;
                    })
                    .toList();
        } catch (Exception e) {
            throw new FileStorageException("Error while processing file!", e);
        }

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

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("Finding all people...");

        var foundPeople = repository.findAll(pageable);

        return buildPagedModel(pageable, foundPeople);
    }

    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pageable) {
        logger.info("Finding all people...");

        var foundPeople = repository.findPeopleByName(firstName, pageable);

        return buildPagedModel(pageable, foundPeople);
    }


    /// v2
    // Somente para treino de versionamento
    public PersonDTOV2 createV2(PersonDTOV2 person) {
        logger.info("Creating a person V2...");

        //o Dozer não iria funcionar aqui, então foi necessario criar um mapper customizado
        var entity = personConverter.convertDTOToEntity(person);

        return personConverter.convertEntityToDTO(repository.save(entity));
    }

    //Cria o paged model do personDTO e adiciona os links do HATEOAS e HAL
    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<Person> foundPeople) {
        var peopleWithLinks = foundPeople.map(
                person -> {
                    var dto = parseObject(person, PersonDTO.class);
                    addHateoasLinks(dto);
                    return dto;
                });

        //Adicionando links HAL(_links e _embedded) para complementar o HATEOAS
        Link findAllLink = WebMvcLinkBuilder.linkTo(        //adiciona links adicionais do HAL 'bindando' na função findAll()
                        WebMvcLinkBuilder.methodOn(PersonController.class)
                                .findAll(pageable.getPageNumber(),
                                        pageable.getPageSize(),
                                        String.valueOf(pageable.getSort())))
                .withSelfRel();

        return pagedAssembler.toModel(peopleWithLinks, findAllLink);
    }


    /// HATEOAS
    //Adiciona links HATEOAS ao DTO
    private void addHateoasLinks(PersonDTO dto) {
        //link GET verb
        dto.add(linkTo(methodOn(PersonController.class).findByID(dto.getId())).withSelfRel().withType("GET")); //withSelfRel() diz que referencia o mesmo endpoint
        dto.add(linkTo(methodOn(PersonController.class).findAll(1, 10, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findByName("", 1, 12, "asc")).withSelfRel().withType("GET"));
        //link DELETE verb
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        //link POST verb
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        //link PUT verb
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        //link PUT verb
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));

        //methodOn(PersonController.class).findByID(dto.getId()) -> simula a chamada da função mas n executa, apenas pega a assinatura
        //linkTo(<função>) -> cria um link HATEOAS com a assinatura
        //withSelfRef -> Adiciona na seção self, indicando que o link retorna á mesma entidade
    }

}

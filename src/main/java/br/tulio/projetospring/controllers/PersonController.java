package br.tulio.projetospring.controllers;

import br.tulio.projetospring.controllers.docs.PersonControllerDocs;
import br.tulio.projetospring.data.dto.person.PersonDTO;
import br.tulio.projetospring.data.dto.person.v2.PersonDTOV2;
import br.tulio.projetospring.file.exporter.MediaTypes;
import br.tulio.projetospring.services.PersonServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//@CrossOrigin(origins = "http//localhost:8080") //Não implementei a nivel de controller
@RestController
@RequestMapping("/people")
//@Tag(name = "People", description = "End-points for Managing People") //Mark as swagger tag for documentation
public class PersonController implements PersonControllerDocs {

    @Autowired
    private PersonServices services;

    //quando não houver valor no mapping, é o metodo default (/person) que sera chamado
    //@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,          *Não se usa mais (Legado)
    //       produces = MediaType.APPLICATION_JSON_VALUE)
    // @CrossOrigin(origins = "http://localhost:8080") - implementado em nivel global
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override //override é usado pois a documentação da função está em outra classe para melhor legibilidade
    public PersonDTO create(@RequestBody PersonDTO person) { //@RequestBody faz retornar o body do request
        return services.create(person);
    }


    @PostMapping( value = "/loadPeopleFromFile",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public List<PersonDTO> loadPeopleFromFile(@RequestParam("file") MultipartFile file) {
        return services.loadPeopleFromFile(file);
    }


    //chama um endpoint com metodo get e retorna um json
    // @CrossOrigin(origins = "http://localhost:8080") //se estiver vazio aceita qualquer origem - implementado em nivel global
    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public PersonDTO findByID(@PathVariable("id") Long id) {
        return services.findByID(id);
    }


    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public PersonDTO update(@RequestBody PersonDTO person) { //requestbody faz retornar o body do request
        return services.update(person);
    }


    @PatchMapping( value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public PersonDTO disablePerson(@PathVariable("id") Long id) { //requestbody faz retornar o body do request
        return services.disablePerson(id);
    }


    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) { //requestbody faz retornar o body do request
        services.delete(id);
        return ResponseEntity.noContent().build(); //retorna o status correto (204)
    }


    @GetMapping(value = "/all",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page, //Pagina da pesquisa
            @RequestParam(value = "size", defaultValue = "10") Integer size, //Tamanho de instancias por pagina
            @RequestParam(value = "direction", defaultValue = "asc") String direction //Consulta retorna de forma ascendente ou descendente baseado em um paramentro
    ){
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName")); //Sort.by() faz com que o retorno seja ordenado baseado no firsrt name -> ordena primeiro TODOS antes de pegar por pagina
        return ResponseEntity.ok(services.findAll(pageable));
    }


    @GetMapping(value = "/findByName/{firstName}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findByName(
            @PathVariable(value = "firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page, //Pagina da pesquisa
            @RequestParam(value = "size", defaultValue = "10") Integer size, //Tamanho de instancias por pagina
            @RequestParam(value = "direction", defaultValue = "asc") String direction //Consulta retorna de forma ascendente ou descendente baseado em um paramentro
    ){
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName")); //Sort.by() faz com que o retorno seja ordenado baseado no firsrt name -> ordena primeiro TODOS antes de pegar por pagina

        return ResponseEntity.ok(services.findByName(firstName, pageable));
    }

    @GetMapping(value = "/exportPage",
            produces = {MediaTypes.APPLICATION_XLSX_VALUE, MediaTypes.APPLICATION_CSV_VALUE}
    )
    @Override
    public ResponseEntity<Resource> exportPage(Integer page, Integer size, String direction, HttpServletRequest request) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName")); //Sort.by() faz com que o retorno seja ordenado baseado no firsrt name -> ordena primeiro TODOS antes de pegar por pagina

        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        Resource file = services.exportPeoplePage(pageable, acceptHeader);

        var contentType = acceptHeader != null ? acceptHeader : "application/octet-stream"; //se não vir nada Accept com tipo padrão
        var fileExtension = MediaTypes.APPLICATION_XLSX_VALUE.equals(acceptHeader) ? ".xlsx" : ".csv";
        var fileName = "people_exported" + fileExtension;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file);
    }


    /// V2
    // Somente para treinar o uso de versionamento
    @PostMapping(value = "/v2", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public PersonDTOV2 create(@RequestBody PersonDTOV2 person) { //@RequestBody faz retornar o body do request
        return services.createV2(person);
    }
}

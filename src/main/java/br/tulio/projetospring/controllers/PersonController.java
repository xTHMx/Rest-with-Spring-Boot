package br.tulio.projetospring.controllers;

import br.tulio.projetospring.models.Person;
import br.tulio.projetospring.request.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PersonController {

    @Autowired
    private PersonServices services;

    //quando não houver valor no mapping, é o metodo default (/person) que sera chamado
    //@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,          *Não se usa mais (Legado)
    //       produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Person create(@RequestBody Person person) { //@RequestBody faz retornar o body do request
        return services.create(person);
    }

    //chama um endpoint com metodo get e retorna um json
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person findByID(@PathVariable("id") Long id) {
        return services.findByID(id);
    }

    @PutMapping( consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Person update(@RequestBody Person person) { //requestbody faz retornar o body do request
        return services.update(person);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) { //requestbody faz retornar o body do request
        services.delete(id);
        return ResponseEntity.noContent().build(); //retorna o status correto (204)
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> findAll() {
        return services.findAll();
    }
}

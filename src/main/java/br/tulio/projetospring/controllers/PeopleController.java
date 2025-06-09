package br.tulio.projetospring.controllers;

import br.tulio.projetospring.models.People;
import br.tulio.projetospring.request.PeopleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private PeopleServices services;

    //quando não houver valor no mapping, é o metodo default (/person) que sera chamado
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public People create(@RequestBody People person) { //@RequestBody faz retornar o body do request
        return services.create(person);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public People update(@RequestBody People person) { //requestbody faz retornar o body do request
        return services.update(person);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") String id) { //requestbody faz retornar o body do request
        services.delete(id);
    }

    //chama um endpoint com metodo get e retorna um json
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public People findByID(@PathVariable("id") String id) {
        return services.findByID(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<People> findAll() {
        return services.findAll();
    }
}

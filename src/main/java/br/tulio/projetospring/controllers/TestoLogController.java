package br.tulio.projetospring.controllers;

import br.tulio.projetospring.request.PersonServices;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;


@RestController
public class TestoLogController {

    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @GetMapping("/test")
    public String testLog(){
        return "Log Generated Successfully";
    }
}

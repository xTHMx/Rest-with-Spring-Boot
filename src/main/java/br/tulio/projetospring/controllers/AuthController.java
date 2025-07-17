package br.tulio.projetospring.controllers;

import br.tulio.projetospring.controllers.docs.AuthControllerDocs;
import br.tulio.projetospring.data.dto.security.AccountCredentialsDTO;
import br.tulio.projetospring.data.dto.security.TokenDTO;
import br.tulio.projetospring.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//@Tag(name = "Authentication Endpoint")
@Controller
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

    @Autowired
    private AuthServices services;


    @PostMapping("/signin")
    @Override
    public ResponseEntity<?> signIn(@RequestBody AccountCredentialsDTO credentials) {
        if(credentialsAreInvalid(credentials)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request: Invalid Credentials");
        }

        var token = services.signIn(credentials);
        if(token == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request: Invalid Token");
        }

        //return ResponseEntity.ok().body(token); //monta uma resposta com header, body (com o nosso token nele) e status code 200
        return token; //mas precisamos retornar somente o token
    }

    @PutMapping(value = "/refresh/{username}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Override
    public ResponseEntity<?> refreshToken(@PathVariable("username") String userName, @RequestHeader("Authorization") String refreshToken) {
        if(parametersAreInvalid(userName, refreshToken)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request: Invalid Credentials");
        }

        var token = services.refreshToken(userName, refreshToken);
        if(token == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client Request: Invalid Token");
        }

        return ResponseEntity.ok().body(token);
    }

    @PostMapping( value = "/createUser",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<AccountCredentialsDTO> createUser(@RequestBody AccountCredentialsDTO credentials) {
        return services.create(credentials);
    }


    private boolean parametersAreInvalid(String userName, String refreshToken) {
        return StringUtils.isBlank(userName) || StringUtils.isBlank(refreshToken);
    }

    private static Boolean credentialsAreInvalid(AccountCredentialsDTO credentials) {
        return (credentials == null || StringUtils.isBlank(credentials.getPassword()) || StringUtils.isBlank(credentials.getUserName()));
    }
}

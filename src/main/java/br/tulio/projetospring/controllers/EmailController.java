package br.tulio.projetospring.controllers;

import br.tulio.projetospring.controllers.docs.EmailControllerDocs;
import br.tulio.projetospring.data.dto.request.EmailRequestDTO;
import br.tulio.projetospring.services.EmailServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/email")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailServices services;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequestDTO) {
        services.sendSimpleEmail(emailRequestDTO);

        return new ResponseEntity<>("Email send successfully!", HttpStatus.OK);
    }

    @PostMapping(value = "/withAttachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<String> sendEmailWithAttachment(@RequestParam("emailRequest") String emailRequestJson, @RequestParam("attachment") MultipartFile file) {
        services.sendEmailwithAttachment(emailRequestJson, file);

        return new ResponseEntity<>("E-mail with attachments send successfully!", HttpStatus.OK);
    }
}

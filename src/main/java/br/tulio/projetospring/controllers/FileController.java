package br.tulio.projetospring.controllers;

import br.tulio.projetospring.controllers.docs.FileControllerDocs;
import br.tulio.projetospring.data.dto.person.UploadFileResponseDTO;
import br.tulio.projetospring.services.FileStorageServices;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


//Permite fazer o upload e download de arquivos, pode ser de qualquer tipo imagens, .pdf, audios e videos (usando o MultipartFile).
@RestController
@RequestMapping("/file")
public class FileController implements FileControllerDocs {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    FileStorageServices services;

    @PostMapping("/uploadFile")
    @Override
    public UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file) { //RequestParam é os parametros passados no request (igual do postman)
        logger.info("Uploading file " + file.getOriginalFilename());

        String fileName = services.storeFile(file); //Guarda o arquivo

        // http://localhost:8080/file/uploadFile/arquivo.docx
        var fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath() //Monta a URL que vai retornar ao client -> para que ele faça download
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponseDTO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    @Override
    public List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        logger.info("Uploading files... ");

        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    @Override
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        logger.info("Downloading file " + fileName);

        Resource resource = services.loadFileAsResource(fileName); //le o arquivo em disco
        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath()); //tenta descobrir qual é o tipo do arquivo
        }catch (Exception e) {
            logger.error("Could not determine file type. " + e.getMessage());
        }

        if(contentType == null) {
            contentType = "application/octet-stream"; //se não souber o tipo, passa um tipo generico -> octet-stream é o mais generico
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType)) //passa o tipo do arquivo
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment: fileName=\"" + resource.getFilename() + "\"") //Diz no cabeçalho da response que vai passar um anexo (nome do arquivo no caso)
                .body(resource); //e passa o recurso como body
    }
}

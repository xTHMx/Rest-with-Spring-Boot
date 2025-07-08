package br.tulio.projetospring.services;

import br.tulio.projetospring.config.FileStorageConfig;
import br.tulio.projetospring.controllers.FileController;
import br.tulio.projetospring.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageServices {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServices.class);

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageServices(FileStorageConfig fileStorageConfig) {
        this.fileStorageLocation  = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath().toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
            logger.info("Creating directories");
        } catch (IOException e) {
            throw new FileStorageException("Could not create directory where files would be stored", e);
        }

    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")){ //Se tiver ".." o caminho é invalido
                logger.error("Invalid file name! The file contains invalid path sequence ");
                throw new FileStorageException("Invalid file name! The file contains invalid path sequence " + fileName);
            }

            if (file.isEmpty()) {
                throw new FileStorageException("O arquivo está vazio");
            }

            logger.info("Storing file in disc");

            Path targetLocation = this.fileStorageLocation.resolve(fileName); //Resolve o caminho e transforma em path
            logger.info("Target location: " + targetLocation.toString());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING); //copia para disco em forma de InputStream, para nosso path e troca caso já exista
            return fileName;

        }catch (Exception e){
            logger.error("Could not store file ");
            throw new FileStorageException("Could not store file " + fileName, e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()) {
                return resource;
            }else{
                logger.error("File not found " + fileName);
                throw new FileStorageException("File not found" + fileName);
            }

        } catch (Exception e) {
            logger.error("File not found " + fileName, e);
            throw new FileStorageException("File not found" + fileName,e);
        }
    }

}



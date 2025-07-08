package br.tulio.projetospring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file") //Acessa as propridades dentro da seção "file" no application.yml
public class FileStorageConfig {

    private String uploadDir; //Deve ser o mesmo nome que que no .yml pra pegar automaticamente o valor da cofig

    public FileStorageConfig() {}

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}

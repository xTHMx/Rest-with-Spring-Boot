package br.tulio.projetospring.integrationTests.dto.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

//Usado para mapear o nosso novo body da resposta após a adição do HAL, devido á sua nova estrutura (_embedded e _links)
//Basicamente vai ler o que esta escrito dentro do _embedded
public class WrapperPersonDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private PersonEmbeddedDTO embedder;

    public WrapperPersonDTO() {} //Serializer precisa de um construtor sem argumentos

    public PersonEmbeddedDTO getEmbedder() {
        return embedder;
    }

    public void setEmbedder(PersonEmbeddedDTO embedder) {
        this.embedder = embedder;
    }
}

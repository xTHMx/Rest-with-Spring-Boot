package br.tulio.projetospring.integrationTests.dto.wrapper;

import br.tulio.projetospring.integrationTests.dto.PersonDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

//Usado para mapear o nosso novo body da resposta após a adição do HAL, devido á sua nova estrutura (_embedded e _links)
//Basicamente vai ler o que esta escrito dentro do People
public class PersonEmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("People")
    private List<PersonDTO> people;

    public PersonEmbeddedDTO() {} //Serializer precisa de um construtor sem argumentos

    public List<PersonDTO> getPeople() {
        return people;
    }

    public void setPeople(List<PersonDTO> people) {
        this.people = people;
    }
}

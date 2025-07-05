package br.tulio.projetospring.tools.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class GenderSerializer extends JsonSerializer<String> {

    public void serialize(String gender, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        String formatedString;

        if(gender.equals("Macho") || gender.equals("Masculino") || gender.equals("Male") || gender.equals("M")){
            formatedString = "M";
        }
        else{
            formatedString = "F";
        }

        //escreve o resultado para enviar --- sem isso causava o erro Could not write JSON: Can not write a field name, expecting a value
        gen.writeString(formatedString);

    }
}

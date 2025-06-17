package br.tulio.projetospring.tools.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class GenderSerializer extends JsonSerializer<String> {

    public void serialize(String gender, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        String formatedString = "Macho".equals(gender) ? "M" : "F";
    }
}

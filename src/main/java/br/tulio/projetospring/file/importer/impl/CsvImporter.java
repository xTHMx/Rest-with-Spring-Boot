package br.tulio.projetospring.file.importer.impl;

import br.tulio.projetospring.data.dto.person.PersonDTO;
import br.tulio.projetospring.file.importer.contract.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvImporter implements FileImporter {

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
        CSVFormat format = CSVFormat.Builder.create() //Cria nosso formato de CSV e configura como vai utilizar
                .setHeader() //diz que tem cabeçalho
                .setSkipHeaderRecord(true) //ignora o cabeçalho da tabela
                .setIgnoreEmptyLines(true) //ignora linhas em branco
                .setTrim(true) //remove espaços antes e depois do valor
                .build(); //retorna o formato

        //Iterable<CSVRecord> records = format.parse(new InputStreamReader(inputStream)); //Pega cada linha da tabela CSV e convertendo em uma lista iterativa de records
        CSVParser parser = CSVParser.parse(new InputStreamReader(inputStream), format);
        Iterable<CSVRecord> records = parser.getRecords();

        return parseRecordsToPersonDTO(records);
    }

    private List<PersonDTO> parseRecordsToPersonDTO(Iterable<CSVRecord> records) {
        List<PersonDTO> people = new ArrayList<>();

        for (CSVRecord record : records) {
            PersonDTO person = new PersonDTO();
            person.setFirstName(record.get("first_name"));
            person.setLastName(record.get("last_name"));
            person.setAddress(record.get("address"));
            person.setGender(record.get("gender"));
            person.setEnabled(true);

            people.add(person);
        }

        return people;
    }
}

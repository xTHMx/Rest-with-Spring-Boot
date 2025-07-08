package br.tulio.projetospring.file.importer.impl;

import br.tulio.projetospring.data.dto.v1.PersonDTO;
import br.tulio.projetospring.file.importer.contract.FileImporter;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class XlsxImporter implements FileImporter {

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {

        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0); //pega a tabela na posição tal

            Iterator<Row> rowIterator = sheet.iterator(); //cria um iterador de tabelas

            if(rowIterator.hasNext()){ //pulamos a primeira linha da planilha
                rowIterator.next();
            }

            return parseRowsToPersonDTOList(rowIterator);

        }
        
    }

    private List<PersonDTO> parseRowsToPersonDTOList(Iterator<Row> rowIterator) {
        List<PersonDTO> people = new ArrayList<>();
        
        while(rowIterator.hasNext()){ //itera por cada linha da tabela
            Row row = rowIterator.next();
            if(isRowValid(row)){ //verifica se a linha é valida
                people.add(parseRowToPersonDTO(row)); //faz a conversão dos valores em DTO e joga na lista
            }
        }

        return people;
    }

    //Converte a Row em PersonDTO
    private PersonDTO parseRowToPersonDTO(Row row) {
        PersonDTO person = new PersonDTO();
        person.setFirstName(row.getCell(0).getStringCellValue());
        person.setLastName(row.getCell(1).getStringCellValue());
        person.setAddress(row.getCell(2).getStringCellValue());
        person.setGender(row.getCell(3).getStringCellValue());
        person.setEnabled(true);

        return person;
    }

    //Verifica se o Row é valido
    private static boolean isRowValid(Row row) {
        //Se não for null ou vazio
        return row.getCell(0) != null && row.getCell(0).getCellType() == CellType.BLANK;
    }
}

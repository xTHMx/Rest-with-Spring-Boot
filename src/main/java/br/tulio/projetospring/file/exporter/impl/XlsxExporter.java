package br.tulio.projetospring.file.exporter.impl;

import br.tulio.projetospring.data.dto.person.PersonDTO;
import br.tulio.projetospring.file.exporter.contract.FileExporter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class XlsxExporter implements FileExporter {

    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("People");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "First Name", "Last Name", "Address", "Gender", "Enabled"};

            for (int i = 0; i < headers.length; i++) { //Cria as celulas na Row e preenche com so valores de header
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createCellStyle(workbook));
            }

            int rowIndex = 1;
            for (PersonDTO person : people) { //passa valores para as celulas na Row
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(person.getId());
                row.createCell(1).setCellValue(person.getFirstName());
                row.createCell(2).setCellValue(person.getLastName());
                row.createCell(3).setCellValue(person.getAddress());
                row.createCell(4).setCellValue(person.getGender());
                row.createCell(5).setCellValue(
                        (person.getEnabled() != null && person.getEnabled()) ? "Yes" : "No"
                );
            }

            for (int i = 0; i < headers.length; i++) { //Ajusta o tamanho das colunas automaticamente
                sheet.autoSizeColumn(i);
            }

            //Para para um output para ser escrito
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());

        }
    }
    
    private CellStyle createCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        return style;
    }
}

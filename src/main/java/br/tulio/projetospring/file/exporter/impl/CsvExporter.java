package br.tulio.projetospring.file.exporter.impl;

import br.tulio.projetospring.data.dto.v1.PersonDTO;
import br.tulio.projetospring.file.exporter.contract.FileExporter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CsvExporter implements FileExporter {

    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {
        return null;
    }
}

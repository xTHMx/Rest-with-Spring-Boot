package br.tulio.projetospring.file.exporter.contract;

import br.tulio.projetospring.data.dto.v1.PersonDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws Exception;


}

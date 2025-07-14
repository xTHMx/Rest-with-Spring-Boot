package br.tulio.projetospring.file.exporter.contract;

import br.tulio.projetospring.data.dto.person.PersonDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws Exception;


}

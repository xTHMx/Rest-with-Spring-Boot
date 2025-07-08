package br.tulio.projetospring.file.importer.contract;

import br.tulio.projetospring.data.dto.v1.PersonDTO;

import java.io.InputStream;
import java.util.List;

//Utilizados Apache commons csv e apache poi

//Importa tabelas para serem utilizadas no programa
public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;

}

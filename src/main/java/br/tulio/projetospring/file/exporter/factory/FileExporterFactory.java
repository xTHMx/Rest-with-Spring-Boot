package br.tulio.projetospring.file.exporter.factory;

import br.tulio.projetospring.file.exporter.MediaTypes;
import br.tulio.projetospring.file.exporter.contract.FileExporter;
import br.tulio.projetospring.file.exporter.impl.CsvExporter;
import br.tulio.projetospring.file.exporter.impl.XlsxExporter;
import br.tulio.projetospring.file.importer.contract.FileImporter;
import br.tulio.projetospring.file.importer.impl.CsvImporter;
import br.tulio.projetospring.file.importer.impl.XlsxImporter;
import br.tulio.projetospring.models.Person;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileExporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    @Autowired
    private ApplicationContext context; // injete automaticamente o ApplicationContext da aplicação, dá acesso direto ao "coração" do Spring, permitindo interagir com os beans e a configuração do projeto em tempo de execução.

    public FileExporter getExporter(String acceptHeader) throws Exception {
        if(acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
            return context.getBean(XlsxExporter.class); //new XlsxImporter(fileName); -> não é legal ficar usando new, use um Bean para deixar que o spring faça o trabalho de instanciação

        }else if(acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)){
            return context.getBean(CsvExporter.class); //new CsvImporter(fileName);

        } else {
            throw new BadRequestException("Invalid file format!");

        }
    }

}

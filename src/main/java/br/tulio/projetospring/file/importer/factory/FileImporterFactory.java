package br.tulio.projetospring.file.importer.factory;

import br.tulio.projetospring.file.importer.contract.FileImporter;
import br.tulio.projetospring.file.importer.impl.CsvImporter;
import br.tulio.projetospring.file.importer.impl.XlsxImporter;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileImporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private ApplicationContext context; // injete automaticamente o ApplicationContext da aplicação, dá acesso direto ao "coração" do Spring, permitindo interagir com os beans e a configuração do projeto em tempo de execução.

    public FileImporter getImporter(String fileName) throws Exception {
        if(fileName.endsWith(".xlsx")){
            return context.getBean(XlsxImporter.class); //new XlsxImporter(fileName); -> não é legal ficar usando new, use um Bean para deixar que o spring faça o trabalho de instanciação

        }else if(fileName.endsWith(".csv")){
            return context.getBean(CsvImporter.class); //new CsvImporter(fileName);

        } else {
            throw new BadRequestException("Invalid file format!");

        }
    }

}

package br.tulio.projetospring.configs;

public interface TestConfigs {

    int SERVER_PORT = 80;   //Se trocar pra 80 (porta aberta pelo docker compose) e tiver a api subida no docker junto com um container do sql,
                            // as requisições aparecem no log do docker da api
    String HEADER_PARAM_AUTHORIZATION = "Authorization";
    String HEADER_PARAM_ORIGIN = "Origin";

    String VALID_ORIGIN = "http://localhost:80"; //"http://localhost:8888"; (Sem estar no docker)
    String INVALID_ORIGIN = "http://www.siteficticio.com";
}


/*
    INFO de Aprendizado:
    -Consigo alterar a parte da configuração mesmo com a api subida
    -Consigo ver logs dos request olhando o logs dos containers
    -Todas as portas devem ser setadas para a selecionada no container (80) -> troquei todos oso 8888 por 80, exceto na application.yml
    -A mesma porta deve ser configurada no application.yml da main

    Docker:
    -Não preciso ter uma image da api no docker compose mas preciso ter um repositorio no dockerhub mesmo vazio
    -Posso enviar a imagem que foi construida (pelo docker compose) para o hub depois de rodar a api pela primeira vez
 */
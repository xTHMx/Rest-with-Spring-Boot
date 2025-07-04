package br.tulio.projetospring.configs;

public interface TestConfigs {

    int SERVER_PORT = 8888;
    String HEADER_PARAM_AUTHORIZATION = "Authorization";
    String HEADER_PARAM_ORIGIN = "Origin";

    String VALID_ORIGIN = "http://localhost:8888";
    String INVALID_ORIGIN = "http://www.siteficticio.com";
}

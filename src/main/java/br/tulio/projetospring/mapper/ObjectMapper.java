package br.tulio.projetospring.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

/*
    Utilizado para mapear objetos em DTOs e vice versa
    As funçoes parseObject() e parseListObjects() são usadas para isso
    basicamente:
    Isolar a responsabilidade de transformar dados de uma camada para outra. Onde campos como senhas e roles não devem ser enviados.
    Entidade: Objeto que reflete o banco de dados (UserEntity).
    DTO: Objeto que trafega na API (UserDTO).
 */
public class ObjectMapper {

    //Cria o mapper padrão
    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    //faz parse de entidade para DTO e DTO para entidade
    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    //o mesmo que o parseObject mas agora em uma lista toda
    public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {

        List<D> list = new ArrayList<D>();
        for(Object o : origin) {
            list.add(mapper.map(o, destination));
        }

        return list;
    }

}

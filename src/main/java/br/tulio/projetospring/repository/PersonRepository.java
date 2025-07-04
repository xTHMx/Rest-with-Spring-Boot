package br.tulio.projetospring.repository;

import br.tulio.projetospring.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person,Long> {

    @Modifying(clearAutomatically = true) //Limpa o cache para caso precisamos pegar sempre direto da tabela atualizado
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id")
    void disablePerson(@Param("id")  Long id);


}

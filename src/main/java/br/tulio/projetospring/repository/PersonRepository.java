package br.tulio.projetospring.repository;

import br.tulio.projetospring.models.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person,Long> {

    @Modifying(clearAutomatically = true) //Limpa o cache para caso precisamos pegar sempre direto da tabela atualizado
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id = :id") // :<variavel> -> ':' significa que é um parametro nomeado
    void disablePerson(@Param("id") Long id);

    //LIKE LOWER(CONCAT('%',:firstName,'%')) -> Aceita qualquer valor e compara com qualquer parte do nome, ex) AND - leANDro, alesANDra, ANDre
    @Query("SELECT p FROM Person p WHERE p.firstName LIKE LOWER(CONCAT('%',:firstName,'%'))")
    Page<Person> findPeopleByName(@Param("firstName") String firstName, Pageable pageable);

}

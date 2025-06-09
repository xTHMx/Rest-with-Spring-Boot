package br.tulio.projetospring.repository;

import br.tulio.projetospring.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person,Long> {

}

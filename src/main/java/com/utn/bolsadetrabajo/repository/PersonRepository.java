package com.utn.bolsadetrabajo.repository;

import com.utn.bolsadetrabajo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByidentification(String dni);

    @Query(value = "SELECT * FROM person WHERE person.user_id = :userId", nativeQuery = true)
    Person findByUserId(Long userId);
}

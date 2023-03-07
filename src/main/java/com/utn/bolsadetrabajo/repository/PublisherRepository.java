package com.utn.bolsadetrabajo.repository;

import com.utn.bolsadetrabajo.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    @Query(value = "SELECT * FROM publisher WHERE publisher.user_id = :userId", nativeQuery = true)
    Publisher findByUserId(Long userId);
}

package com.utn.bolsadetrabajo.repository;

import com.utn.bolsadetrabajo.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    @Query(value = "SELECT * FROM applicant WHERE applicant.user_id = :userId", nativeQuery = true)
    Applicant findByUserId(Long userId);
}

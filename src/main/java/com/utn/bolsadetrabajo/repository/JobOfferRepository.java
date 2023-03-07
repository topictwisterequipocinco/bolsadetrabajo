package com.utn.bolsadetrabajo.repository;

import com.utn.bolsadetrabajo.model.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {

    @Query(value = "SELECT * FROM JOBOFFER WHERE state = :state", nativeQuery = true)
    List<JobOffer> findAllByState(String state);

    @Query(value = "SELECT * FROM JOBOFFER WHERE id = :id", nativeQuery = true)
    boolean findSearchById(Long id);
}

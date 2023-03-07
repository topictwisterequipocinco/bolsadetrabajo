package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Subscriber;
import com.utn.bolsadetrabajo.service.crud.Removable;
import com.utn.bolsadetrabajo.service.crud.Writeable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ApplicantService extends Removable, Writeable<PersonDTO> {

    ResponseEntity<?> sendGetPersonByRequestById(Person person, Long id);

    ResponseEntity<?> getByIdUserApp(Subscriber subscriber);

    void postulateJobOffer(Applicant applicant);

    List<Applicant> getAll();

    Applicant getApplicantByUser(Subscriber subscriber);
}

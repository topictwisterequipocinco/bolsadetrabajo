package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Publisher;
import com.utn.bolsadetrabajo.model.Subscriber;
import com.utn.bolsadetrabajo.service.crud.Removable;
import com.utn.bolsadetrabajo.service.crud.Writeable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PublisherService extends Removable, Writeable<PersonDTO> {

    ResponseEntity<?> sendGetPersonByRequestById(Person person, Long id);

    ResponseEntity<?> getByIdUserPub(Subscriber subscriber);

    void addJobOffer(Publisher publisher);

    List<Publisher> getAll();

    Publisher getPublisherByUser(Subscriber subscriber);
}

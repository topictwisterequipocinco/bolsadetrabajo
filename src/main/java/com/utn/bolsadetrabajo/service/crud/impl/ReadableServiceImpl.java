package com.utn.bolsadetrabajo.service.crud.impl;

import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Publisher;
import com.utn.bolsadetrabajo.model.Subscriber;
import com.utn.bolsadetrabajo.model.enums.Roles;
import com.utn.bolsadetrabajo.repository.PersonRepository;
import com.utn.bolsadetrabajo.service.crud.Readable;
import com.utn.bolsadetrabajo.service.interfaces.ApplicantService;
import com.utn.bolsadetrabajo.service.interfaces.PersonService;
import com.utn.bolsadetrabajo.service.interfaces.PublisherService;
import com.utn.bolsadetrabajo.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReadableServiceImpl implements Readable {

    @Autowired PersonRepository repository;
    @Autowired PersonService personService;
    @Autowired ApplicantService applicantService;
    @Autowired PublisherService publisherService;
    @Autowired UserService userService;

    @Override
    public ResponseEntity<?> getById(Long id) {
        Person person = repository.findById(id).get();
        if (person.getSubscriber().getRole().getRole().equals(Roles.APPLICANT)) {
            return applicantService.sendGetPersonByRequestById(person, id);
        } else if (person.getSubscriber().getRole().getRole().equals(Roles.PUBLISHER)) {
            return publisherService.sendGetPersonByRequestById(person, id);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(person);
        }
    }

    @Override
    public ResponseEntity<?> getByIdentification(String identification) {
        Person person = repository.findByidentification(identification);
        if (person.getSubscriber().getRole().getRole().equals(Roles.APPLICANT)) {
            return applicantService.sendGetPersonByRequestById(person, Long.valueOf(identification));
        } else if (person.getSubscriber().getRole().getRole().equals(Roles.PUBLISHER)) {
            return publisherService.sendGetPersonByRequestById(person, Long.valueOf(identification));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(person);
        }
    }

    @Override
    public ResponseEntity<?> getByIdUser(Long id) {
        Subscriber subscriber = getUserById(id);
        if (subscriber.getRole().getRole().equals(Roles.APPLICANT)) {
            return applicantService.getByIdUserApp(subscriber);
        } else if (subscriber.getRole().getRole().equals(Roles.PUBLISHER)) {
            return publisherService.getByIdUserPub(subscriber);
        } else {
            return personService.sendGetPersonByUserId(subscriber);
        }
    }

    @Override
    public Applicant getPersonTypeApplicantByIdUser(Long id) {
        Subscriber subscriber = getUserById(id);
        return applicantService.getApplicantByUser(subscriber);
    }

    @Override
    public Publisher getPersonTypePublisherByIdUser(Long id) {
        Subscriber subscriber = getUserById(id);
        return publisherService.getPublisherByUser(subscriber);
    }

    private Subscriber getUserById(Long id) {
        return userService.findByIdUser(id);
    }

}

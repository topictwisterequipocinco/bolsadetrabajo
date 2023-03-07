package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.dto.response.ResponsePersonDto;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.mapper.PersonMapper;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Subscriber;
import com.utn.bolsadetrabajo.repository.PersonRepository;
import com.utn.bolsadetrabajo.service.emails.EmailGoogleService;
import com.utn.bolsadetrabajo.service.interfaces.ApplicantService;
import com.utn.bolsadetrabajo.service.interfaces.PersonService;
import com.utn.bolsadetrabajo.service.interfaces.PublisherService;
import com.utn.bolsadetrabajo.service.interfaces.UserService;
import com.utn.bolsadetrabajo.util.Errors;
import com.utn.bolsadetrabajo.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired private PersonRepository repository;
    @Autowired private EmailGoogleService emailGoogleService;
    @Autowired private PersonMapper mapper;
    @Autowired private MessageSource messageSource;
    @Autowired private UserService userService;
    @Autowired private Validator validator;
    @Autowired private PublisherService publisherService;
    @Autowired private ApplicantService applicantService;
    @Autowired private Errors errors;

    @Override
    public ResponseEntity<?> sendGetPersonByUserId(Subscriber subscriber) {
        try{
            Person person = repository.findByUserId(subscriber.getUserId());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    mapper.toResponsePerson(person, messageSource.getMessage("person.response.object.success", null,null)));
        }catch (Exception e){
            LOGGER.error("No existe la cuenta con id: " + subscriber.getUserId() + " " + e.getMessage());
            errors.logError("No existe la cuenta con id: " + subscriber.getUserId() + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.search.failed", new Object[] {subscriber.getUserId()}, null));
        }
    }

    @Override
    public ResponseEntity<?> update(Long id, PersonDTO personDTO) throws PersonException {
        return id > 0L ? updatePerson(id, personDTO) : create(personDTO);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            Person person = getPerson(id);
            LOGGER.info("la persona id es " + person.getId());
            person = mapper.deletePerson(person);
            repository.save(person);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("person.deleted.success", null,null));
        }catch (Exception e){
            LOGGER.error("No pudo ser eliminado su Perfil y Usuario con id : " + id + " " + e.getMessage());
            errors.logError("No pudo ser eliminado su Perfil y Usuario con id : " + id + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.deleted.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> getAll() {
        return allPersonList(mapper.toPersonList(repository.findAll()));
    }

    @Override
    public ResponseEntity<?> getAllApplicant(int numberPage) {
        return allPersonList(mapper.toApplicantList(applicantService.getAll()));
    }

    @Override
    public ResponseEntity<?> getAllPublisher(int numberPage) {
        return allPersonList(mapper.toPublisherList(publisherService.getAll()));
    }

    @Override
    public Person getPersonByUsername(String username) {
        Subscriber subscriber = userService.findByUsername(username);
        return repository.findByUserId(subscriber.getUserId());
    }

    private ResponseEntity<?> updatePerson(Long id, PersonDTO personDTO) {
        try{
            Person newPer = mapper.toUpdate(getPerson(id), personDTO);
            validator.validPerson(newPer);
            Person aux = repository.save(newPer);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponsePerson(aux, messageSource.getMessage("person.update.success", null,null)));
        }catch (PersonException e){
            LOGGER.error("No se ha podido modificar su perfil con el id : " + id + " " + e.getMessage());
            errors.logError("No se ha podido modificar su perfil con el id : " + id + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(messageSource.getMessage("person.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    private ResponseEntity<?> create(PersonDTO personDTO) throws PersonException {
        try{
            Person newPerson = mapper.toModel(personDTO);
            validator.validPerson(newPerson);
            Person person = repository.save(newPerson);
            emailGoogleService.createEmailPerson(person);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponsePerson(person, messageSource.getMessage("person.created.success", null,null)));
        }catch (PersonException e){
            LOGGER.error("No pudo crearse la cuenta." + e.getMessage());
            errors.logError("No pudo crearse la cuenta." + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("person.create.failed",new Object[] {e.getMessage()}, null));
        }
    }

    private ResponseEntity<?> allPersonList(List<ResponsePersonDto> lists) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(lists);
        }catch (Exception e){
            LOGGER.error("Fallo la operacion de pedido de todos las Personas.");
            errors.logError("Fallo la operacion de pedido de todos las Personas.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.all.failed",null, null));
        }
    }

    private Person getPerson(Long id) {
        return repository.findById(id).get();
    }

}

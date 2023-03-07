package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.mapper.ApplicantMapper;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Subscriber;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.ApplicantRepository;
import com.utn.bolsadetrabajo.service.emails.EmailGoogleService;
import com.utn.bolsadetrabajo.service.interfaces.ApplicantService;
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
public class ApplicantServiceImpl implements ApplicantService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicantServiceImpl.class);

    @Autowired private ApplicantRepository repository;
    @Autowired private EmailGoogleService emailGoogleService;
    @Autowired private UserService userService;
    @Autowired private ApplicantMapper applicantMapper;
    @Autowired private MessageSource messageSource;
    @Autowired private Validator validator;
    @Autowired private Errors errors;

    @Override
    public ResponseEntity<?> sendGetPersonByRequestById(Person person, Long id){
        try{
            Applicant applicant = getApplicant(person.getId());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(applicantMapper.toResponseApplicant(applicant, messageSource.getMessage("applicant.response.object.success", null,null)));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("applicant.search.failed " + e.getMessage(), new Object[] {id}, null));
            errors.logError(messageSource.getMessage("applicant.search.failed " + e.getMessage(), new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("applicant.search.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> update(Long id, PersonDTO personDTO) throws PersonException {
        return id > 0L ? updateApplicant(id, personDTO) : create(personDTO);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            Applicant applicant = getApplicant(id);
            applicant.setDeleted(true);
            applicant.getSubscriber().setState(State.DELETED);
            repository.save(applicant);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("applicant.deleted.success", null,null));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("applicant.deleted.failed " + e.getMessage(), new Object[] {id}, null));
            errors.logError(messageSource.getMessage("applicant.deleted.failed " + e.getMessage(), new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("applicant.deleted.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> getByIdUserApp(Subscriber subscriber) {
        try{
            Applicant applicant = getApplicantByUser(subscriber);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(applicantMapper.toResponseApplicant(applicant, messageSource.getMessage("applicant.response.object.success", null,null)));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("applicant.search.failed " + e.getMessage(), new Object[] {subscriber.getUserId()}, null));
            errors.logError(messageSource.getMessage("applicant.search.failed " + e.getMessage(), new Object[] {subscriber.getUserId()}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("applicant.search.failed", new Object[] {subscriber.getUserId()}, null));
        }
    }

    @Override
    public void postulateJobOffer(Applicant applicant) {
        repository.save(applicant);
    }

    @Override
    public List<Applicant> getAll() {
        return repository.findAll();
    }

    @Override
    public Applicant getApplicantByUser(Subscriber subscriber) {
        return repository.findByUserId(subscriber.getUserId());
    }

    public ResponseEntity<?> updateApplicant(Long id, PersonDTO applicantDTO) {
        try{
            Applicant newApplicant = applicantMapper.toUpdate(getApplicant(id), applicantDTO);
            validator.validApplicant(newApplicant);
            Applicant aux = repository.save(newApplicant);
            return ResponseEntity.status(HttpStatus.OK).body(applicantMapper.toResponseApplicant(aux, messageSource.getMessage("applicant.update.success", null,null)));
        }catch (PersonException e){
            LOGGER.error(messageSource.getMessage("applicant.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            errors.logError(messageSource.getMessage("applicant.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(messageSource.getMessage("applicant.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    private ResponseEntity<?> create(PersonDTO applicantDTO) throws PersonException {
        try{
            Applicant app = applicantMapper.toModel(null, applicantDTO);
            validator.validApplicant(app);
            Applicant applicant = repository.save(app);
            emailGoogleService.createEmailApplicant(applicant);
            return ResponseEntity.status(HttpStatus.CREATED).body(applicantMapper.toResponseApplicant(applicant, messageSource.getMessage("applicant.created.success", null,null)));
        }catch (PersonException e){
            LOGGER.error(messageSource.getMessage("applicant.created.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            errors.logError(messageSource.getMessage("applicant.created.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("applicant.created.failed",new Object[] {e.getMessage()}, null));
        }
    }

    private Applicant getApplicant(Long id) {
        return repository.findById(id).get();
    }

}

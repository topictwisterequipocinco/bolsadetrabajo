package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.dto.request.ForgotDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.*;
import org.springframework.http.ResponseEntity;

public interface UserService {

    Subscriber saveUser(String email, String password, Role role) throws PersonException;

    ResponseEntity<?> findById(Long id);

    Subscriber findByIdUser(Long id);

    ResponseEntity<?> getAllUsers(int page);

    ResponseEntity<?> activateAccount(String username, String hash);

    Subscriber findByUsername(String username);

    Subscriber update(Person pub, String email, String password);

    Subscriber updateApplicant(Applicant applicant, String email, String password);

    Subscriber updatePublisher(Publisher publisher, String email, String password);

    ResponseEntity<?> forgot(ForgotDTO forgotDTO);

    Subscriber findByUsernameByStateActive(String username);

    Subscriber save(Subscriber subscriber);
}

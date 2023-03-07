package com.utn.bolsadetrabajo.mapper;

import com.utn.bolsadetrabajo.dto.response.ResponseUserDto;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.Role;
import com.utn.bolsadetrabajo.model.Subscriber;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.security.authentication.AuthenticationResponse;
import com.utn.bolsadetrabajo.util.GenerateHash;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final GenerateHash generateHash;

    public UserMapper(GenerateHash generateHash) {
        this.generateHash = generateHash;
    }

    public Subscriber toModel(String email, Role role, String encode) throws PersonException {
        Long pass = generateHash.generateAleatorio();
        Subscriber subscriber = Subscriber.builder()
                .username(email)
                .password(encode)
                .role(role)
                .state(State.REVIEW)
                .verificationCode(String.valueOf(pass))
                .conected(true)
                .build();
        return subscriber;
    }

    public ResponseUserDto toUserResponseDto(Subscriber subscriber, String message) {
        ResponseUserDto us = ResponseUserDto.builder()
                .id(subscriber.getUserId())
                .username(subscriber.getUsername())
                .role(subscriber.getRole().getRole().toString())
                .message(message)
                .conected(subscriber.isConected())
                .build();
        return us;
    }

    public AuthenticationResponse responseLoginUserJason(Subscriber subscriber, String jwt) {
        AuthenticationResponse auth = new AuthenticationResponse(jwt);
        auth.setUsername(subscriber.getUsername());
        auth.setId(subscriber.getUserId());
        auth.setRole(subscriber.getRole());
        return auth;
    }

    public Subscriber update(Subscriber subscriber, String email, String encodePassword) {
        subscriber.setUsername(email);
        subscriber.setPassword(encodePassword);
        return subscriber;
    }
}

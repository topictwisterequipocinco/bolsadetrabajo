package com.utn.bolsadetrabajo.mapper;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.dto.response.ResponsePersonDto;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.Publisher;
import com.utn.bolsadetrabajo.model.Role;
import com.utn.bolsadetrabajo.model.Subscriber;
import com.utn.bolsadetrabajo.model.enums.Roles;
import com.utn.bolsadetrabajo.repository.RoleRepository;
import com.utn.bolsadetrabajo.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class PublisherMapper {

    private final RoleRepository roleRepository;
    private final UserService userService;

    @Autowired
    public PublisherMapper(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    public Publisher toModel(Publisher pub, PersonDTO dto) throws PersonException {
        Role role = roleRepository.getByRole(Roles.PUBLISHER);
        Subscriber subscriber = userService.saveUser(dto.getEmail(), dto.getPassword(), role);
        if (pub == null) pub = new Publisher();

        buildPerson(pub, dto);
        pub.setSubscriber(subscriber);
        return pub;
    }

    public Publisher toUpdate(Publisher pub, PersonDTO dto) {
        buildPerson(pub, dto);
        pub.setSubscriber(userService.updatePublisher(pub, dto.getEmail(), dto.getPassword()));
        return pub;
    }

    private void buildPerson(Publisher pub, PersonDTO dto){
        pub.setOficialName(dto.getName());
        pub.setLastName(dto.getSurname());
        pub.setIdentification(dto.getIdentification());
        pub.setPhoneNumber(dto.getPhoneNumber());
        pub.setWebPage(dto.getWebPage());
        pub.setDeleted(false);
    }

    public ResponsePersonDto toResponsePublisher(Publisher publisher, String message) {
        ResponsePersonDto dto = ResponsePersonDto.builder()
                .id(publisher.getId())
                .name(publisher.getOficialName())
                .surname(publisher.getLastName())
                .identification(publisher.getIdentification())
                .phoneNumber(publisher.getPhoneNumber())
                .email(publisher.getSubscriber().getUsername())
                .role(publisher.getSubscriber().getRole().getRole().toString())
                .webPage(publisher.getWebPage())
                .message(message)
                .uri(ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/person/{id}")
                        .buildAndExpand(publisher.getSubscriber().getUserId()).toUri())
                .build();
        return dto;
    }

}



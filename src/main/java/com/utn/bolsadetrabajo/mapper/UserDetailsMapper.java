package com.utn.bolsadetrabajo.mapper;

import com.utn.bolsadetrabajo.model.Subscriber;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserDetailsMapper {

    public static UserDetails build(Subscriber subscriber){
        return new org.springframework.security.core.userdetails.User(subscriber.getUsername(), subscriber.getPassword(), getAuthorities(subscriber));
    }

    private static Set<? extends GrantedAuthority> getAuthorities(Subscriber retrievedSubscriber) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + retrievedSubscriber.getRole().getRole().toString()));
        return authorities;
    }
}

package com.evampsaanga.usermanagement.service;

import com.evampsaanga.usermanagement.model.JwtAuthenticationResponse;
import com.evampsaanga.usermanagement.model.SaveRequest;
import com.evampsaanga.usermanagement.model.SigninRequest;
import com.evampsaanga.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;



    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        System.out.println("Request recived in signin service");

        System.out.println("Generating JWT token");
        var jwt = jwtService.generateJwtToken(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())));
        return new JwtAuthenticationResponse(jwt);


    }
}

package com.evampsaanga.usermanagement.model;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationResponse {

    private String token;

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public JwtAuthenticationResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

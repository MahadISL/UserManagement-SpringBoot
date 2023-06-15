package com.evampsaanga.usermanagement.model;

import org.springframework.stereotype.Component;

@Component
public class SigninResponse {

    private String responseCode;
    private String responseBody;
    private String token;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

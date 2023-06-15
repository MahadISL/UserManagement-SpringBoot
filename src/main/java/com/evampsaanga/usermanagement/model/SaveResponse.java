package com.evampsaanga.usermanagement.model;

import org.springframework.stereotype.Component;

@Component
public class SaveResponse {

    private String responseCode;
    private String responseBody;
    private String token;

    public SaveResponse(String responseCode, String responseBody, String token) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.token = token;
    }

    public SaveResponse() {
    }

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

package com.evampsaanga.usermanagement.model;

import org.springframework.stereotype.Component;

@Component
public class VerifiedResponse {

    private String responseCode;
    private String responseBody;
    private String temp;


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

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}

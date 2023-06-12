package com.evampsaanga.usermanagement.model;

import org.springframework.stereotype.Component;

@Component
public class Response {

    private String responseCode;
    private String responseBody;

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

    @Override
    public String toString() {
        return "Response: {" +
                "Response Code='" + responseCode + '\'' +
                ", Response Body='" + responseBody + '\'' +
                '}';
    }
}

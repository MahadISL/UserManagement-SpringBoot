package com.evampsaanga.usermanagement.service;

import com.evampsaanga.usermanagement.model.JwtAuthenticationResponse;
import com.evampsaanga.usermanagement.model.SaveRequest;
import com.evampsaanga.usermanagement.model.SigninRequest;

public interface AuthenticationService {


    JwtAuthenticationResponse signin(SigninRequest request);
}

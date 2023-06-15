package com.evampsaanga.usermanagement.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


public interface UserInfoService {

    UserDetailsService userDetailsService();
}

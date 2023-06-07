package com.evampsaanga.usermanagement.service;

import com.evampsaanga.usermanagement.model.User;
import com.evampsaanga.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void saveSignUp(User user){
        userRepository.save(user);
    }
}

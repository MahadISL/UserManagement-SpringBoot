package com.evampsaanga.usermanagement.service;

import com.evampsaanga.usermanagement.model.SaveRequest;
import com.evampsaanga.usermanagement.model.User;
import com.evampsaanga.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    User user;

    public User setUserCredentials(SaveRequest request) {
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return user;
    }

    public void saveSignUp(SaveRequest saveRequest) {
        user = setUserCredentials(saveRequest);
        userRepository.save(user);
    }

    public Boolean checkIfEmailExist(String email) {
        Boolean exist = userRepository.existsByEmail(email);
        return exist;
    }

    public Boolean checkIfPasswordsSimilar(String pass, String confirmPass) {
        if (pass.equals(confirmPass)) {
            return true;
        } else {
            return false;
        }
    }
}

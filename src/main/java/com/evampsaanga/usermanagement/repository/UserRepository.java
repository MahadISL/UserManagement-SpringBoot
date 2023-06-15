package com.evampsaanga.usermanagement.repository;

import com.evampsaanga.usermanagement.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    Boolean existsByEmail(String email);
    User findByEmail(String email);

}

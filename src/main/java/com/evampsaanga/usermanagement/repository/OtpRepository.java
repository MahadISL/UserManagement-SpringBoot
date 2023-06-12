package com.evampsaanga.usermanagement.repository;

import com.evampsaanga.usermanagement.model.Otp;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface OtpRepository extends CrudRepository<Otp, Integer> {
    Otp findByemail(String email);
//    Otp findFirstByOrderByCreationTimeDesc(LocalTime time);
    Otp findByotpValue(String otp);
    Otp findTopByOrderByIdDesc();
    Otp findByEmail(String email);
}

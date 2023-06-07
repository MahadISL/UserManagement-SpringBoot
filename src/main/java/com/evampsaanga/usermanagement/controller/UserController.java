package com.evampsaanga.usermanagement.controller;

import com.evampsaanga.usermanagement.model.Otp;
import com.evampsaanga.usermanagement.model.User;
import com.evampsaanga.usermanagement.service.EmailService;
import com.evampsaanga.usermanagement.service.OtpService;
import com.evampsaanga.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("User")
public class UserController {


    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    OtpService otpService;


    @GetMapping("/generateotp")
    ResponseEntity<String> generateOtp(@RequestParam("email") String email1) throws UnsupportedEncodingException {
        int otp = otpService.generateOTP();
        if (otp != 0) {
            emailService.sendEmail(email1,"OTP", "One-time passowrd (OTP): " + String.valueOf(otp));
            otpService.saveOtpMetaData(otp);
            return new ResponseEntity<>("SUCCESSFULLY GENERATED OTP!", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("ERROR: COULD NOT GENERATE OTP!", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/verifyotp")
    ResponseEntity<String> verifyOtp(@RequestParam("otpValue") int otpValue) throws UnsupportedEncodingException {
        int otpValue1 = otpService.encryptDecryptOtp(otpValue);
        String result = otpService.validateOtp(otpValue1);

        if(result == "Entered Otp is valid"){
            String temp = result + "\n! temp: " + String.valueOf(otpValue1) + "\n Response code: "+ HttpStatus.OK;
            return new ResponseEntity<>(temp, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUpUserGenerateOtp(@RequestBody User user){

        userService.saveSignUp(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}

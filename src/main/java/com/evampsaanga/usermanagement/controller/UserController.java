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
            String responseString = "{\n \tresponse code: 200\n \tresponse description: SUCCESSFULLY GENERATED OTP!\n}";
            return new ResponseEntity<>(responseString, HttpStatus.OK);
        }
        else {
            String responseString = "{\n \tresponse code: 202\n \tresponse description: ERROR: COULD NOT GENERATE OTP!!\n}";
            return new ResponseEntity<>(responseString, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/verifyotp")
    ResponseEntity<String> verifyOtp(@RequestParam("otpValue") int otpValue) throws UnsupportedEncodingException {
        int otpValue1 = otpService.encryptDecryptOtp(otpValue);
        String result = otpService.validateOtp(otpValue1);

        if(result == "Entered Otp is valid"){
            String responseString = "{\n \tresponse code: 200\n \tresponse description: " + result + " !\n}";
            String temp = result + "\n! temp: " + String.valueOf(otpValue1) + "\n Response code: "+ HttpStatus.OK;
            return new ResponseEntity<>(responseString, HttpStatus.OK);
        }
        else {
            String responseString = "{\n \tresponse code: 400\n \tresponse description: " + result + " \n}";
            return new ResponseEntity<>(responseString, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUpUserGenerateOtp(@RequestBody User user){

        userService.saveSignUp(user);
        String responseString = "{\n \tresponse code: 200\n \tresponse description: SUCCESSFULLY SAVED USER!\n}";
        return new ResponseEntity<>(responseString, HttpStatus.OK);
    }

}

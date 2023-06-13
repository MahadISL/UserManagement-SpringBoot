package com.evampsaanga.usermanagement.controller;

import com.evampsaanga.usermanagement.model.Response;
import com.evampsaanga.usermanagement.model.SaveRequest;
import com.evampsaanga.usermanagement.model.User;
import com.evampsaanga.usermanagement.model.VerifiedResponse;
import com.evampsaanga.usermanagement.service.EmailService;
import com.evampsaanga.usermanagement.service.OtpService;
import com.evampsaanga.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping("User")
public class UserController {

    @Autowired
    Response response;

    @Autowired
    VerifiedResponse verifyResponse;

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    OtpService otpService;


    @GetMapping("/generateotp")
    ResponseEntity<Response> generateOtp(@RequestParam("email") String email1) throws UnsupportedEncodingException {
        String otp = otpService.generateOTP(email1);
        if (otp != null && !otp.equals("attempt")) {
            emailService.sendEmail(email1,"OTP", "One-time passowrd (OTP): " + otp);
            otpService.saveOtpMetaData(otp,email1);
            response.setResponseCode(String.valueOf(HttpStatus.OK));
            response.setResponseBody("SUCCESSFULLY GENERATED OTP!");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else if ( otp!=null && otp.equals("attempt"))
        {
            response.setResponseCode(String.valueOf(HttpStatus.OK));
            response.setResponseBody("SUCCESSFULLY GENERATED OTP (new Attempt)!");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setResponseCode(String.valueOf(HttpStatus.EXPECTATION_FAILED));
            response.setResponseBody("ERROR COULD NOT GENERATE OTP!");
            String responseString = "{\n \tresponse code: 202\n \tresponse description: ERROR: COULD NOT GENERATE OTP!!\n}";
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping("/verifyotp")
    ResponseEntity<?> verifyOtp(@RequestBody String otpValue) throws UnsupportedEncodingException {

        String otpValue1 = otpService.encryptThisString(otpValue);
        String result = otpService.validateOtp(otpValue);

        if (result.equals("Entered Otp is valid")) {

            verifyResponse.setResponseCode(String.valueOf(HttpStatus.OK));
            verifyResponse.setResponseBody(result);
            verifyResponse.setTemp(otpValue1);

            return new ResponseEntity<VerifiedResponse>(verifyResponse, HttpStatus.OK);
        } else {

            response.setResponseCode(String.valueOf(HttpStatus.BAD_REQUEST));
            response.setResponseBody(result);
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> signUpUserGenerateOtp(@RequestBody SaveRequest saveRequest) throws UnsupportedEncodingException {

        int otpValue = otpService.validateOtp(saveRequest.getEmail(), saveRequest.getTemp());

        if(otpValue >0){
            boolean emailCheck = userService.checkIfEmailExist(saveRequest.getEmail());
            boolean passwordCheck = userService.checkIfPasswordsSimilar(saveRequest.getPassword(),saveRequest.getConfirmPassword());
            if (passwordCheck) {
                if (!emailCheck) {
                    userService.saveSignUp(saveRequest);
                    response.setResponseCode(String.valueOf(HttpStatus.OK));
                    response.setResponseBody("SUCCESSFULLY SAVED USER!");

                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.setResponseCode(String.valueOf(HttpStatus.BAD_REQUEST));
                    response.setResponseBody("ERROR EMAIL ALREADY EXISTS!");

                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
            else {
                response.setResponseCode(String.valueOf(HttpStatus.BAD_REQUEST));
                response.setResponseBody("ERROR PASSWORDS DO NOT MATCH!");

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        else {

            response.setResponseCode(String.valueOf(HttpStatus.BAD_REQUEST));
            response.setResponseBody("ERROR EMAIL NOT VERIFIED!");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

}

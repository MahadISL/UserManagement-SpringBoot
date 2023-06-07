package com.evampsaanga.usermanagement.service;

import com.evampsaanga.usermanagement.model.Otp;
import com.evampsaanga.usermanagement.repository.OtpRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    OtpRepository otpRepository;


    public int generateOTP(){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return otp;
    }

    public void saveOtpMetaData(int otp) throws UnsupportedEncodingException {
        // Date and time of OTP creation
        LocalDate otpGenerationDate = LocalDate.now();
        LocalTime otpGenerationTime = LocalTime.now(ZoneId.of("Asia/Karachi"));

        int encryptedOtp =  encryptDecryptOtp(otp);

        // Save Otp data in DB
        Otp otpObject2 = new Otp();
        otpObject2.setOtpValue(encryptedOtp);
        otpObject2.setCreationDate(otpGenerationDate);
        otpObject2.setCreationTime(otpGenerationTime);

        saveOtp(otpObject2);
    }

    public void saveOtp(Otp otp){
        otpRepository.save(otp);
    }

    public String validateOtp(int inputOtpVal){

        final String SUCCESS = "Entered Otp is valid";
        final String FAIL = "Entered Otp is NOT valid. Please Retry!";

        // Input OTP valid
        if (inputOtpVal >= 0){

            Optional<Otp> otpObject1 = otpRepository.findById(inputOtpVal);
            // The Otp Object is not null
            if (!otpObject1.isEmpty()){

                int originalOtpVal = otpObject1.get().getOtpValue();

                // If similar OTP found in DB
                if(inputOtpVal == originalOtpVal){

                    // The creation and expiry date of original OTP
                    LocalDate creationDate = otpObject1.get().getCreationDate();
                    LocalDate expiryDate = creationDate.plusDays(1);

                    LocalDate localDate = LocalDate.now();

                    // Compare if Otp Date valid
                    if (expiryDate.compareTo(localDate)>0){

                        // The creation and expiry time of original OTP
                        LocalTime creationTime = otpObject1.get().getCreationTime();
                        LocalTime expiryTime = creationTime.plusMinutes(otpObject1.get().getExpireMins());

                        // Time zone set to Pakistan
                        LocalTime localTime = LocalTime.now(ZoneId.of("Asia/Karachi"));

                        // Compare if Otp Time valid
                        if (expiryTime.compareTo(localTime) > 0){
//                            otpRepository.deleteById(originalOtpVal);
                            return SUCCESS;
                        }
                        else {
                            return FAIL;
                        }
                    }
                    else {
                        return FAIL;
                    }
                }
                else{
                    return FAIL;
                }
            }
            else {
                return FAIL;
            }

        }
        else {
            return FAIL;
        }
    }

    public int encryptDecryptOtp(int otp) throws UnsupportedEncodingException {

        int reversedOtp = 0;
        System.out.println("Original Number: " + otp);

        // run loop until num becomes 0
        while(otp != 0) {

            // get last digit from num
            int digit = otp % 10;
            reversedOtp = reversedOtp * 10 + digit;

            // remove the last digit from num
            otp /= 10;
        }

        System.out.println("Reversed Number: " + reversedOtp);
        return reversedOtp;
    }

}

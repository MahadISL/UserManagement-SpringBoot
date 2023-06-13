package com.evampsaanga.usermanagement.service;

import com.evampsaanga.usermanagement.model.Otp;
import com.evampsaanga.usermanagement.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class OtpService {

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    Otp otp;

    @Autowired
    EmailService emailService;


    public String generateOTP(String email1){
        Random random = new Random();
        // initializing
        int otp1 = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otp1);
        Otp otpObject1 = otpRepository.findByEmail(email1);
        if(otpObject1 != null) {
            int id = otpObject1.getId();
            int attempts = otpObject1.getAttempts();
            String otpVal = otpObject1.getOtpValue();
            LocalTime ltime = otpObject1.getCreationTime();
            String email2 = otpObject1.getEmail();
            int expT = otpObject1.getExpireMins();
            Boolean verify = otpObject1.getVerified();
            Boolean attemptsAllowed = checkGenerationAttempts(attempts,ltime,expT);
            if (attemptsAllowed) {
                LocalTime newLocalTime = LocalTime.now();
                LocalDate newDateTIme = LocalDate.now();
                updateOtpAttempts(id, attempts, otp, newDateTIme, newLocalTime, email2, verify);
                emailService.sendEmail(email1,"OTP", "One-time passowrd (OTP): " + otp);
                return "attempt";
            }
            else {
                return null;
            }
        }

        return otp;
    }

    public Boolean checkGenerationAttempts(int attempt, LocalTime createdT, int expirT){
        if (attempt > 2)
        {
            LocalTime fourthAttemptT = LocalTime.now();
            LocalTime expiryTime = createdT.plusMinutes(expirT);
            if(expiryTime.compareTo(fourthAttemptT)>0){
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return true;
        }
    }


    public void updateVerifiedOtp(int Id, String OtpVal, LocalDate creationD, LocalTime creationT, String email, Boolean verify){
        otp.setId(Id);
        otp.setOtpValue(OtpVal);
        otp.setCreationDate(creationD);
        otp.setCreationTime(creationT);
        otp.setEmail(email);
        otp.setVerified(verify);
        otpRepository.save(otp);
    }

    public void updateOtpAttempts(int Id, int attempts,
                                  String otpVal, LocalDate creationD,
                                  LocalTime creationT, String email1, Boolean verify){
        if(attempts>2) {
            attempts = 0;

        }
        else {
            attempts += 1;
        }
        otp.setId(Id);
        otp.setAttempts(attempts);
        otp.setOtpValue(otpVal);
        otp.setCreationDate(creationD);
        otp.setCreationTime(creationT);
        otp.setEmail(email1);
        otp.setVerified(verify);
        otpRepository.save(otp);
    }

    public void saveOtpMetaData(String otp, String email) throws UnsupportedEncodingException {
        // Date and time of OTP creation
        LocalDate otpGenerationDate = LocalDate.now();
        LocalTime otpGenerationTime = LocalTime.now(ZoneId.of("Asia/Karachi"));


        // Save Otp data in DB
        Otp otpObject2 = new Otp();
        otpObject2.setOtpValue(otp);
        otpObject2.setCreationDate(otpGenerationDate);
        otpObject2.setCreationTime(otpGenerationTime);
        otpObject2.setEmail(email);
        saveOtp(otpObject2);
    }

    public void saveOtp(Otp otp){
        otpRepository.save(otp);
    }

    public String validateOtp(String inputOtpVal){

        final String SUCCESS = "Entered Otp is valid";
        final String FAIL = "Entered Otp is NOT valid. Please Retry!";
        System.out.println(inputOtpVal);
        Otp otpObject2 = otpRepository.findTopByOrderByIdDesc();
        Otp otpObject1 = otpRepository.findByOtpValue(inputOtpVal);
        System.out.println(otpObject2);
        if (otpObject1 != null) {
            int originalId = otpObject1.getId();
            String originalOtpVal = otpObject1.getOtpValue();
            System.out.println(originalOtpVal);
            LocalDate creationDate = otpObject1.getCreationDate();
            LocalTime creationTime = otpObject1.getCreationTime();
            String email = otpObject1.getEmail();

            // Input OTP valid
            if (inputOtpVal != null) {
                System.out.println("inputOtpVal not Null");
                // The Otp Object is not null
                if (otpObject1 != null) {
                    System.out.println("object not null");
                    // If similar OTP found in DB
                    // Alternate method contains()
                    if (inputOtpVal.trim().equals(originalOtpVal.trim())) {
                        System.out.println("inputOtp = originalotp");
                        // The creation and expiry date of original OTP
                        LocalDate expiryDate = creationDate.plusDays(1);

                        LocalDate localDate = LocalDate.now();

                        // Compare if Otp Date valid
                        if (expiryDate.compareTo(localDate) > 0) {
                            System.out.println("Otp date valid");
                            // The creation and expiry time of original OTP
                            LocalTime expiryTime = creationTime.plusMinutes(otpObject1.getExpireMins());

                            // Time zone set to Pakistan
                            LocalTime localTime = LocalTime.now(ZoneId.of("Asia/Karachi"));

                            // Compare if Otp Time valid
                            if (expiryTime.compareTo(localTime) > 0) {
                                System.out.println("Otp time valid");
                                updateVerifiedOtp(originalId, originalOtpVal, creationDate, creationTime, email, true);

                                return SUCCESS;
                            } else {
                                updateVerifiedOtp(originalId, originalOtpVal, creationDate, creationTime, email, false);

                                return FAIL;
                            }
                        } else {
                            updateVerifiedOtp(originalId, originalOtpVal, creationDate, creationTime, email, false);

                            return FAIL;
                        }
                    } else {
                        updateVerifiedOtp(originalId, originalOtpVal, creationDate, creationTime, email, false);

                        return FAIL;
                    }
                } else {
                    updateVerifiedOtp(originalId, originalOtpVal, creationDate, creationTime, email, false);

                    return FAIL;
                }

            } else {
                updateVerifiedOtp(originalId, originalOtpVal, creationDate, creationTime, email, false);

                return FAIL;
            }
        }

        return FAIL;
    }

    public int validateOtp(String email, String temp) throws UnsupportedEncodingException {

        final String SUCCESS = "Otp verified for " + email;
        final String FAIL = "Otp not verified for " + email;


        Otp otpObject2 = otpRepository.findTopByOrderByIdDesc();
        Otp otpObject1 = otpRepository.findByEmail(email);
        System.out.println(otpObject1);
        String originalOtp = otpObject1.getOtpValue();
        String encryptedOriginalOtp = encryptThisString(originalOtp);
        System.out.println(encryptedOriginalOtp);
        // Alternate method contains()
        if (encryptedOriginalOtp.trim().equals(temp.trim())) {
            System.out.println("sameotp");
            Boolean verify = otpObject1.getVerified();
            if (verify) {
                return 1;
            } else {
                return 0;
            }
        }
        else {
            return 0;
        }

    }



    public String encryptThisString(String input)
    {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
         catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}

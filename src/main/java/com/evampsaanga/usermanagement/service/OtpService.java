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

    static int generationAttempts = 0;
    static LocalTime firstAttemptTime;
    static LocalTime fourthAttemptTime;
    static final int expireTime = 30;
    static String email;
    static int badValidationAttempts = 0;

    static Map<String, String> sha1Map = new HashMap<>();

    public String generateOTP(String email1){
        Boolean generationPossible;
        Random random = new Random();
        // initializing
        int otp1 = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otp1);
        Otp otpObject1 = otpRepository.findByEmail(email1);
        if(otpObject1 != null) {
            int id = otpObject1.getId();
            int attempts = otpObject1.getAttempts();
            String otpVal = otpObject1.getOtpValue();
            LocalDate ldate = otpObject1.getCreationDate();
            LocalTime ltime = otpObject1.getCreationTime();
            String email2 = otpObject1.getEmail();
            int expT = otpObject1.getExpireMins();
            Boolean verify = otpObject1.getVerified();
            Boolean attemptsAllowed = checkGenerationAttempts(attempts,ltime,expT);
            if (attemptsAllowed) {
                LocalTime newLocalTime = LocalTime.now();
                LocalDate newDateTIme = LocalDate.now();
                updateOtpAttempts(id, attempts, otpVal, newDateTIme, newLocalTime, email2, verify);
                return "attempt";
            }
            else {
                //updateOtpAttempts(id,attempts,otpVal,ldate,ltime,email2,verify, attemptsAllowed);
                return null;
            }
        }
        generationAttempts += 1;
        if (generationAttempts == 1) {
            firstAttemptTime = LocalTime.now();
            email = email1;
        }
        if (generationAttempts > 3) {
            if (email.equals(email1)) {
                generationPossible = checkAttempts();
                if (generationPossible) {
                    return otp;
                } else {
                    return null;
                }
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

    public Boolean checkAttempts(){

        fourthAttemptTime = LocalTime.now();
        LocalTime expiryTime = firstAttemptTime.plusSeconds(expireTime);
        if(expiryTime.compareTo(fourthAttemptTime)>0){
            return false;
        }
        else{
            generationAttempts = 0;
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

        //int encryptedOtp =  encryptDecryptOtp(otp);
        //String encryptedOtp = encryptThisString(otp);

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

//        Optional<Otp> otpObject2 = otpRepository.findById(inputOtpVal);
        Otp otpObject1 = otpRepository.findByotpValue(inputOtpVal);
        if (otpObject1 != null) {
            int originalId = otpObject1.getId();
            String originalOtpVal = otpObject1.getOtpValue();
            LocalDate creationDate = otpObject1.getCreationDate();
            LocalTime creationTime = otpObject1.getCreationTime();

            // Input OTP valid
            if (inputOtpVal != null) {

                // The Otp Object is not null
                if (otpObject1 != null) {

                    // If similar OTP found in DB
                    if (inputOtpVal.equals(originalOtpVal)) {

                        // The creation and expiry date of original OTP
                        LocalDate expiryDate = creationDate.plusDays(1);

                        LocalDate localDate = LocalDate.now();

                        // Compare if Otp Date valid
                        if (expiryDate.compareTo(localDate) > 0) {

                            // The creation and expiry time of original OTP
                            LocalTime expiryTime = creationTime.plusMinutes(otpObject1.getExpireMins());

                            // Time zone set to Pakistan
                            LocalTime localTime = LocalTime.now(ZoneId.of("Asia/Karachi"));

                            // Compare if Otp Time valid
                            if (expiryTime.compareTo(localTime) > 0) {
//                            otpRepository.deleteById(originalOtpVal);
                                updateVerifiedOtp(originalId, originalOtpVal, creationDate, creationTime, email, true);
                                badValidationAttempts = 0;
                                return SUCCESS;
                            } else {
                                updateVerifiedOtp(originalId, originalOtpVal, creationDate, creationTime, email, false);
                                badValidationAttempts += 1;
                                return FAIL;
                            }
                        } else {
                            updateVerifiedOtp(originalId, originalOtpVal, creationDate, creationTime, email, false);
                            badValidationAttempts += 1;
                            return FAIL;
                        }
                    } else {
                        updateVerifiedOtp(originalId, originalOtpVal, creationDate, creationTime, email, false);
                        badValidationAttempts += 1;
                        return FAIL;
                    }
                } else {
                    updateVerifiedOtp(originalId, originalOtpVal, creationDate, creationTime, email, false);
                    badValidationAttempts += 1;
                    return FAIL;
                }

            } else {
                updateVerifiedOtp(originalId, originalOtpVal, creationDate, creationTime, email, false);
                badValidationAttempts += 1;
                return FAIL;
            }
        }
        badValidationAttempts += 1;
        return FAIL;
    }

    public int validateOtp(String email, String temp) throws UnsupportedEncodingException {

//        int inputOtpVal = encryptDecryptOtp(inputOtpVal1);
        final String SUCCESS = "Otp verified for " + email;
        final String FAIL = "Otp not verified for " + email;

       // List<Otp> otpList = otpRepository.findByemail(email);

        Otp otpObject1 = otpRepository.findTopByOrderByIdDesc();
        String originalOtp = otpObject1.getOtpValue();
        String encryptedOriginalOtp = encryptThisString(originalOtp);
        if (encryptedOriginalOtp.equals(temp)) {
            //temp1 = encryptThisString(temp)
            //Otp otpObject1 = otpRepository.findByotpValue(temp);

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

//        for(Otp otpObject: otpList){
//            int otpValue1 = otpObject.getOtpValue();
//            Boolean verify = otpObject.getVerified();
//            if(verify)
//            {
//                return 1;
//            }
//            else {
//                return 0;
//            }
////            //String rs = validateOtp(otpValue1);
////            System.out.println(rs);
////            if (rs.equals("Entered Otp is valid")){
////                return otpObject.getOtpValue();
////            }
//        }

//        Otp otpObject = otpRepository.findFirstByOrderByCreationTimeDesc(time);
//        if(otpObject.getVerified()){
//            return 1;
//        }
//        else {
//            return -1;
//        }

    }

    public Boolean checkBadValidateAttempts(){

        if (badValidationAttempts > 2) {
            return false;
        } else {
            return true;
        }
    }

//    public Boolean checkBadValidationExpiryTime(){
//        if (timeAfterBadValidationAttempts != LocalTime.parse("14:54:44.21")) {
//            // time expired for waiting after bad validation attempts
//            if (expiryTime.compareTo(timeAfterBadValidationAttempts) < 0) {
//                badValidationAttempts = 0;
//                return true;
//            }
//            else
//            {
//                return false;
//            }
//        }
//        else {
//            return false;
//        }
//    }
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

package com.evampsaanga.usermanagement.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
@Entity
@Table(name = "Otptable")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private int id;

    @Column(name = "Attempts")
    private int attempts;

    @Column(name="Otp_Value")
    private String otpValue;

    @Column(name = "Expire_Mins")
    private final int expireMins = 2;

    @Column(name = "Creation_Date")
    private LocalDate creationDate;

    @Column(name = "Creation_Time")
    private LocalTime creationTime;

    @Column(name = "Email")
    private String email;

    @Column(name = "Verified")
    private Boolean verified = false;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOtpValue() {
        return otpValue;
    }

    public void setOtpValue(String otpValue) {
        this.otpValue = otpValue;
    }

    public int getExpireMins() {
        return expireMins;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "Otp{" +
                "id=" + id +
                ", otpValue=" + otpValue +
                ", expireMins=" + expireMins +
                ", creationDate=" + creationDate +
                ", creationTime=" + creationTime +
                ", email='" + email + '\'' +
                ", verified=" + verified +
                '}';
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}

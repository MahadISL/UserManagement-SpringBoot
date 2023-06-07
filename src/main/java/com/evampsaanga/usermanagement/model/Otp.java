package com.evampsaanga.usermanagement.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Otptable")
public class Otp {

    @Id
    private int otpValue;
    private final int expireMins = 2;
    private LocalDate creationDate;
    private LocalTime creationTime;


    public int getOtpValue() {
        return otpValue;
    }

    public void setOtpValue(int otpValue) {
        this.otpValue = otpValue;
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

    public int getExpireMins() {
        return expireMins;
    }

    @Override
    public String toString() {
        return "Otp{" +
                "otpValue=" + otpValue +
                ", expireMins=" + expireMins +
                ", creationDate=" + creationDate +
                ", creationTime=" + creationTime +
                '}';
    }
}

package com.nlu.cdweb.BookStore.services;

public interface IOTPService {
    public String sendOtp(String email);
    public boolean validateOtp(String token, String otp);
}

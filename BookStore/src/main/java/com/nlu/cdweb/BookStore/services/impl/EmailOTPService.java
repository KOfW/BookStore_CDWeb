package com.nlu.cdweb.BookStore.services.impl;

import com.nlu.cdweb.BookStore.config.JwtGenerator;
import com.nlu.cdweb.BookStore.services.IOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailOTPService implements IOTPService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private JwtGenerator jwtGenerator;

    public EmailOTPService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String sendOtp(String email) {
        try{
            String otp = generateOtp();

//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(email);
//            message.setSubject("Your OTP Code");
//            message.setText("Your OTP is: " + otp + "\nIt is valid for 5 minutes.");
//            mailSender.send(message);

            return otp;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bị lỗi khi generate OTP");
        }
    }

    @Override
    public boolean validateOtp(String token, String otp) {
        return jwtGenerator.validateOtp(token, otp);
    }

    private String generateOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000)); // 6-digit OTP
    }
}

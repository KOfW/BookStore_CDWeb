package com.nlu.cdweb.BookStore.controller;

import com.nlu.cdweb.BookStore.config.JwtGenerator;
import com.nlu.cdweb.BookStore.dto.ApiResponse;
import com.nlu.cdweb.BookStore.dto.request.Email;
import com.nlu.cdweb.BookStore.dto.request.VerifyRequest;
import com.nlu.cdweb.BookStore.services.IOTPService;
import com.nlu.cdweb.BookStore.services.IUserService;
import com.nlu.cdweb.BookStore.utils.State;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/otp")
@RequiredArgsConstructor
public class OTPController {

    @Autowired
    private final IOTPService otpService;
    @Autowired
    private final IUserService userService;
    @Autowired
    private JwtGenerator jwtGenerator;

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse> sendOTP(@RequestBody Email email) {
        try {
            String sendOtp = otpService.sendOtp(email.getEmail());
            String token = jwtGenerator.generateTokenOTP(email.getEmail(), sendOtp);

            Map<String, String> response = new HashMap<>();
            response.put("token", token); // gửi về client để xác thực lần sau

            if (sendOtp != null) {
                return ResponseEntity.ok(new ApiResponse("success", "OTP sent successfully: "+sendOtp, response));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("failed", "Failed to send OTP", false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Internal error while sending OTP", false));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyRequest request) {
        try {
            boolean valid = otpService.validateOtp(request.getToken(), request.getOtp());
            if (valid) {
                String email = jwtGenerator.getEmailFromJwtOtp(request.getToken());
                userService.userActive(email, State.ACTIVE);
                return ResponseEntity.ok("✅ OTP hợp lệ!");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ OTP không hợp lệ!");
            }
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Token không hợp lệ hoặc đã hết hạn.");
        }
    }
}

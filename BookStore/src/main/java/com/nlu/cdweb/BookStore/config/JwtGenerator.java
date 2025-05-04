package com.nlu.cdweb.BookStore.config;

import com.nlu.cdweb.BookStore.entity.RoleEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtGenerator {
    // Khoa dunbg de ky jwt
    private final Key key;

    public JwtGenerator() {

        this.key = Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }
    /*
    Lấy username từ đối tượng Authentication
    Ghi nhận thời gian tạo
    Đặt thời gian hết hạn (dựa trên JWT_EXPIRATION)
    Ký token với key và thuật toán HS512
    */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return token;
    }
    /*Tạo token từ email, otp, date và có key*/
    public String generateTokenOTP(String email, String otp){
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("otp", otp);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512) // 🔄 Sửa ở đây
                .compact();
    }


    public Claims extractClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmailFromJwtOtp(String token){
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    /*Lấy thông tin người dùng ừ token cụ thể*/
    public String getUsernameFromJwt(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateOtp(String token, String inputOtp) {
        Claims claims = extractClaims(token);
        String otpInToken = claims.get("otp", String.class);
        return otpInToken.equals(inputOtp) && claims.getExpiration().after(new Date());
    }
    /*
    Token có đúng định dạng không?
    Token có đúng chữ ký không?
    Token có hết hạn không?
    */
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("JWT token is not valid: " + e.getMessage());
        }
    }
}
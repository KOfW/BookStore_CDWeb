package com.nlu.cdweb.BookStore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String tokenType="Bearer ";

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}

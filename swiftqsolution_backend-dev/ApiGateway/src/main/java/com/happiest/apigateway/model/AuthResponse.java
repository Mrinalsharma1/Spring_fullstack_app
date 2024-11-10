package com.happiest.apigateway.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private long id;
    private String username;
    private String profilename;
    private String role;

    public long getUserId() {
        return 0;
    }
}


package org.example.medinsurance.responses;

import lombok.Getter;
import lombok.Setter;
import org.example.medinsurance.enums.Role;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private long expiresIn;

    public LoginResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}

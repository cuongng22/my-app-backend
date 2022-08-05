package com.example.myweb.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.UniqueConstraint;
@Data
@AllArgsConstructor
public class LoginRequest {
    private String username;

    private String password;
}

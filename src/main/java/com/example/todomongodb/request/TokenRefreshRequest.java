package com.example.todomongodb.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class TokenRefreshRequest {
    @NotBlank
    @Getter
    @Setter
    private String refreshToken;
}

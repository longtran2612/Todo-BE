package com.example.todomongodb.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String phoneNumber;
    private String newPassword;
}

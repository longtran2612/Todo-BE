package com.example.todomongodb.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


public interface AccountService extends UserDetailsService {
    void setRefreshToken(String accountId, String refreshToken);
}
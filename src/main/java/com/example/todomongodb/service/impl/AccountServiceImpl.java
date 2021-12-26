package com.example.todomongodb.service.impl;

import com.example.todomongodb.entity.Account;
import com.example.todomongodb.enumEntity.RoleType;
import com.example.todomongodb.repository.AccountRepository;

import com.example.todomongodb.request.SignupRequest;
import com.example.todomongodb.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AccountServiceImpl implements AccountService, UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder encoder;

    private static final Logger logger = Logger.getLogger(AccountService.class.getName());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Không tồn tại tài khoản " + username));
        return Account.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .roles(RoleType.ROLE_USER.toString())
                .build();
    }

    public boolean signup(SignupRequest signupRequest){
        if (accountRepository.existsByUsername(signupRequest.getUsername()))
            return false;
        Account account = Account.builder()
                .username(signupRequest.getUsername())
                .password(encoder.encode(signupRequest.getPassword()))
                .roles(RoleType.ROLE_USER.toString())
                .build();
        accountRepository.save(account);
        return true;
    }
    /**
     *  set refresh token for account
     * @param username
     * @param refreshToken
     */
    @Override
    public void setRefreshToken(String username, String refreshToken){
        Account account = accountRepository.findByUsername(username).get();
        account.setRefreshToken(refreshToken);
        accountRepository.save(account);
    }

}

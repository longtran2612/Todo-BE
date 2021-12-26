package com.example.todomongodb.repository;
import com.example.todomongodb.entity.Account;
import com.example.todomongodb.service.AccountService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String>  {
    Optional<Account> findByUsername(String username);
    Boolean existsByUsername(String username);
}

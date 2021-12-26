package com.example.todomongodb.repository;

import com.example.todomongodb.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    @Override
    boolean existsById(String s);
}

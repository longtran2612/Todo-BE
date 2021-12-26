package com.example.todomongodb.service;

import com.example.todomongodb.entity.Task;
import com.example.todomongodb.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    User save(User user);
    void delete(String id);
    User get(String id);
    List<User> getAll();
    User update(User user);

}

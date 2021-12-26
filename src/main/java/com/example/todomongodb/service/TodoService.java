package com.example.todomongodb.service;

import com.example.todomongodb.entity.Task;
import com.example.todomongodb.entity.Todo;
import com.example.todomongodb.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TodoService {
    Todo save(Todo todo);

    void delete(String id);

    Todo get(String id);

    List<Todo> getAll();
    Todo update(Todo todo);
}


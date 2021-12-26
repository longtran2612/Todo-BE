package com.example.todomongodb.service.impl;

import com.example.todomongodb.entity.Task;
import com.example.todomongodb.entity.Todo;
import com.example.todomongodb.repository.TodoRepository;
import com.example.todomongodb.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoImpl implements TodoService {
    @Autowired
    private TodoRepository todoRepository;
    @Override
    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public void delete(String id) {
        todoRepository.deleteById(id);

    }

    @Override
    public Todo get(String id) {
        return todoRepository.findById(id).get();
    }

    @Override
    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    @Override
    public Todo update(Todo todo) {
        return todoRepository.save(todo);
    }
}

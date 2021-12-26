package com.example.todomongodb.controller;

import com.example.todomongodb.entity.Task;
import com.example.todomongodb.entity.Todo;
import com.example.todomongodb.service.TaskService;
import com.example.todomongodb.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @PostMapping
    public Todo save(@RequestBody Todo todo){
        return todoService.save(todo);
    }

    @GetMapping("/{id}")
    public Todo findById(@PathVariable(value = "id") String id){
        return todoService.get(id);
    }

    @GetMapping
    public List<Todo> findAll(){
        return todoService.getAll();
    }

    @PutMapping("/{id}")
    public Todo update(
                       @RequestBody Todo todo){
        return todoService.update(todo);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") String id){
        todoService.delete(id);
    }
}
package com.example.todomongodb.controller;

import com.example.todomongodb.entity.Task;
import com.example.todomongodb.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping
    public Task save(@RequestBody Task task){
        return taskService.save(task);
    }

    @GetMapping("/{id}")
    public Task findById(@PathVariable(value = "id") String id){
        return taskService.get(id);
    }

    @GetMapping
    public List<Task> findAll(){
        return taskService.getAll();
    }

    @PutMapping("/{id}")
    public Task update(@RequestBody Task task){
        return taskService.update( task);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") String id){
        taskService.delete(id);
    }
}
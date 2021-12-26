package com.example.todomongodb.service;

import com.example.todomongodb.entity.Task;
import com.example.todomongodb.entity.User;
import com.example.todomongodb.repository.TaskRepository;

import org.springframework.stereotype.Service;

import java.util.List;


public interface TaskService {
     Task save(Task task);
     void delete(String id);
     Task get(String id);
     List<Task> getAll();
     Task update(Task task);


}

package com.example.todomongodb.repository;

import com.example.todomongodb.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task,String> {

}

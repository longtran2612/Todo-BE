package com.example.todomongodb.service.impl;

import com.example.todomongodb.entity.Task;
import com.example.todomongodb.entity.User;
import com.example.todomongodb.repository.UserRepository;
import com.example.todomongodb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);

    }

    @Override
    public User get(String id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void partialUpdate(String userId, String fieldName, Object fieldValue) {
        mongoTemplate.findAndModify(BasicQuery.query(Criteria.where("id").is(userId)),
                BasicUpdate.update(fieldName, fieldValue), FindAndModifyOptions.none(), User.class);
    }
}

package com.example.todomongodb.controller;

import com.example.todomongodb.entity.Account;
import com.example.todomongodb.entity.User;
import com.example.todomongodb.repository.UserRepository;
import com.example.todomongodb.request.UpdateUserRequest;
import com.example.todomongodb.response.MessageResponse;
import com.example.todomongodb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @GetMapping("/phone={userPhone}")
    @PreAuthorize("hasRole('USER')")
    public User findUserByPhone(@PathVariable String userPhone){
        return userRepository.findDistinctByPhone(userPhone).orElseThrow(() -> new UsernameNotFoundException("Không tồn tại người dùng!!"));
    }

    /**
     * get user by id
     * @param userId
     * @return user
     */
    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public User findUserById(@PathVariable String userId){
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Không tồn tại người dùng!!"));
    }
    /**
     * partial update user info
     * @param account
     * @param user
     * @return
     * @throws Exception
     */
    @PatchMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public User partialUpdateUser(@AuthenticationPrincipal Account account, @RequestBody User user) throws Exception{
        User userFromData = userRepository.findDistinctByPhone(account.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Người dùng không tồn tại"));
        for (Field field: User.class.getDeclaredFields()){
            String fieldName = field.getName();
            if (fieldName.equals("id")){
                continue;
            }
            Method getter = User.class.getDeclaredMethod("get"+ StringUtils.capitalize(fieldName));
            Object fieldValue = getter.invoke(user);

            if (Objects.nonNull(fieldValue)){
                userService.partialUpdate(userFromData.getId(), fieldName, fieldValue);
            }
        }
        return userRepository.findById(userFromData.getId()).get();
    }

    @PutMapping("/updateUser/{userPhone}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUser(@PathVariable String userPhone, @RequestBody UpdateUserRequest updateUser){
        //ktra co ton tai nguoi dung?
        if(!userRepository.findDistinctByPhone(userPhone).isPresent())
            return ResponseEntity.badRequest().body(new MessageResponse("Người dùng không tồn tại!"));
        User user=userRepository.findDistinctByPhone(userPhone).get();

        user.setAddress(updateUser.getAddress());
        user.setEmail(updateUser.getEmail());
        user.setImage(updateUser.getImage());
        user.setName(updateUser.getName());
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/deleteUser/{userPhone}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteUser(@PathVariable String userPhone){
        //ktra co ton tai nguoi dung?
        if(!userRepository.findDistinctByPhone(userPhone).isPresent())
            return ResponseEntity.badRequest().body(new MessageResponse("Người dùng không tồn tại!"));
        userRepository.delete(userRepository.findDistinctByPhone(userPhone).get());
        return ResponseEntity.ok(new MessageResponse("Xóa người dùng thành công"));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<User> findAllUser(){
        return userRepository.findAll();
    }

    /**
     * đổi ảnh đại diện cá nhân
     * @param account
     * @param multipartFile
     * @return
     */
}


package org.demo.UserService.Controller;

import Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.demo.UserService.Request.CreateUserRequest;
import org.demo.UserService.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public User create(@RequestBody @Valid CreateUserRequest createUserRequest) throws JsonProcessingException {
        return userService.create(createUserRequest);
    }

    @GetMapping("/userDetails")
    public User find(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        User user=(User) authentication.getPrincipal();
        return (User) userService.loadUserByUsername(user.getContact());

    }

    @GetMapping("/getUser")
    public User findUser(@RequestParam("contact") String contact){
        return (User) userService.loadUserByUsername(contact);
    }

}

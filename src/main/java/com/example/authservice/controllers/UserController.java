package com.example.authservice.controllers;

import com.example.authservice.dtos.LoginRequestDto;
import com.example.authservice.dtos.SignUpRequestDto;
import com.example.authservice.dtos.UserDto;
import com.example.authservice.exceptions.ExpiredTokenException;
import com.example.authservice.exceptions.InvalidTokenException;
import com.example.authservice.exceptions.PasswordMismatchException;
import com.example.authservice.exceptions.UnknownUserException;
import com.example.authservice.models.User;
import com.example.authservice.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/sample")
    public String sampleAPI(){
        return "Hello World";
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody SignUpRequestDto requestDto) {
//        System.out.println("Registering user with email: " + requestDto.getEmail());

        User user = userService.signUp(
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword()
        );

        return UserDto.from(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto requestDto) throws UnknownUserException,PasswordMismatchException {
        return userService.login(requestDto.getEmail(), requestDto.getPassword());
    }

    @GetMapping("/validate/{tokenValue}")
    public UserDto validateToken(@PathVariable("tokenValue") String tokenValue)  throws InvalidTokenException, ExpiredTokenException {
        System.out.println("Validating token!");
        User user = userService.validateToken(tokenValue);

        return UserDto.from(user);
    }


}

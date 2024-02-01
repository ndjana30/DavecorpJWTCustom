package com.jwt.integration.restController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.integration.exception.domain.ExceptionHandling;

// import com.jwt.integration.domain.User;

@RestController
@RequestMapping(value = "/user")
public class UserResource extends ExceptionHandling{
    
    @RequestMapping("/home")
    public String showUser()
    {
        return "application works";
    }
}

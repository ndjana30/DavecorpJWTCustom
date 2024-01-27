package com.jwt.integration.restController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.integration.domain.User;

@RestController
@RequestMapping(value = "/user")
public class UserResource {
    
    @RequestMapping("/show")
    public String showUser()
    {
        return "application works";
    }
}

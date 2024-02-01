package com.jwt.integration.exception.domain;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message)
    {
    super(message);
    }
}

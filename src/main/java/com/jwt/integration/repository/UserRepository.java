package com.jwt.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.integration.domain.User;

public interface UserRepository extends JpaRepository<User,Long>
{
    User findUserByUsername(String username);
    User findUserByEmail(String email);
}

package com.jwt.integration.service.implemantation;

import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jwt.integration.domain.User;
import com.jwt.integration.domain.UserPrincipal;
import com.jwt.integration.repository.UserRepository;
import com.jwt.integration.service.UserService;

@Service
@Transactional
// @Qualifier("UserDetailsService")
public class UserServiceImplementation implements UserService,UserDetailsService
{
    @Autowired
    private UserRepository userRepository;
    private Logger LOGGER = LoggerFactory.getLogger(UserServiceImplementation.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findUserByUsername(username);
       if(user == null)
       {
        LOGGER.error("User not found by username: "+ username);
        throw new UsernameNotFoundException("User not found by username: "+ username);
       }
       else{
        user.setLastLoginDateDisplay(user.getLastLoginDate());
        user.setLastLoginDate(new Date());
        userRepository.save(user);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        LOGGER.info("Returning Found User by username :" + username);
        return userPrincipal;
       }
        
    }
    
}

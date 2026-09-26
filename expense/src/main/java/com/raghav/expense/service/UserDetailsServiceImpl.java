package com.raghav.expense.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.raghav.expense.model.User;
import com.raghav.expense.model.UserPrincipal;
import com.raghav.expense.repository.UserRepository;

@Service 
public class UserDetailsServiceImpl implements UserDetailsService{

    private final UserRepository userRepository;
    public UserDetailsServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> u=userRepository.findByEmail(username);
       if(u.isEmpty()) throw new UsernameNotFoundException("User not found");
       User user=u.get();
       return new UserPrincipal(user);
    }
    
}

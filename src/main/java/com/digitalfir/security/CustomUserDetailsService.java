package com.digitalfir.security;

import com.digitalfir.backend.model.Role;
import com.digitalfir.backend.model.User;
import com.digitalfir.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
        // 2️⃣ 🔴 THIS IS WHERE YOU ADDED IT
        if (user.getRole() == Role.POLICE && !user.isEnabled()) {
            throw new DisabledException("Police account is disabled by admin");
        }


        // 3️⃣ If enabled → allow login
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().name()
                ))
   
        );
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}




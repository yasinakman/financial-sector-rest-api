package com.akman.springbootdemo.service.userdetailservice;

import com.akman.springbootdemo.repository.UserRepository;
import com.akman.springbootdemo.rest_api.exceptions.BadRequestException;
import com.akman.springbootdemo.utils.ErrorConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        com.akman.springbootdemo.model.user.User userByUsername
                = userRepository.findById(s).orElseThrow(() -> new BadRequestException(ErrorConstants.ANY_USER));
        return new User(userByUsername.getUsername(), userByUsername.getPassword(), new ArrayList<>());
    }
}

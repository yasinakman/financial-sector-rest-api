package com.akman.springbootdemo.service;

import com.akman.springbootdemo.model.user.User;
import com.akman.springbootdemo.repository.UserRepository;
import com.akman.springbootdemo.service.userdetailservice.MyUserDetailsService;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MyUserDetailsService.class)
public class MyUserDetailsServiceTest {

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testLoadUserByUsername() {
        //preparation
        User user = prepareUser();
        String username = user.getUsername();
        doReturn(Optional.of(user)).when(userRepository).findById(username);

        //test
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

        //verification
        verify(userRepository, times(1)).findById(username);
        assertEquals(username, userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    private User prepareUser() {
        final String username = "yasin";
        final String password = "akman";
        return User.builder().username(username).password(password).build();
    }
}
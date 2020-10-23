package com.akman.springbootdemo.rest_api.controller;

import com.akman.springbootdemo.model.user.AuthenticationRequest;
import com.akman.springbootdemo.service.userdetailservice.MyUserDetailsService;
import com.akman.springbootdemo.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(PowerMockRunner.class)
public class AuthenticationControllerTest {

    private static final String AUTHENTICATION_ENDPOINT = "/";

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private JwtUtil jwtTokenUtil;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mvc = MockMvcBuilders.standaloneSetup(
                new AuthenticationController(authenticationManager, myUserDetailsService, jwtTokenUtil)
        ).build();
    }

    @Test
    public void testGetJwt() throws Exception {
        //preparation
        AuthenticationRequest authenticationRequest = prepareAuthenticationRequest();
        String requestStr = objectMapper.writeValueAsString(authenticationRequest);
        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()
        ))).willReturn(null);
        UserDetails userDetails = prepareUserDetails();
        given(myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername()))
                .willReturn(userDetails);
        final String jwt = "jwt";
        given(jwtTokenUtil.generateToken(userDetails))
                .willReturn(jwt);

        //test
        MvcResult mvcResult = mvc.perform(post(AUTHENTICATION_ENDPOINT + "authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestStr))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        //verification
        assertEquals(jwt, contentAsString);
    }

    @Test
    public void testGetJwt_exception() throws Exception {
        //preparation
        AuthenticationRequest authenticationRequest = prepareAuthenticationRequest();
        String requestStr = objectMapper.writeValueAsString(authenticationRequest);
        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()
        ))).willThrow(new NullPointerException());

        //test and verification
        mvc.perform(post(AUTHENTICATION_ENDPOINT + "authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestStr))
                .andExpect(status().isUnauthorized());
    }

    private UserDetails prepareUserDetails() {
        AuthenticationRequest authenticationRequest = prepareAuthenticationRequest();
        return new User(authenticationRequest.getUsername(), authenticationRequest.getPassword(), new ArrayList<>());
    }

    private AuthenticationRequest prepareAuthenticationRequest() {
        return AuthenticationRequest.builder()
                .username("yasin")
                .password("akman").build();
    }
}
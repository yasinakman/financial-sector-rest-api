package com.akman.springbootdemo.rest_api.controller;

import com.akman.springbootdemo.model.user.AuthenticationRequest;
import com.akman.springbootdemo.repository.UserRepository;
import com.akman.springbootdemo.rest_api.exceptions.UnAuthorizedException;
import com.akman.springbootdemo.service.userdetailservice.MyUserDetailsService;
import com.akman.springbootdemo.utils.JwtUtil;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
@Api(tags = {"Authentication"})
@SwaggerDefinition(tags = {
        @Tag(name = "Authentication", description = "Authentication Controller")
})
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtUtil jwtTokenUtil;

    @ApiOperation(value = "Get Authentication")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authorized :)"),
            @ApiResponse(code = 401, message = "Unauthorized :("),
    })
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<String> getJwt(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (Exception e) {
            throw new UnAuthorizedException("Username is not authorized !");
        }
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(jwt);
    }
}

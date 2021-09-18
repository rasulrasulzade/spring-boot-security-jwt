package com.company.springbootsecurityjwt.controller;

import com.company.springbootsecurityjwt.model.AuthenticationRequest;
import com.company.springbootsecurityjwt.model.AuthenticationResponse;
import com.company.springbootsecurityjwt.service.JwtUtil;
import com.company.springbootsecurityjwt.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {
    @Autowired
    private AuthenticationManager authenticationManager;

     @Autowired
     private MyUserDetailsService myUserDetailsService;

     @Autowired
     private JwtUtil jwtUtil;

    @GetMapping("/hello")
    public String getHelloWorld(){
        return "Hello World!";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> generateJwtToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Username or password is incorrect",e);
        }

        UserDetails  userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}

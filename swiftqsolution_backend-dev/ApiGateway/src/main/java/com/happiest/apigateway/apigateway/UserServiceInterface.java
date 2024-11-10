package com.happiest.apigateway.apigateway;

import com.happiest.apigateway.model.Users;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@FeignClient(name="http://UserService/user")

public interface UserServiceInterface {
    @GetMapping("/register")
    public Users register(@RequestBody Users user);

//    @PostMapping("/submitfeedback")
//    public ResponseEntity<Map<String, Object>> submitFeedback(@RequestBody Feedback feedback);
//


//    @PostMapping("/forgot-password")
//    public Users findByUsername(String email);
//
//    @PostMapping("/reset-password")
//    public Users findByResetToken(String token);


    @GetMapping("/getallmessages")
    public ResponseEntity<Map<String, Object>> getAllMessages();

    @GetMapping("/users/count")
    public ResponseEntity<Map<String, Object>> countUsers(@RequestParam(defaultValue = "user") String role);

    @GetMapping("/managers/count")
    public ResponseEntity<Map<String, Object>> countManagers(@RequestParam(defaultValue = "manager") String role);


}



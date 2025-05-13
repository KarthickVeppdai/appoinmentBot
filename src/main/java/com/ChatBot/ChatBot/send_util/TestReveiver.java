package com.ChatBot.ChatBot.send_util;

import com.ChatBot.ChatBot.rag_service.HospitalInfoRag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class TestReveiver {



    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody String userRequest) {



        System.out.println(userRequest);
        return ResponseEntity.ok("200");
    }
}

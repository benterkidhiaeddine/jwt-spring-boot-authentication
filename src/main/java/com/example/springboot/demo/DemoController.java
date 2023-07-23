package com.example.springboot.demo;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/protected")
public class DemoController {

    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld(){
        return ResponseEntity.ok("Hello World from protected endpoint");
    }

}

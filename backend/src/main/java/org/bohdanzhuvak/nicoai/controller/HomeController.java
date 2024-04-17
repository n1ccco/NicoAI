package org.bohdanzhuvak.nicoai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping
    String Index(){
        return "Hello world!";
    }
}

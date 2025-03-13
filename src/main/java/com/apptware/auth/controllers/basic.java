package com.apptware.auth.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class basic {

    @GetMapping
    public String basicString() {
        return "Basic";
    }
}

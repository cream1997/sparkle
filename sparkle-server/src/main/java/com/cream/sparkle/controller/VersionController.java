package com.cream.sparkle.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {

    @GetMapping("/latestVersionNumber")
    public String latestVersionNumber() {
        return "0.0.0";
    }
}

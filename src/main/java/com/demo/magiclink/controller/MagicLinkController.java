package com.demo.magiclink.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.demo.magiclink.service.MagicLinkService;

@RestController
@RequestMapping("/magic-link")
@Validated
public class MagicLinkController {

    @Autowired
    private MagicLinkService magicLinkService;

    @PostMapping("/request")
    public ResponseEntity<?> request() {
        return magicLinkService.requestMagicLink();
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam("token") String token) {
        return magicLinkService.verifyToken(token);
    }
}
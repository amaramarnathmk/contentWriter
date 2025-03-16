package com.email.contentWriter;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin(origins="*")
public class EmailController {
    private final EmailService emailService;
    @PostMapping("/generateemail")
    public ResponseEntity<String> getEmail(@RequestBody EmailRequest email)
    {
    String response = emailService.generateEmailReply(email);
    return ResponseEntity.ok(response);
    }


}

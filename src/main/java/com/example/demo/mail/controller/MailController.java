package com.example.demo.mail.controller;


import com.example.demo.mail.dto.EmailRequestDto;
import com.example.demo.mail.service.MailSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MailController {
    private final MailSendService mailService;
    @PostMapping("/mailSend")
    public String mailSend(@RequestBody EmailRequestDto emailDto){
        return mailService.joinEmail(emailDto.getEmail());
    }
}

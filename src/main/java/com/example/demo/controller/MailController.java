package com.example.demo.controller;


import com.example.demo.entity.MailData;
import com.example.demo.model.ApiResponse;
import com.example.demo.model.MailDataModel;
import com.example.demo.service.MailService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mail")
public class MailController {
    @Autowired
    MailService mailService;
    @GetMapping
    public ResponseEntity<ApiResponse<List<MailDataModel>>> inboxMails(){
        return mailService.allMailData();
    }

    @GetMapping("/from")
    public ResponseEntity<ApiResponse<List<MailDataModel>>> mailDataBySender(@RequestParam String senderMail){
        return mailService.mailDataBySender(senderMail);
    }
}

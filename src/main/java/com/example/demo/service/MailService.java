package com.example.demo.service;

import com.example.demo.model.ApiResponse;
import com.example.demo.model.MailDataModel;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface MailService {
    void saveMailData(String subject, String fromAddress, String toAddress, String receivedDate) throws ParseException;

    ResponseEntity<ApiResponse<List<MailDataModel>>> allMailData();

    ResponseEntity<ApiResponse<List<MailDataModel>>> mailDataBySender(String senderMail);
}

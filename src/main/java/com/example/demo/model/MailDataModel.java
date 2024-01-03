package com.example.demo.model;

import com.example.demo.entity.MailData;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MailDataModel implements Serializable {

    private String subject;
    private String fromAddress;
    private String toAddress;
    private Date receivedDate;

    public static MailDataModel fromEntity(MailData mailData) {
        MailDataModel model = new MailDataModel();

        model.setSubject(mailData.getSubject());
        model.setFromAddress(mailData.getFromAddress());
        model.setToAddress(mailData.getToAddress());
        model.setReceivedDate(mailData.getReceivedDate());
        return model;
}
}

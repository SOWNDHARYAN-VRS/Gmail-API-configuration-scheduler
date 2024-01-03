package com.example.demo.service;

import com.example.demo.entity.MailData;
import com.example.demo.model.ApiResponse;
import com.example.demo.model.MailDataModel;
import com.example.demo.repository.MailDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MailServiceImpl implements MailService{

    @Autowired
    MailDataRepository mailDataRepository;
    @Override
    public void saveMailData(String subject, String fromField, String toAddress, String receivedDate) throws ParseException {

        int startIndex = fromField.indexOf('<');
        int endIndex = fromField.indexOf('>');

        // If both angle brackets are found and in the correct order
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            // Extract the substring between angle brackets
            fromField=fromField.substring(startIndex + 1, endIndex).trim();
        }
        System.out.println(receivedDate);
        String[] parts = receivedDate.split(" ");
        System.out.println(Arrays.stream(parts).toList());
        String dayOfWeek = parts[0];
        int dayOfMonth = Integer.parseInt(parts[1]);
        String month = parts[2];
        int year = Integer.parseInt(parts[3]);
        String time = parts[4];
        String timeZone = parts[5];

        // Format components into a new date string
        String formattedDate = dayOfMonth + " " + month + " " + year + " " + time + " " + timeZone;

        // Create a SimpleDateFormat with the desired pattern
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            // Parse the formatted date string to get the Date object
            Date date = sdf.parse(formattedDate);



        mailDataRepository.save(new MailData(null,subject,fromField,toAddress,date));
    }

    @Override
    public ResponseEntity<ApiResponse<List<MailDataModel>>> allMailData() {
        List<MailDataModel> mailDataModels=new ArrayList<>();
        for(MailData m:mailDataRepository.findAll()){
            mailDataModels.add(MailDataModel.fromEntity(m));
        }
        return ResponseEntity.ok(new ApiResponse<>("List of all mail data from inbox",mailDataModels));
    }

    @Override
    public ResponseEntity<ApiResponse<List<MailDataModel>>> mailDataBySender(String senderMail) {
        List<MailDataModel> mailDataModels=new ArrayList<>();
        for(MailData m:mailDataRepository.findByFromAddress(senderMail)){
            mailDataModels.add(MailDataModel.fromEntity(m));
        }
        return ResponseEntity.ok(new ApiResponse<>("Filtered mail data by sender email",mailDataModels));
    }

}

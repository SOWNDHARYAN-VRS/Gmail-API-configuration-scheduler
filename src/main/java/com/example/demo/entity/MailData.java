package com.example.demo.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//import org.springframework.data.mongodb.core.mapping.Document;

//@Document(collection = "sample")

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MailData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String subject;
    private String fromAddress;
    private String toAddress;
    private Date receivedDate;

}

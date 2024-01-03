package com.example.demo.repository;

import com.example.demo.entity.MailData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MailDataRepository extends JpaRepository<MailData, Integer> {
    List<MailData> findByFromAddress(String senderMail);

}

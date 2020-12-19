package com.cloud.backend.service;

import com.cloud.model.common.Page;
import com.cloud.model.mail.Mail;

import java.util.Map;

public interface MailService {

    void saveMail(Mail mail);

    void updateMail(Mail mail);

    void sendMail(Mail mail);

    Mail findById(Long id);

    Page<Mail> findMails(Map<String, Object> params);
}

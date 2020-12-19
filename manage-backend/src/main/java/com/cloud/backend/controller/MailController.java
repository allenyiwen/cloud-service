package com.cloud.backend.controller;

import com.cloud.backend.service.MailService;
import com.cloud.model.common.Page;
import com.cloud.model.log.LogAnnotation;
import com.cloud.model.mail.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mails")
public class MailController {

    @Autowired
    private MailService mailService;

    @PreAuthorize("hasAuthority('mail:query')")
    @GetMapping("/{id}")
    public Mail findById(@PathVariable Long id) {
        return mailService.findById(id);
    }

    @PreAuthorize("hasAuthority('mail:query')")
    @GetMapping
    public Page<Mail> findMails(@RequestParam Map<String, Object> params) {
        return mailService.findMails(params);
    }

    /**
     * 保存邮件
     *
     * @param mail
     * @param send 是否发送邮件
     * @return
     */
    @LogAnnotation(module = "保存邮件")
    @PreAuthorize("hasAuthority('mail:save')")
    @PostMapping
    public Mail save(@RequestBody Mail mail, Boolean send) {
        mailService.saveMail(mail);
        if (Boolean.TRUE == send) {
            mailService.sendMail(mail);
        }

        return mail;
    }

    /**
     * 修改邮件
     *
     * @param mail
     * @param send 是否发送
     * @return
     */
    @LogAnnotation(module = "修改邮件")
    @PreAuthorize("hasAuthority('mail:update')")
    @PutMapping
    public Mail update(@RequestBody Mail mail, Boolean send) {
        mailService.updateMail(mail);
        if (Boolean.TRUE == send) {
            mailService.sendMail(mail);
        }

        return mail;
    }


}

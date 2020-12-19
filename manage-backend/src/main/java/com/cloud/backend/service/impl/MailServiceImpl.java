package com.cloud.backend.service.impl;

import com.cloud.backend.dao.MailDao;
import com.cloud.backend.service.MailService;
import com.cloud.backend.service.SendMailService;
import com.cloud.common.utils.AppUserUtil;
import com.cloud.common.utils.PageUtil;
import com.cloud.model.common.Page;
import com.cloud.model.mail.Mail;
import com.cloud.model.mail.constants.MailStatus;
import com.cloud.model.user.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private MailDao mailDao;
    @Autowired
    private SendMailService sendMailService;

    /**
     * 保存邮件
     *
     * @param mail
     */
    @Transactional
    @Override
    public void saveMail(Mail mail) {
        if (mail.getUserId() == null || StringUtils.isBlank(mail.getUsername())) {
            AppUser appUser = AppUserUtil.getLoginAppUser();
            if (appUser != null) {
                mail.setUserId(appUser.getId());
                mail.setUsername(appUser.getUsername());
            }
        }
        if (mail.getUserId() == null) {
            mail.setUserId(0L);
            mail.setUsername("系统邮件");
        }

        if (mail.getCreateTime() == null) {
            mail.setCreateTime(new Date());
        }
        mail.setUpdateTime(mail.getCreateTime());
        mail.setStatus(MailStatus.DRAFT);

        mailDao.save(mail);
        log.info("保存邮件：{}", mail);
    }

    /**
     * 修改未发送邮件
     *
     * @param mail
     */
    @Transactional
    @Override
    public void updateMail(Mail mail) {
        Mail oldMail = mailDao.findById(mail.getId());
        if (oldMail.getStatus() == MailStatus.SUCCESS) {
            throw new IllegalArgumentException("已发送的邮件不能编辑");
        }
        mail.setUpdateTime(new Date());

        mailDao.update(mail);

        log.info("修改邮件：{}", mail);
    }

    /**
     * 异步发送邮件
     *
     * @param mail
     */
    @Override
    @Async
    public void sendMail(Mail mail) {
        boolean flag = sendMailService.sendMail(mail.getToEmail(), mail.getSubject(), mail.getContent());
        mail.setSendTime(new Date());
        mail.setStatus(flag ? MailStatus.SUCCESS : MailStatus.ERROR); // 邮件发送结果

        mailDao.update(mail);
    }

    @Override
    public Mail findById(Long id) {
        return mailDao.findById(id);
    }

    @Override
    public Page<Mail> findMails(Map<String, Object> params) {
        int total = mailDao.count(params);
        List<Mail> list = Collections.emptyList();
        if (total > 0) {
            PageUtil.pageParamConver(params, true);

            list = mailDao.findData(params);
        }
        return new Page<>(total, list);
    }
}

package com.cloud.model.mail;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮件
 *
 * @author allen
 */
@Data
public class Mail implements Serializable {

    private static final long serialVersionUID = 4885093464493499448L;

    private Long id;
    private Long userId;
    /**
     * 发送人用户名
     */
    private String username;
    /**
     * 收件人邮件地址
     */
    private String toEmail;
    /**
     * 标题
     */
    private String subject;
    /**
     * 正文
     */
    private String content;
    /**
     * 发送状态
     *
     * @see com.cloud.model.mail.constants.MailStatus
     */
    private Integer status;
    /**
     * 发送时间
     */
    private Date sendTime;
    private Date createTime;
    private Date updateTime;

}

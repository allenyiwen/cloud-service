package com.cloud.backend.service;

public interface SendMailService {

	/**
	 * 发送邮件
	 * 
	 * @param toUser
	 * @param subject
	 *            标题
	 * @param text
	 *            内容（支持html格式）
	 */
	boolean sendMail(String toUser, String subject, String text);
}

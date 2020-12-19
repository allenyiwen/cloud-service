package com.cloud.notification.service;

import java.util.Map;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.cloud.model.common.Page;
import com.cloud.notification.model.Sms;

public interface SmsService {

	void save(Sms sms, Map<String, String> params);

	void update(Sms sms);

	Sms findById(Long id);

	Page<Sms> findSms(Map<String, Object> params);

	/**
	 * 发送短信
	 */
	SendSmsResponse sendSmsMsg(Sms sms);
}

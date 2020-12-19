package com.cloud.notification.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.common.utils.PhoneUtil;
import com.cloud.model.common.Page;
import com.cloud.notification.model.Sms;
import com.cloud.notification.model.VerificationCode;
import com.cloud.notification.service.SmsService;
import com.cloud.notification.service.VerificationCodeService;

@RestController
public class SmsController {

	@Autowired
	private VerificationCodeService verificationCodeService;

	/**
	 * 发送短信验证码
	 * 
	 * @param phone
	 * @return
	 */
	@PostMapping(value = "/notification-anon/codes", params = { "phone" })
	public VerificationCode sendSmsVerificationCode(String phone) {
		if (!PhoneUtil.checkPhone(phone)) {
			throw new IllegalArgumentException("手机号格式不正确");
		}

		VerificationCode verificationCode = verificationCodeService.generateCode(phone);

		return verificationCode;
	}

	/**
	 * 根据验证码和key获取手机号
	 * 
	 * @param key
	 * @param code
	 * @param delete
	 *            是否删除key
	 * @param second
	 *            不删除时，可重置过期时间（秒）
	 * @return
	 */
	@GetMapping(value = "/notification-anon/internal/phone", params = { "key", "code" })
	public String matcheCodeAndGetPhone(String key, String code, Boolean delete, Integer second) {
		return verificationCodeService.matcheCodeAndGetPhone(key, code, delete, second);
	}

	@Autowired
	private SmsService smsService;

	/**
	 * 查询短信发送记录
	 * 
	 * @param params
	 * @return
	 */
	@PreAuthorize("hasAuthority('sms:query')")
	@GetMapping("/sms")
	public Page<Sms> findSms(@RequestParam Map<String, Object> params) {
		return smsService.findSms(params);
	}
}

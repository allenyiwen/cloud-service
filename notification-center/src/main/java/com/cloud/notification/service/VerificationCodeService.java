package com.cloud.notification.service;

import com.cloud.notification.model.VerificationCode;

public interface VerificationCodeService {

	VerificationCode generateCode(String phone);

	String matcheCodeAndGetPhone(String key, String code, Boolean delete, Integer second);
}

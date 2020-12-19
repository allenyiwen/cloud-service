package com.cloud.notification;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cloud.notification.model.Sms;
import com.cloud.notification.service.SmsService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class T {

	@Autowired
	private SmsService smsService;

	@Test
	public void sendTest() {
		Sms sms = new Sms();
		sms.setPhone("15000000000");
		
		Map<String, String> params = new HashMap<>();
		params.put("code", "34Cd9j");
		
		smsService.save(sms, params);
	}
}

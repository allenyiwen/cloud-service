package com.cloud.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 通知中心
 * 
 * @author allen zhangxuelun@cmsr.chinamobile.com
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class NotificationCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationCenterApplication.class, args);
	}

}
package com.cloud.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 管理后台
 * 
 * @author allen zhangxuelun@cmsr.chinamobile.com
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ManageBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManageBackendApplication.class, args);
	}

}
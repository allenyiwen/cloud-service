package com.cloud.notification.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Sms implements Serializable {

	private static final long serialVersionUID = -1084513813698387690L;

	private Long id;
	private String phone;
	private String signName;
	private String templateCode;
	private String params;
	private String bizId;
	private String code;
	private String message;
	private Date day;
	private Date createTime;
	private Date updateTime;
}

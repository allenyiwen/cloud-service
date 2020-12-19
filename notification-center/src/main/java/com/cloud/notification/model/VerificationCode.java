package com.cloud.notification.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class VerificationCode implements Serializable {

	private static final long serialVersionUID = -3122215341764978293L;

	private String key;
}

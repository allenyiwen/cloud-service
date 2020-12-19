package com.cloud.model.user;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 *
 */
@Data
public class AppUser implements Serializable {
	private static final long serialVersionUID = -3770524765010588222L;

	//private static final long serialVersionUID = 611197991672067628L;

	private Long id;
	private String username;
	private String password;
	private String nickname;
	private String headImgUrl;
	private String phone;
	private Integer sex;
	/**
	 * 状态
	 */
	private Boolean enabled;
	private String type;
	private Date createTime;
	private Date updateTime;

}

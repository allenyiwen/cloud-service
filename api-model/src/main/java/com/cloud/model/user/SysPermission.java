package com.cloud.model.user;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 权限标识
 * 
 * @author allen
 *
 */
@Data
public class SysPermission implements Serializable {

	private static final long serialVersionUID = 280565233032255804L;

	private Long id;
	private String permission;
	private String name;
	private Date createTime;
	private Date updateTime;

}

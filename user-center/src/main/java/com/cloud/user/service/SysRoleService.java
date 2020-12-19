package com.cloud.user.service;

import java.util.Map;
import java.util.Set;

import com.cloud.model.common.Page;
import com.cloud.model.user.SysPermission;
import com.cloud.model.user.SysRole;

public interface SysRoleService {

	void save(SysRole sysRole);

	void update(SysRole sysRole);

	void deleteRole(Long id);

	void setPermissionToRole(Long id, Set<Long> permissionIds);

	SysRole findById(Long id);

	Page<SysRole> findRoles(Map<String, Object> params);

	Set<SysPermission> findPermissionsByRoleId(Long roleId);
}

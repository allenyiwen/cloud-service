package com.cloud.user.controller;

import com.cloud.model.common.Page;
import com.cloud.model.log.LogAnnotation;
import com.cloud.model.user.SysPermission;
import com.cloud.model.user.SysRole;
import com.cloud.user.service.SysRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;

	/**
	 * 管理后台添加角色
	 * 
	 * @param sysRole
	 */
	@LogAnnotation(module = "添加角色")
	@PreAuthorize("hasAuthority('back:role:save')")
	@PostMapping("/roles")
	public SysRole save(@RequestBody SysRole sysRole) {
		if (StringUtils.isBlank(sysRole.getCode())) {
			throw new IllegalArgumentException("角色code不能为空");
		}
		if (StringUtils.isBlank(sysRole.getName())) {
			sysRole.setName(sysRole.getCode());
		}

		sysRoleService.save(sysRole);

		return sysRole;
	}

	/**
	 * 管理后台删除角色
	 * 
	 * @param id
	 */
	@LogAnnotation(module = "删除角色")
	@PreAuthorize("hasAuthority('back:role:delete')")
	@DeleteMapping("/roles/{id}")
	public void deleteRole(@PathVariable Long id) {
		sysRoleService.deleteRole(id);
	}

	/**
	 * 管理后台修改角色
	 * 
	 * @param sysRole
	 */
	@LogAnnotation(module = "修改角色")
	@PreAuthorize("hasAuthority('back:role:update')")
	@PutMapping("/roles")
	public SysRole update(@RequestBody SysRole sysRole) {
		if (StringUtils.isBlank(sysRole.getName())) {
			throw new IllegalArgumentException("角色名不能为空");
		}

		sysRoleService.update(sysRole);

		return sysRole;
	}

	/**
	 * 管理后台给角色分配权限
	 *
	 * @param id            角色id
	 * @param permissionIds 权限ids
	 */
	@LogAnnotation(module = "分配权限")
	@PreAuthorize("hasAuthority('back:role:permission:set')")
	@PostMapping("/roles/{id}/permissions")
	public void setPermissionToRole(@PathVariable Long id, @RequestBody Set<Long> permissionIds) {
		sysRoleService.setPermissionToRole(id, permissionIds);
	}

	/**
	 * 获取角色的权限
	 * 
	 * @param id
	 */
	@PreAuthorize("hasAnyAuthority('back:role:permission:set','role:permission:byroleid')")
	@GetMapping("/roles/{id}/permissions")
	public Set<SysPermission> findPermissionsByRoleId(@PathVariable Long id) {
		return sysRoleService.findPermissionsByRoleId(id);
	}

	@PreAuthorize("hasAuthority('back:role:query')")
	@GetMapping("/roles/{id}")
	public SysRole findById(@PathVariable Long id) {
		return sysRoleService.findById(id);
	}

	/**
	 * 搜索角色
	 * 
	 * @param params
	 */
	@PreAuthorize("hasAuthority('back:role:query')")
	@GetMapping("/roles")
	public Page<SysRole> findRoles(@RequestParam Map<String, Object> params) {
		return sysRoleService.findRoles(params);
	}

}

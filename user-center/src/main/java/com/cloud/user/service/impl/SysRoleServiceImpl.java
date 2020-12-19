package com.cloud.user.service.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.cloud.common.utils.PageUtil;
import com.cloud.model.common.Page;
import com.cloud.model.user.SysPermission;
import com.cloud.model.user.SysRole;
import com.cloud.model.user.constants.UserCenterMq;
import com.cloud.user.dao.RolePermissionDao;
import com.cloud.user.dao.SysRoleDao;
import com.cloud.user.dao.UserRoleDao;
import com.cloud.user.service.SysRoleService;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private RolePermissionDao rolePermissionDao;
	@Autowired
	private AmqpTemplate amqpTemplate;

	@Transactional
	@Override
	public void save(SysRole sysRole) {
		SysRole role = sysRoleDao.findByCode(sysRole.getCode());
		if (role != null) {
			throw new IllegalArgumentException("角色code已存在");
		}

		sysRole.setCreateTime(new Date());
		sysRole.setUpdateTime(sysRole.getCreateTime());

		sysRoleDao.save(sysRole);
		log.info("保存角色：{}", sysRole);
	}

	@Transactional
	@Override
	public void update(SysRole sysRole) {
		sysRole.setUpdateTime(new Date());

		sysRoleDao.update(sysRole);
		log.info("修改角色：{}", sysRole);
	}

	@Transactional
	@Override
	public void deleteRole(Long id) {
		SysRole sysRole = sysRoleDao.findById(id);

		sysRoleDao.delete(id);
		rolePermissionDao.deleteRolePermission(id, null);
		userRoleDao.deleteUserRole(null, id);

		log.info("删除角色：{}", sysRole);

		// 发布role删除的消息
		amqpTemplate.convertAndSend(UserCenterMq.MQ_EXCHANGE_USER, UserCenterMq.ROUTING_KEY_ROLE_DELETE, id);
	}

	/**
	 * 给角色设置权限
	 *
	 * @param roleId
	 * @param permissionIds
	 */
	@Transactional
	@Override
	public void setPermissionToRole(Long roleId, Set<Long> permissionIds) {
		SysRole sysRole = sysRoleDao.findById(roleId);
		if (sysRole == null) {
			throw new IllegalArgumentException("角色不存在");
		}

		// 查出角色对应的old权限
		Set<Long> oldPermissionIds = rolePermissionDao.findPermissionsByRoleIds(Sets.newHashSet(roleId)).stream()
				.map(p -> p.getId()).collect(Collectors.toSet());

		// 需要添加的权限
		Collection<Long> addPermissionIds = org.apache.commons.collections4.CollectionUtils.subtract(permissionIds,
				oldPermissionIds);
		if (!CollectionUtils.isEmpty(addPermissionIds)) {
			addPermissionIds.forEach(permissionId -> {
				rolePermissionDao.saveRolePermission(roleId, permissionId);
			});
		}
		// 需要移除的权限
		Collection<Long> deletePermissionIds = org.apache.commons.collections4.CollectionUtils
				.subtract(oldPermissionIds, permissionIds);
		if (!CollectionUtils.isEmpty(deletePermissionIds)) {
			deletePermissionIds.forEach(permissionId -> {
				rolePermissionDao.deleteRolePermission(roleId, permissionId);
			});
		}

		log.info("给角色id：{}，分配权限：{}", roleId, permissionIds);
	}

	@Override
	public SysRole findById(Long id) {
		return sysRoleDao.findById(id);
	}

	@Override
	public Page<SysRole> findRoles(Map<String, Object> params) {
		int total = sysRoleDao.count(params);
		List<SysRole> list = Collections.emptyList();
		if (total > 0) {
			PageUtil.pageParamConver(params, false);

			list = sysRoleDao.findData(params);
		}
		return new Page<>(total, list);
	}

	@Override
	public Set<SysPermission> findPermissionsByRoleId(Long roleId) {
		return rolePermissionDao.findPermissionsByRoleIds(Sets.newHashSet(roleId));
	}
}

package com.cloud.user.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloud.common.utils.PageUtil;
import com.cloud.model.common.Page;
import com.cloud.model.user.SysPermission;
import com.cloud.user.dao.RolePermissionDao;
import com.cloud.user.dao.SysPermissionDao;
import com.cloud.user.service.SysPermissionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

	@Autowired
	private SysPermissionDao sysPermissionDao;
	@Autowired
	private RolePermissionDao rolePermissionDao;

	@Override
	public Set<SysPermission> findByRoleIds(Set<Long> roleIds) {
		return rolePermissionDao.findPermissionsByRoleIds(roleIds);
	}

	@Transactional
	@Override
	public void save(SysPermission sysPermission) {
		SysPermission permission = sysPermissionDao.findByPermission(sysPermission.getPermission());
		if (permission != null) {
			throw new IllegalArgumentException("权限标识已存在");
		}
		sysPermission.setCreateTime(new Date());
		sysPermission.setUpdateTime(sysPermission.getCreateTime());

		sysPermissionDao.save(sysPermission);
		log.info("保存权限标识：{}", sysPermission);
	}

	@Transactional
	@Override
	public void update(SysPermission sysPermission) {
		sysPermission.setUpdateTime(new Date());
		sysPermissionDao.update(sysPermission);
		log.info("修改权限标识：{}", sysPermission);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		SysPermission permission = sysPermissionDao.findById(id);
		if (permission == null) {
			throw new IllegalArgumentException("权限标识不存在");
		}

		sysPermissionDao.delete(id);
		rolePermissionDao.deleteRolePermission(null, id);
		log.info("删除权限标识：{}", permission);
	}

	@Override
	public Page<SysPermission> findPermissions(Map<String, Object> params) {
		int total = sysPermissionDao.count(params);
		List<SysPermission> list = Collections.emptyList();
		if (total > 0) {
			PageUtil.pageParamConver(params, false);

			list = sysPermissionDao.findData(params);
		}
		return new Page<>(total, list);
	}
}

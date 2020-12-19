package com.cloud.user.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cloud.common.utils.PhoneUtil;
import com.cloud.model.user.constants.UserType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.cloud.common.utils.PageUtil;
import com.cloud.model.common.Page;
import com.cloud.model.user.AppUser;
import com.cloud.model.user.LoginAppUser;
import com.cloud.model.user.SysPermission;
import com.cloud.model.user.SysRole;
import com.cloud.model.user.UserCredential;
import com.cloud.model.user.constants.CredentialType;
import com.cloud.user.dao.AppUserDao;
import com.cloud.user.dao.UserRoleDao;
import com.cloud.user.dao.UserCredentialsDao;
import com.cloud.user.service.AppUserService;
import com.cloud.user.service.SysPermissionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private UserCredentialsDao userCredentialsDao;

    @Transactional
    @Override
    public void addAppUser(AppUser appUser) {
        String username = appUser.getUsername();
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("用户名不能为空");
        }

        if (PhoneUtil.checkPhone(username)) {// 防止用手机号直接当用户名，手机号要发短信验证
            throw new IllegalArgumentException("用户名要包含英文字符");
        }

        if (username.contains("@")) {// 防止用邮箱直接当用户名，邮箱也要发送验证（暂未开发）
            throw new IllegalArgumentException("用户名不能包含@");
        }

        if (username.contains("|")) {
            throw new IllegalArgumentException("用户名不能包含|字符");
        }

        if (StringUtils.isBlank(appUser.getPassword())) {
            throw new IllegalArgumentException("密码不能为空");
        }

        if (StringUtils.isBlank(appUser.getNickname())) {
            appUser.setNickname(username);
        }

        if (StringUtils.isBlank(appUser.getType())) {
            appUser.setType(UserType.APP.name());
        }

        UserCredential userCredential = userCredentialsDao.findByUsername(appUser.getUsername());
        if (userCredential != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        appUser.setPassword(passwordEncoder.encode(appUser.getPassword())); // 加密密码
        appUser.setEnabled(Boolean.TRUE);
        appUser.setCreateTime(new Date());
        appUser.setUpdateTime(appUser.getCreateTime());

        appUserDao.save(appUser);
        userCredentialsDao
                .save(new UserCredential(appUser.getUsername(), CredentialType.USERNAME.name(), appUser.getId()));
        log.info("添加用户：{}", appUser);
    }

    @Transactional
    @Override
    public void updateAppUser(AppUser appUser) {
        appUser.setUpdateTime(new Date());

        appUserDao.update(appUser);
        log.info("修改用户：{}", appUser);
    }

    @Transactional
    @Override
    public LoginAppUser findByUsername(String username) {
        AppUser appUser = userCredentialsDao.findUserByUsername(username);
        if (appUser != null) {
            LoginAppUser loginAppUser = new LoginAppUser();
            BeanUtils.copyProperties(appUser, loginAppUser);

            Set<SysRole> sysRoles = userRoleDao.findRolesByUserId(appUser.getId());
            loginAppUser.setSysRoles(sysRoles);// 设置角色

            if (!CollectionUtils.isEmpty(sysRoles)) {
                Set<Long> roleIds = sysRoles.parallelStream().map(SysRole::getId).collect(Collectors.toSet());
                Set<SysPermission> sysPermissions = sysPermissionService.findByRoleIds(roleIds);
                if (!CollectionUtils.isEmpty(sysPermissions)) {
                    Set<String> permissions = sysPermissions.parallelStream().map(SysPermission::getPermission)
                            .collect(Collectors.toSet());

                    loginAppUser.setPermissions(permissions);// 设置权限集合
                }

            }

            return loginAppUser;
        }

        return null;
    }

    @Override
    public AppUser findById(Long id) {
        return appUserDao.findById(id);
    }

    /**
     * 给用户设置角色<br>
     * 这里采用先删除老角色，再插入新角色
     */
    @Transactional
    @Override
    public void setRoleToUser(Long id, Set<Long> roleIds) {
        AppUser appUser = appUserDao.findById(id);
        if (appUser == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        userRoleDao.deleteUserRole(id, null);
        if (!CollectionUtils.isEmpty(roleIds)) {
            roleIds.forEach(roleId -> {
                userRoleDao.saveUserRoles(id, roleId);
            });
        }

        log.info("修改用户：{}的角色，{}", appUser.getUsername(), roleIds);
    }

    /**
     * 修改密码
     *
     * @param id
     * @param oldPassword
     * @param newPassword
     */
    @Transactional
    @Override
    public void updatePassword(Long id, String oldPassword, String newPassword) {
        AppUser appUser = appUserDao.findById(id);
        if (StringUtils.isNoneBlank(oldPassword)) {
            if (!passwordEncoder.matches(oldPassword, appUser.getPassword())) { // 旧密码校验
                throw new IllegalArgumentException("旧密码错误");
            }
        }

        AppUser user = new AppUser();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(newPassword)); // 加密密码

        updateAppUser(user);
        log.info("修改密码：{}", user);
    }

    @Override
    public Page<AppUser> findUsers(Map<String, Object> params) {
        int total = appUserDao.count(params);
        List<AppUser> list = Collections.emptyList();
        if (total > 0) {
            PageUtil.pageParamConver(params, true);

            list = appUserDao.findData(params);
        }
        return new Page<>(total, list);
    }

    @Override
    public Set<SysRole> findRolesByUserId(Long userId) {
        return userRoleDao.findRolesByUserId(userId);
    }

    /**
     * 绑定手机号
     */
    @Transactional
    @Override
    public void bindingPhone(Long userId, String phone) {
        UserCredential userCredential = userCredentialsDao.findByUsername(phone);
        if (userCredential != null) {
            throw new IllegalArgumentException("手机号已被绑定");
        }

        AppUser appUser = appUserDao.findById(userId);
        appUser.setPhone(phone);

        updateAppUser(appUser);
        log.info("绑定手机号成功,username:{}，phone:{}", appUser.getUsername(), phone);

        // 绑定成功后，将手机号存到用户凭证表，后续可通过手机号+密码或者手机号+短信验证码登陆
        userCredentialsDao.save(new UserCredential(phone, CredentialType.PHONE.name(), userId));
    }

}

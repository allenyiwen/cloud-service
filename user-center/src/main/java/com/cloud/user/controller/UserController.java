package com.cloud.user.controller;

import com.cloud.common.utils.AppUserUtil;
import com.cloud.model.common.Page;
import com.cloud.model.log.LogAnnotation;
import com.cloud.model.user.AppUser;
import com.cloud.model.user.LoginAppUser;
import com.cloud.model.user.SysRole;
import com.cloud.user.feign.SmsClient;
import com.cloud.user.service.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private AppUserService appUserService;

    /**
     * 当前登录用户 LoginAppUser
     */
    @GetMapping("/users/current")
    public LoginAppUser getLoginAppUser() {
        return AppUserUtil.getLoginAppUser();
    }

    @GetMapping(value = "/users-anon/internal", params = "username")
    public LoginAppUser findByUsername(String username) {
        return appUserService.findByUsername(username);
    }

    /**
     * 用户查询
     *
     * @param params
     */
    @PreAuthorize("hasAuthority('back:user:query')")
    @GetMapping("/users")
    public Page<AppUser> findUsers(@RequestParam Map<String, Object> params) {
        return appUserService.findUsers(params);
    }

    @PreAuthorize("hasAuthority('back:user:query')")
    @GetMapping("/users/{id}")
    public AppUser findUserById(@PathVariable Long id) {
        return appUserService.findById(id);
    }

    /**
     * 添加用户,根据用户名注册
     *
     * @param appUser
     */
    @PostMapping("/users-anon/register")
    public AppUser register(@RequestBody AppUser appUser) {
        // 用户名等信息的判断逻辑挪到service了
        appUserService.addAppUser(appUser);

        return appUser;
    }

    /**
     * 修改自己的个人信息
     *
     * @param appUser
     */
    @LogAnnotation(module = "修改个人信息")
    @PutMapping("/users/me")
    public AppUser updateMe(@RequestBody AppUser appUser) {
        AppUser user = AppUserUtil.getLoginAppUser();
        appUser.setId(user.getId());

        appUserService.updateAppUser(appUser);

        return appUser;
    }

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @LogAnnotation(module = "修改密码")
    @PutMapping(value = "/users/password", params = {"oldPassword", "newPassword"})
    public void updatePassword(String oldPassword, String newPassword) {
        if (StringUtils.isBlank(oldPassword)) {
            throw new IllegalArgumentException("旧密码不能为空");
        }
        if (StringUtils.isBlank(newPassword)) {
            throw new IllegalArgumentException("新密码不能为空");
        }

        AppUser user = AppUserUtil.getLoginAppUser();
        appUserService.updatePassword(user.getId(), oldPassword, newPassword);
    }

    /**
     * 管理后台，给用户重置密码
     *
     * @param id          用户id
     * @param newPassword 新密码
     */
    @LogAnnotation(module = "重置密码")
    @PreAuthorize("hasAuthority('back:user:password')")
    @PutMapping(value = "/users/{id}/password", params = {"newPassword"})
    public void resetPassword(@PathVariable Long id, String newPassword) {
        appUserService.updatePassword(id, null, newPassword);
    }

    /**
     * 管理后台修改用户
     *
     * @param appUser
     */
    @LogAnnotation(module = "修改用户")
    @PreAuthorize("hasAuthority('back:user:update')")
    @PutMapping("/users")
    public void updateAppUser(@RequestBody AppUser appUser) {
        appUserService.updateAppUser(appUser);
    }

    /**
     * 管理后台给用户分配角色
     *
     * @param id      用户id
     * @param roleIds 角色ids
     */
    @LogAnnotation(module = "分配角色")
    @PreAuthorize("hasAuthority('back:user:role:set')")
    @PostMapping("/users/{id}/roles")
    public void setRoleToUser(@PathVariable Long id, @RequestBody Set<Long> roleIds) {
        appUserService.setRoleToUser(id, roleIds);
    }

    /**
     * 获取用户的角色
     *
     * @param id 用户id
     */
    @PreAuthorize("hasAnyAuthority('back:user:role:set','user:role:byuid')")
    @GetMapping("/users/{id}/roles")
    public Set<SysRole> findRolesByUserId(@PathVariable Long id) {
        return appUserService.findRolesByUserId(id);
    }

    @Autowired
    private SmsClient smsClient;

    /**
     * 绑定手机号
     *
     * @param phone
     * @param key
     * @param code
     */
    @LogAnnotation(module = "绑定手机号")
    @PostMapping(value = "/users/binding-phone")
    public void bindingPhone(String phone, String key, String code) {
        if (StringUtils.isBlank(phone)) {
            throw new IllegalArgumentException("手机号不能为空");
        }

        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("key不能为空");
        }

        if (StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("code不能为空");
        }

        LoginAppUser loginAppUser = AppUserUtil.getLoginAppUser();
        log.info("绑定手机号，key:{},code:{},username:{}", key, code, loginAppUser.getUsername());

        String value = smsClient.matcheCodeAndGetPhone(key, code, false, 30);
        if (value == null) {
            throw new IllegalArgumentException("验证码错误");
        }

        if (phone.equals(value)) {
            appUserService.bindingPhone(loginAppUser.getId(), phone);
        } else {
            throw new IllegalArgumentException("手机号不一致");
        }
    }
}

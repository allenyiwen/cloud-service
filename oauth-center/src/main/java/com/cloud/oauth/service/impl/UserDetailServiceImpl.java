package com.cloud.oauth.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cloud.model.user.LoginAppUser;
import com.cloud.model.user.constants.CredentialType;
import com.cloud.oauth.feign.SmsClient;
import com.cloud.oauth.feign.UserClient;

import lombok.extern.slf4j.Slf4j;

/**
 * 根据用户名获取用户<br>
 * <p>
 * 密码校验请看下面两个类
 *
 * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
 * @see org.springframework.security.authentication.dao.DaoAuthenticationProvider
 */
@Slf4j
@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

        @Autowired
        private UserClient userClient;
        @Autowired
        private BCryptPasswordEncoder passwordEncoder;
        @Autowired
        private SmsClient smsClient;

        @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 为了支持多类型登录，这里username后面拼装上登录类型,如username|type
        String[] params = username.split("\\|");
        username = params[0];// 真正的用户名

        LoginAppUser loginAppUser = userClient.findByUsername(username);
        if (loginAppUser == null) {
            throw new AuthenticationCredentialsNotFoundException("用户不存在");
        } else if (!loginAppUser.isEnabled()) {
            throw new DisabledException("用户已作废");
        }

        if (params.length > 1) {
            // 登录类型
            CredentialType credentialType = CredentialType.valueOf(params[1]);
            if (CredentialType.PHONE == credentialType) {// 短信登录
                handlerPhoneSmsLogin(loginAppUser, params);
            } else if (CredentialType.WECHAT_OPENID == credentialType) {// 微信登陆
                handlerWechatLogin(loginAppUser, params);
            }
        }

        return loginAppUser;
    }

    private void handlerWechatLogin(LoginAppUser loginAppUser, String[] params) {
        if (params.length < 3) {
            throw new IllegalArgumentException("非法请求");
        }

        String openid = params[0];
        String tempCode = params[2];

        userClient.wechatLoginCheck(tempCode, openid);

        // 其实这里是将密码重置，网关层的微信登录接口，密码也用同样规则即可
        loginAppUser.setPassword(passwordEncoder.encode(tempCode));
        log.info("微信登陆，{},{}", loginAppUser, openid);
    }

    /**
     * 手机号+短信验证码登陆，处理逻辑
     *
     * @param loginAppUser
     * @param params
     */
    private void handlerPhoneSmsLogin(LoginAppUser loginAppUser, String[] params) {
        if (params.length < 5) {
            throw new IllegalArgumentException("非法请求");
        }

        String phone = params[0];
        String key = params[2];
        String code = params[3];
        String md5 = params[4];
        if (!DigestUtils.md5Hex(key + code).equals(md5)) {
            throw new IllegalArgumentException("非法请求");
        }

        String value = smsClient.matcheCodeAndGetPhone(key, code, false, 30);
        if (!StringUtils.equals(phone, value)) {
            throw new IllegalArgumentException("验证码错误");
        }

        // 其实这里是将密码重置，网关层的短信登录接口，密码也用同样规则即可
        loginAppUser.setPassword(passwordEncoder.encode(phone));
        log.info("手机号+短信验证码登陆，{},{}", phone, code);
    }

}

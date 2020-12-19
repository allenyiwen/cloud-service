package com.cloud.user.service;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import com.cloud.model.user.AppUser;
import com.cloud.model.user.WechatUserInfo;

public interface WechatService {

    /**
     * 获取微信授权url
     *
     * @param app
     * @param request
     * @param toUrl
     * @return
     */
    String getWechatAuthorizeUrl(String app, HttpServletRequest request, String toUrl)
            throws UnsupportedEncodingException;

    /**
     * 获取微信个人用户信息
     *
     * @param app
     * @param request
     * @param code
     * @param state
     * @return
     */
    WechatUserInfo getWechatUserInfo(String app, HttpServletRequest request, String code, String state);

    String getToUrl(String toUrl, WechatUserInfo wechatUserInfo);

    void bindingUser(AppUser appUser, String tempCode, String openid);

    WechatUserInfo checkAndGetWechatUserInfo(String tempCode, String openid);
}

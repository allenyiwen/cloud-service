package com.cloud.model.log.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志模块定义<br>
 *
 * @author allen
 */
@Deprecated
public abstract class LogModule {

    public static final Map<String, String> MODULES = new HashMap<>();

    public static final String LOGIN = "LOGIN";
    public static final String LOGOUT = "LOGOUT";

    public static final String ADD_PERMISSION = "ADD_PERMISSION";
    public static final String UPDATE_PERMISSION = "UPDATE_PERMISSION";
    public static final String DELETE_PERMISSION = "DELETE_PERMISSION";

    public static final String ADD_ROLE = "ADD_ROLE";
    public static final String UPDATE_ROLE = "UPDATE_ROLE";
    public static final String DELETE_ROLE = "DELETE_ROLE";
    public static final String SET_PERMISSION = "SET_PERMISSION";

    public static final String SET_ROLE = "SET_ROLE";
    public static final String UPDATE_USER = "UPDATE_USER";
    public static final String UPDATE_ME = "UPDATE_ME";

    public static final String UPDATE_PASSWORD = "UPDATE_PASSWORD";
    public static final String RESET_PASSWORD = "RESET_PASSWORD";

    public static final String ADD_MENU = "ADD_MENU";
    public static final String UPDATE_MENU = "UPDATE_MENU";
    public static final String DELETE_MENU = "DELETE_MENU";
    public static final String SET_MENU_ROLE = "SET_MENU_ROLE";

    public static final String ADD_BLACK_IP = "ADD_BLACK_IP";
    public static final String DELETE_BLACK_IP = "DELETE_BLACK_IP";

    public static final String FILE_UPLOAD = "FILE_UPLOAD";
    public static final String FILE_DELETE = "FILE_DELETE";

    public static final String ADD_MAIL = "ADD_MAIL";
    public static final String UPDATE_MAIL = "UPDATE_MAIL";

    public static final String ADD_CLIENT = "ADD_CLIENT";
    public static final String UPDATE_CLIENT = "UPDATE_CLIENT";
    public static final String DELETE_CLIENT = "DELETE_CLIENT";
    public static final String RESET_PASSWORD_CLIENT = "RESET_PASSWORD_CLIENT";

    static {
        MODULES.put(LOGIN, "登陆");
        MODULES.put(LOGOUT, "退出");

        MODULES.put(ADD_PERMISSION, "添加权限");
        MODULES.put(UPDATE_PERMISSION, "修改权限");
        MODULES.put(DELETE_PERMISSION, "删除权限");

        MODULES.put(ADD_ROLE, "添加角色");
        MODULES.put(UPDATE_ROLE, "修改角色");
        MODULES.put(DELETE_ROLE, "删除角色");
        MODULES.put(SET_PERMISSION, "分配权限");
        MODULES.put(SET_ROLE, "分配角色");

        MODULES.put(UPDATE_USER, "修改用户");
        MODULES.put(UPDATE_ME, "修改个人信息");
        MODULES.put(UPDATE_PASSWORD, "修改密码");
        MODULES.put(RESET_PASSWORD, "重置密码");

        MODULES.put(ADD_MENU, "添加菜单");
        MODULES.put(UPDATE_MENU, "修改菜单");
        MODULES.put(DELETE_MENU, "删除菜单");
        MODULES.put(SET_MENU_ROLE, "分配菜单");

        MODULES.put(ADD_BLACK_IP, "添加黑名单");
        MODULES.put(DELETE_BLACK_IP, "删除黑名单");

        MODULES.put(FILE_UPLOAD, "文件上传");
        MODULES.put(FILE_DELETE, "文件删除");

        MODULES.put(ADD_MAIL, "保存邮件");
        MODULES.put(UPDATE_MAIL, "修改邮件");

        MODULES.put(ADD_CLIENT, "保存client");
        MODULES.put(UPDATE_CLIENT, "修改client");
        MODULES.put(DELETE_CLIENT, "删除client");
        MODULES.put(RESET_PASSWORD_CLIENT, "修改client密码");
    }

}

/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : cloud_user

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2019-06-17 09:47:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for app_user
-- ----------------------------
DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(60) NOT NULL COMMENT '密码',
  `nickname` varchar(255) DEFAULT NULL COMMENT '昵称',
  `headImgUrl` varchar(1024) DEFAULT NULL COMMENT '头像url',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态（1有效,0无效）',
  `type` varchar(16) NOT NULL COMMENT '类型（暂未用）',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of app_user
-- ----------------------------
INSERT INTO `app_user` VALUES ('1', 'admin', '$2a$10$3uOoX1ps14CxuotogUoDreW8zXJOZB9XeGdrC/xDV36hhaE8Rn9HO', '测试1', '', '', '1', '1', 'APP', '2019-01-17 16:57:01', '2019-01-17 16:57:01');
INSERT INTO `app_user` VALUES ('2', 'superadmin', '$2a$10$.gLkG0j2kM0stWoOvPBvqu0H9uSD0HUlpErI.PTKyZQkSUZIV2wFq', '超级管理员', null, null, '1', '1', 'BACKEND', '2019-01-19 20:30:11', '2019-01-19 20:30:11');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `permission` varchar(32) NOT NULL COMMENT '权限标识',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `permission` (`permission`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COMMENT='权限标识表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1', 'back:permission:save', '保存权限标识', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('2', 'back:permission:update', '修改权限标识', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('3', 'back:permission:delete', '删除权限标识', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('4', 'back:permission:query', '查询权限标识', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('5', 'back:role:save', '添加角色', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('6', 'back:role:update', '修改角色', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('7', 'back:role:delete', '删除角色', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('8', 'back:role:permission:set', '给角色分配权限', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('9', 'back:user:query', '用户查询', '2019-01-18 17:12:00', '2019-01-18 17:12:05');
INSERT INTO `sys_permission` VALUES ('10', 'back:user:update', '修改用户', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('11', 'back:user:role:set', '给用户分配角色', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('12', 'back:user:password', '用户重置密码', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('13', 'back:menu:save', '添加菜单', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('14', 'back:menu:update', '修改菜单', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('15', 'back:menu:delete', '删除菜单', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('16', 'back:menu:query', '查询菜单', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('17', 'back:menu:set2role', '给角色分配菜单', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('18', 'back:role:query', '查询角色', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('19', 'user:role:byuid', '获取用户的角色', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('20', 'role:permission:byroleid', '获取角色的权限', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('21', 'menu:byroleid', '获取角色的菜单', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('22', 'ip:black:query', '查询黑名单ip', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('23', 'ip:black:save', '添加黑名单ip', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('24', 'ip:black:delete', '删除黑名单ip', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('25', 'log:query', '日志查询', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('26', 'file:query', '文件查询', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('27', 'file:del', '文件删除', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('28', 'mail:save', '保存邮件', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('29', 'mail:update', '修改邮件', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('30', 'mail:query', '邮件查询', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('31', 'sms:query', '短信发送记录查询', '2019-01-18 17:06:39', '2019-01-18 17:06:42');
INSERT INTO `sys_permission` VALUES ('32', 'client:save', '保存client', '2019-06-28 17:06:39', '2019-06-28 17:06:39');
INSERT INTO `sys_permission` VALUES ('33', 'client:update', '修改client', '2019-06-28 17:06:39', '2019-06-28 17:06:39');
INSERT INTO `sys_permission` VALUES ('34', 'client:query', '查询client', '2019-06-28 17:06:39', '2019-06-28 17:06:39');
INSERT INTO `sys_permission` VALUES ('35', 'client:del', '删除client', '2019-06-28 17:06:39', '2019-06-28 17:06:39');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `code` varchar(32) NOT NULL COMMENT '角色code',
  `name` varchar(50) NOT NULL COMMENT '角色名',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', 'SUPER_ADMIN', '超级管理员', '2019-01-19 20:32:16', '2019-01-19 20:32:18');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `roleId` int(11) NOT NULL COMMENT '角色id',
  `permissionId` int(11) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`roleId`,`permissionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关系表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('1', '1');
INSERT INTO `sys_role_permission` VALUES ('1', '2');
INSERT INTO `sys_role_permission` VALUES ('1', '3');
INSERT INTO `sys_role_permission` VALUES ('1', '4');
INSERT INTO `sys_role_permission` VALUES ('1', '5');
INSERT INTO `sys_role_permission` VALUES ('1', '6');
INSERT INTO `sys_role_permission` VALUES ('1', '7');
INSERT INTO `sys_role_permission` VALUES ('1', '8');
INSERT INTO `sys_role_permission` VALUES ('1', '9');
INSERT INTO `sys_role_permission` VALUES ('1', '10');
INSERT INTO `sys_role_permission` VALUES ('1', '11');
INSERT INTO `sys_role_permission` VALUES ('1', '12');
INSERT INTO `sys_role_permission` VALUES ('1', '13');
INSERT INTO `sys_role_permission` VALUES ('1', '14');
INSERT INTO `sys_role_permission` VALUES ('1', '15');
INSERT INTO `sys_role_permission` VALUES ('1', '16');
INSERT INTO `sys_role_permission` VALUES ('1', '17');
INSERT INTO `sys_role_permission` VALUES ('1', '18');
INSERT INTO `sys_role_permission` VALUES ('1', '19');
INSERT INTO `sys_role_permission` VALUES ('1', '20');
INSERT INTO `sys_role_permission` VALUES ('1', '21');
INSERT INTO `sys_role_permission` VALUES ('1', '22');
INSERT INTO `sys_role_permission` VALUES ('1', '23');
INSERT INTO `sys_role_permission` VALUES ('1', '24');
INSERT INTO `sys_role_permission` VALUES ('1', '25');
INSERT INTO `sys_role_permission` VALUES ('1', '26');
INSERT INTO `sys_role_permission` VALUES ('1', '27');
INSERT INTO `sys_role_permission` VALUES ('1', '28');
INSERT INTO `sys_role_permission` VALUES ('1', '29');
INSERT INTO `sys_role_permission` VALUES ('1', '30');
INSERT INTO `sys_role_permission` VALUES ('1', '31');
INSERT INTO `sys_role_permission` VALUES ('1', '32');
INSERT INTO `sys_role_permission` VALUES ('1', '33');
INSERT INTO `sys_role_permission` VALUES ('1', '34');
INSERT INTO `sys_role_permission` VALUES ('1', '35');

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `userId` int(11) NOT NULL COMMENT '用户id',
  `roleId` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`userId`,`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色用户关系表';

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES ('1', '1');
INSERT INTO `sys_role_user` VALUES ('2', '1');

-- ----------------------------
-- Table structure for t_wechat
-- ----------------------------
DROP TABLE IF EXISTS `t_wechat`;
CREATE TABLE `t_wechat` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `openid` varchar(128) NOT NULL COMMENT '微信openid',
  `unionid` varchar(128) DEFAULT NULL COMMENT '微信unionid',
  `userId` int(11) DEFAULT NULL COMMENT '绑定用户的id',
  `app` varchar(32) NOT NULL COMMENT '公众号标识',
  `nickname` varchar(64) NOT NULL COMMENT '微信昵称',
  `sex` varchar(16) DEFAULT NULL COMMENT '微信返回的性别',
  `province` varchar(64) DEFAULT NULL COMMENT '微信返回的省',
  `city` varchar(64) DEFAULT NULL COMMENT '微信返回的城市',
  `country` varchar(64) DEFAULT NULL COMMENT '微信返回的国家',
  `headimgurl` varchar(1024) DEFAULT NULL COMMENT '微信头像',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `openid` (`openid`),
  KEY `userId` (`userId`),
  KEY `unionid` (`unionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信信息表';

-- ----------------------------
-- Records of t_wechat
-- ----------------------------

-- ----------------------------
-- Table structure for user_credentials
-- ----------------------------
DROP TABLE IF EXISTS `user_credentials`;
CREATE TABLE `user_credentials` (
  `username` varchar(50) NOT NULL COMMENT '用户名或手机号等',
  `type` varchar(16) NOT NULL COMMENT '账号类型（用户名、手机号）',
  `userId` int(11) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`username`),
  KEY `userId` (`userId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户凭证表';

-- ----------------------------
-- Records of user_credentials
-- ----------------------------
INSERT INTO `user_credentials` VALUES ('admin', 'USERNAME', '1');
INSERT INTO `user_credentials` VALUES ('superadmin', 'USERNAME', '2');

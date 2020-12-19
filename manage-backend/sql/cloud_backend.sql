/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : cloud_backend

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2019-06-17 09:59:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for black_ip
-- ----------------------------
DROP TABLE IF EXISTS `black_ip`;
CREATE TABLE `black_ip` (
  `ip` varchar(32) NOT NULL COMMENT '黑名单ip',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`ip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ip黑名单表';

-- ----------------------------
-- Records of black_ip
-- ----------------------------

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `parentId` int(11) NOT NULL COMMENT '父菜单id',
  `name` varchar(64) NOT NULL COMMENT '菜单名',
  `url` varchar(1024) DEFAULT NULL COMMENT '菜单url',
  `css` varchar(32) DEFAULT NULL COMMENT '菜单样式',
  `sort` int(11) NOT NULL COMMENT '排序',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '0', '系统设置', '', 'fa-gears', '1', '2019-01-23 10:20:30', '2019-01-23 10:20:31');
INSERT INTO `menu` VALUES ('2', '1', '菜单', 'pages/menu/menuList.html', 'fa-windows', '2', '2019-01-23 14:04:40', '2019-01-23 14:04:43');
INSERT INTO `menu` VALUES ('3', '1', '角色', 'pages/role/roleList.html', 'fa-cubes', '3', '2019-01-23 14:04:40', '2019-01-23 14:04:43');
INSERT INTO `menu` VALUES ('4', '1', '权限', 'pages/permission/permissionList.html', 'fa-align-justify', '4', '2019-01-23 14:04:40', '2019-01-23 14:04:43');
INSERT INTO `menu` VALUES ('5', '0', '用户管理', '', 'fa-user', '4', '2019-01-23 14:04:40', '2019-01-23 14:04:43');
INSERT INTO `menu` VALUES ('6', '5', '用户查询', 'pages/user/userList.html', 'fa-user', '4', '2019-01-23 14:04:40', '2019-01-23 14:04:43');
INSERT INTO `menu` VALUES ('7', '0', '文件查询', 'pages/file/fileList.html', 'fa-folder-open', '5', '2019-01-23 14:04:40', '2019-01-23 14:04:43');
INSERT INTO `menu` VALUES ('8', '0', '邮件管理', 'pages/mail/mailList.html', 'fa-envelope', '6', '2019-01-23 14:04:40', '2019-01-23 14:04:43');
INSERT INTO `menu` VALUES ('9', '0', '注册中心', 'http://local.register.com:8761', 'fa-institution', '7', '2019-01-23 14:04:40', '2019-01-23 14:04:43');
INSERT INTO `menu` VALUES ('10', '0', '监控中心', 'http://local.monitor.com:9001', 'fa-spinner', '8', '2019-01-23 14:04:40', '2019-01-23 14:04:43');
INSERT INTO `menu` VALUES ('11', '0', 'swagger文档', 'pages/swagger/api-doc.html', 'fa-file-pdf-o', '8', '2019-01-23 14:04:40', '2019-01-23 14:04:43');
INSERT INTO `menu` VALUES ('12', '0', '黑名单ip', 'pages/blackIP/blackIPList.html', 'fa-child', '9', '2019-01-23 14:04:40', '2019-01-23 14:04:43');
INSERT INTO `menu` VALUES ('13', '0', '日志查询', 'pages/log/logList.html', 'fa-reorder', '10', '2019-01-23 14:04:40', '2019-01-23 14:04:43');
INSERT INTO `menu` VALUES ('14', '0', '短信历史查询', 'pages/sms/smsList.html', 'fa-reorder', '11', '2019-01-23 14:04:40', '2019-01-23 14:04:43');
INSERT INTO `menu` VALUES ('15', '1', 'client管理', 'pages/client/clientList.html', 'fa-tachometer',  '13', '2019-01-23 14:04:40', '2019-01-23 14:04:43');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu` (
  `roleId` int(11) NOT NULL COMMENT '角色id',
  `menuId` int(11) NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`roleId`,`menuId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关系表';

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES ('1', '1');
INSERT INTO `role_menu` VALUES ('1', '2');
INSERT INTO `role_menu` VALUES ('1', '3');
INSERT INTO `role_menu` VALUES ('1', '4');
INSERT INTO `role_menu` VALUES ('1', '5');
INSERT INTO `role_menu` VALUES ('1', '6');
INSERT INTO `role_menu` VALUES ('1', '7');
INSERT INTO `role_menu` VALUES ('1', '8');
INSERT INTO `role_menu` VALUES ('1', '9');
INSERT INTO `role_menu` VALUES ('1', '10');
INSERT INTO `role_menu` VALUES ('1', '11');
INSERT INTO `role_menu` VALUES ('1', '12');
INSERT INTO `role_menu` VALUES ('1', '13');
INSERT INTO `role_menu` VALUES ('1', '14');
INSERT INTO `role_menu` VALUES ('1', '15');

-- ----------------------------
-- Table structure for t_mail
-- ----------------------------
DROP TABLE IF EXISTS `t_mail`;
CREATE TABLE `t_mail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `userId` int(11) NOT NULL COMMENT '发送人id',
  `username` varchar(50) NOT NULL COMMENT '发送人用户名',
  `toEmail` varchar(128) NOT NULL COMMENT '收件人邮件地址',
  `subject` varchar(255) NOT NULL COMMENT '标题',
  `content` longtext NOT NULL COMMENT '正文',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0草稿，1成功，2失败',
  `sendTime` datetime DEFAULT NULL COMMENT '发送时间',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`),
  KEY `updateTime` (`updateTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邮件发送记录表';

-- ----------------------------
-- Records of t_mail
-- ----------------------------

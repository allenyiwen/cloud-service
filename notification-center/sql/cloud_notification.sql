/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : cloud_notification

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2019-06-17 10:04:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_sms
-- ----------------------------
DROP TABLE IF EXISTS `t_sms`;
CREATE TABLE `t_sms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(16) NOT NULL COMMENT '手机号码',
  `signName` varchar(128) DEFAULT NULL COMMENT '阿里云短信签名',
  `templateCode` varchar(128) DEFAULT NULL COMMENT '阿里云模板code',
  `params` varchar(500) DEFAULT NULL COMMENT '参数',
  `bizId` varchar(128) DEFAULT NULL COMMENT '阿里云返回的',
  `code` varchar(64) DEFAULT NULL COMMENT '阿里云返回的code',
  `message` varchar(128) DEFAULT NULL COMMENT '阿里云返回的',
  `day` date NOT NULL COMMENT '日期（冗余字段,便于统计某天的发送次数）',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `phone` (`phone`),
  KEY `day` (`day`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发短信记录表';

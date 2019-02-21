/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80011
Source Host           : localhost:3306
Source Database       : sike_web

Target Server Type    : MYSQL
Target Server Version : 80011
File Encoding         : 65001

Date: 2019-02-16 15:59:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(50) NOT NULL COMMENT '角色id',
  `name` varchar(255) DEFAULT NULL COMMENT '角色名',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `role_type` smallint(1) DEFAULT '1' COMMENT '角色类型(0:系统内置角色，1：业务角色)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('8d0b8d7ee5b44d479423d9cf2f09da5b', 'normal', '普通用户', '0');
INSERT INTO `sys_role` VALUES ('e57d0e8e7d9c497c8909d03436fb69d0', 'admin', '系统管理员', '0');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `portrait` text COMMENT '用户头像 存储图片base64',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `salt` varchar(50) NOT NULL COMMENT '盐，用于密码加密',
  `phone` varchar(15) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `register_time` timestamp NULL DEFAULT NULL COMMENT '注册时间',
  `user_state` smallint(1) NOT NULL DEFAULT '0' COMMENT '用户状态（1：启用，0：禁用）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('f1347d3259e941238517cd84ea2ed6b6', 'admin', '系统管理员', null, '3af6b3c37616d89a5eda9588f52a6997', 'a43c7fc10bf54216a2f2ee1e0937d09a', null, null, null, '1');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL COMMENT '用户id',
  `role_id` varchar(50) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户角色关系表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', 'f1347d3259e941238517cd84ea2ed6b6', 'e57d0e8e7d9c497c8909d03436fb69d0');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色id',
  `permission` varchar(100) NOT NULL COMMENT '权限代码（接口预定义好）',
  `application` varchar(100) NOT NULL COMMENT '权限所属应用名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色权限表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('1', '8d0b8d7ee5b44d479423d9cf2f09da5b', 'user.list', 'controller');
INSERT INTO `sys_role_permission` VALUES ('2', '8d0b8d7ee5b44d479423d9cf2f09da5b', 'user.logout', 'controller');
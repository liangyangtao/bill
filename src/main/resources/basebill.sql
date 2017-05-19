/*
Navicat MySQL Data Transfer

Source Server         : localhost_spider
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : licai

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2017-05-19 19:56:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for basebill
-- ----------------------------
DROP TABLE IF EXISTS `basebill`;
CREATE TABLE `basebill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_bank` varchar(255) DEFAULT NULL COMMENT '发行银行',
  `product_stats` varchar(255) DEFAULT NULL COMMENT '产品状态',
  `product_limit` varchar(255) DEFAULT NULL COMMENT '期限',
  `product_income_type` varchar(255) DEFAULT NULL COMMENT '收益类型',
  `product_currency` varchar(255) DEFAULT NULL COMMENT '币种',
  `product_purchasse_amount` varchar(255) DEFAULT NULL COMMENT '起购金额',
  `product_type` varchar(255) DEFAULT NULL COMMENT '产品类型',
  `product_name` varchar(255) DEFAULT NULL COMMENT '产品名称',
  `product_code` varchar(255) DEFAULT NULL COMMENT '产品代码',
  `product_start` varchar(255) DEFAULT NULL COMMENT '起售日期',
  `product_end` varchar(255) DEFAULT NULL COMMENT '截至购买日',
  `product_expire` varchar(255) DEFAULT NULL COMMENT '到期日期',
  `product_anticipated` varchar(255) DEFAULT NULL COMMENT '预计收益率',
  `product_invest_type` varchar(255) DEFAULT NULL COMMENT '投资类型',
  `product_risk_type` varchar(255) DEFAULT NULL COMMENT '风险评级',
  `product_channel` varchar(255) DEFAULT NULL COMMENT '销售渠道',
  `product_area` varchar(255) DEFAULT NULL COMMENT '发行区域',
  `unmd` varchar(255) DEFAULT NULL COMMENT '唯一标识',
  `uptime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
SQLyog Ultimate v11.25 (64 bit)
MySQL - 8.0.15 : Database - rcs3
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`rcs3` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `rcs3`;

/*Table structure for table `QRTZ_BLOB_TRIGGERS` */

DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;

CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `QRTZ_CALENDARS` */

DROP TABLE IF EXISTS `QRTZ_CALENDARS`;

CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `QRTZ_CRON_TRIGGERS` */

DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;

CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `QRTZ_FIRED_TRIGGERS` */

DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;

CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `QRTZ_JOB_DETAILS` */

DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;

CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `QRTZ_LOCKS` */

DROP TABLE IF EXISTS `QRTZ_LOCKS`;

CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `QRTZ_PAUSED_TRIGGER_GRPS` */

DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;

CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `QRTZ_SCHEDULER_STATE` */

DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;

CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `QRTZ_SIMPLE_TRIGGERS` */

DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;

CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `QRTZ_SIMPROP_TRIGGERS` */

DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;

CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `QRTZ_TRIGGERS` */

DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;

CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `ab_job` */

DROP TABLE IF EXISTS `ab_job`;

CREATE TABLE `ab_job` (
  `job_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '任务编号',
  `job_name` varchar(50) DEFAULT NULL COMMENT '任务名称',
  `goal_a` int(11) DEFAULT NULL COMMENT '目标A编号',
  `goal_b` int(11) DEFAULT NULL COMMENT '目标B编号',
  `job_status` tinyint(1) DEFAULT NULL COMMENT '任务状态(1:运行中；0:暂停；2:完成)',
  `scale_a` int(11) DEFAULT NULL COMMENT 'A占比',
  `scale_b` int(11) DEFAULT NULL COMMENT 'B占比',
  `event_type` tinyint(1) DEFAULT NULL COMMENT '进件类型（1:测试进件；0:api进件）',
  `line_type` tinyint(1) DEFAULT NULL COMMENT '0:实时；1:离线',
  `rule_type` tinyint(1) DEFAULT NULL COMMENT '进件类型（0:规则集；1:决策集；2:模型）',
  `entry_type` int(11) DEFAULT NULL COMMENT '单批类型(0:单次；1:批量)',
  `bussiness_type` int(11) DEFAULT NULL COMMENT '业务类型',
  `scene_type` int(1) DEFAULT NULL COMMENT '场景类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '客户编号',
  `parameters` text COMMENT '参数集',
  `bean_name` varchar(50) DEFAULT NULL COMMENT '类名',
  `method_name` varchar(50) DEFAULT NULL COMMENT '方法名',
  `cron_expression` varchar(50) DEFAULT NULL COMMENT '表达式',
  `task_count` int(11) DEFAULT NULL COMMENT '名单数量',
  `completed_count` int(11) DEFAULT '0' COMMENT '已执行数量',
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=utf8;

/*Table structure for table `ab_task_0_part` */

DROP TABLE IF EXISTS `ab_task_0_part`;

CREATE TABLE `ab_task_0_part` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `id_number` varchar(20) DEFAULT NULL COMMENT '身份证',
  `cid` varchar(50) DEFAULT NULL COMMENT '手机号',
  `job_id` int(11) DEFAULT NULL COMMENT '任务编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `task_status` tinyint(1) DEFAULT '0' COMMENT '执行状态（0:未执行;1:监控中;2完成;）',
  `distribute_status` tinyint(1) DEFAULT '0' COMMENT '分发状态',
  `update_time` datetime DEFAULT '2018-01-01 00:00:00' COMMENT '更新时间',
  `result_map` text COMMENT '查询结果',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `user_id` bigint(11) DEFAULT NULL COMMENT '客户标识',
  `execute_status` int(11) DEFAULT NULL COMMENT '上次执行状态,1:通过；2：人工审核；3：拒绝；4：失效',
  `goal_id` int(11) DEFAULT NULL COMMENT '目标编号（规则集|决策集|模型）',
  `parms_json` varchar(255) DEFAULT NULL COMMENT '参数集',
  `role` varchar(10) DEFAULT NULL COMMENT '区分AB',
  PRIMARY KEY (`id`),
  KEY `gather_index` (`job_id`,`user_id`,`execute_status`,`task_status`,`distribute_status`)
) ENGINE=InnoDB AUTO_INCREMENT=10511 DEFAULT CHARSET=utf8;

/*Table structure for table `ab_task_39_part` */

DROP TABLE IF EXISTS `ab_task_39_part`;

CREATE TABLE `ab_task_39_part` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `id_number` varchar(20) DEFAULT NULL COMMENT '身份证',
  `cid` varchar(50) DEFAULT NULL COMMENT '手机号',
  `job_id` int(11) DEFAULT NULL COMMENT '任务编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `task_status` tinyint(1) DEFAULT '0' COMMENT '执行状态（0:未执行;1:监控中;2完成;）',
  `distribute_status` tinyint(1) DEFAULT '0' COMMENT '分发状态',
  `update_time` datetime DEFAULT '2018-01-01 00:00:00' COMMENT '更新时间',
  `result_map` text COMMENT '查询结果',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `user_id` bigint(11) DEFAULT NULL COMMENT '客户标识',
  `execute_status` int(11) DEFAULT NULL COMMENT '上次执行状态,1:通过；2：人工审核；3：拒绝；4：失效',
  `goal_id` int(11) DEFAULT NULL COMMENT '目标编号（规则集|决策集|模型）',
  `parms_json` text COMMENT '参数集',
  `role` varchar(10) DEFAULT NULL COMMENT '区分AB',
  PRIMARY KEY (`id`),
  KEY `gather_index` (`job_id`,`user_id`,`execute_status`,`task_status`,`distribute_status`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

/*Table structure for table `api_event_entry` */

DROP TABLE IF EXISTS `api_event_entry`;

CREATE TABLE `api_event_entry` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '进件id（主键）',
  `channel` varchar(50) DEFAULT NULL COMMENT '访问渠道（Web、Android、ios...）',
  `business_id` bigint(20) DEFAULT NULL COMMENT '业务类型id',
  `sence_id` bigint(20) DEFAULT NULL COMMENT '场景id',
  `rules_id` bigint(20) DEFAULT NULL COMMENT '规则集id',
  `rules_name` varchar(100) DEFAULT NULL COMMENT '规则集name',
  `user_id` bigint(20) DEFAULT NULL COMMENT '账户id',
  `nickname` varchar(50) DEFAULT NULL COMMENT '账户name(昵称)',
  `user_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `id_card` varchar(50) DEFAULT NULL COMMENT '身份证号',
  `acct_no` varchar(50) DEFAULT NULL COMMENT '银行卡号',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
  `approver_id` bigint(20) DEFAULT NULL COMMENT '审核人id',
  `approver_name` varchar(50) DEFAULT NULL COMMENT '审核人name',
  `sys_approval_time` datetime DEFAULT NULL COMMENT '系统审核日期',
  `man_approval_time` datetime DEFAULT NULL COMMENT '人工审核日期',
  `sys_status` tinyint(4) DEFAULT '0' COMMENT '系统审核结果(0:"-",1:"通过",2:"人工审核",3:"拒绝",4:"失效")',
  `man_status` tinyint(4) DEFAULT '0' COMMENT '人工审核结果(0:"-",1:"通过",2:"拒绝")',
  `status` tinyint(4) DEFAULT '0' COMMENT '审核状态(0:"未审核",1:"已审核")',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `result_map` longtext,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `province` varchar(10) DEFAULT NULL COMMENT '省份',
  `type` int(11) DEFAULT '0' COMMENT '1测试or0正常进件',
  `city` varchar(20) DEFAULT NULL COMMENT '市区',
  `isp` varchar(10) DEFAULT NULL COMMENT '运营商',
  `source_time` int(11) DEFAULT NULL COMMENT '数据源耗时',
  `expend_time` int(11) DEFAULT NULL COMMENT '进件耗时',
  `engine_time` int(11) DEFAULT NULL COMMENT '规则引擎耗时',
  `else_time` int(11) DEFAULT NULL COMMENT '预留',
  `score` int(11) DEFAULT NULL COMMENT '分数',
  PRIMARY KEY (`id`),
  KEY `index_userId` (`user_id`),
  KEY `index_createTime` (`create_time`),
  KEY `index_remark` (`remark`)
) ENGINE=InnoDB AUTO_INCREMENT=4622280 DEFAULT CHARSET=utf8;

/*Table structure for table `api_model_score_log` */

DROP TABLE IF EXISTS `api_model_score_log`;

CREATE TABLE `api_model_score_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `inter_id` bigint(20) DEFAULT NULL COMMENT '接口id',
  `inter_name` varchar(20) DEFAULT NULL COMMENT '接口名称',
  `channel` varchar(20) DEFAULT NULL COMMENT '调用渠道',
  `model_name` varchar(20) DEFAULT NULL COMMENT '模型名称',
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `score_time` datetime DEFAULT NULL COMMENT '评分时间',
  `score` int(10) DEFAULT NULL COMMENT '得分',
  `user_id` bigint(20) DEFAULT NULL COMMENT '运行人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9897 DEFAULT CHARSET=utf8;

/*Table structure for table `api_user_token` */

DROP TABLE IF EXISTS `api_user_token`;

CREATE TABLE `api_user_token` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'api用户token关联编号',
  `user_id` bigint(11) DEFAULT NULL COMMENT '用户编号',
  `token` varchar(255) DEFAULT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

/*Table structure for table `approval_action_type` */

DROP TABLE IF EXISTS `approval_action_type`;

CREATE TABLE `approval_action_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '审批类型编号',
  `action_name` varchar(20) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Table structure for table `approval_manage` */

DROP TABLE IF EXISTS `approval_manage`;

CREATE TABLE `approval_manage` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '审批编号',
  `action_type` int(11) DEFAULT NULL COMMENT '操作类型',
  `submitter` varchar(50) DEFAULT NULL COMMENT '提交人',
  `sub_time` datetime DEFAULT NULL COMMENT '提交时间',
  `obj_id` int(50) DEFAULT NULL COMMENT '对象名称',
  `app_status` int(11) DEFAULT '0' COMMENT '审批状态（0:未审批，1:正在审批，2:审批通过 3:审批失败）',
  `threshold` int(11) DEFAULT NULL COMMENT '阈值',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `scene` varchar(255) DEFAULT NULL COMMENT '适用场景',
  `activate` int(5) DEFAULT NULL COMMENT '是否激活',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '与客户关联',
  `business_id` int(11) DEFAULT NULL COMMENT '业务类型',
  `app_person` varchar(20) DEFAULT NULL COMMENT '审批人',
  `app_time` datetime DEFAULT NULL COMMENT '审批时间',
  `only_id` bigint(20) DEFAULT NULL COMMENT '自身编号',
  `threshold_min` int(11) DEFAULT NULL COMMENT '最小值',
  `threshold_max` int(11) DEFAULT NULL COMMENT '最大值',
  `record_id` bigint(20) DEFAULT NULL COMMENT '记录历史表的编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1106 DEFAULT CHARSET=utf8;

/*Table structure for table `approval_object_type` */

DROP TABLE IF EXISTS `approval_object_type`;

CREATE TABLE `approval_object_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '审批对象编号',
  `obj_name` varchar(20) DEFAULT NULL COMMENT '对象名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Table structure for table `batch_event_overview` */

DROP TABLE IF EXISTS `batch_event_overview`;

CREATE TABLE `batch_event_overview` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(64) NOT NULL,
  `file_size` varchar(64) NOT NULL,
  `count` bigint(20) NOT NULL,
  `pass_count` bigint(20) DEFAULT '0',
  `refuse_count` bigint(20) DEFAULT '0',
  `audit_count` bigint(20) DEFAULT '0',
  `error_count` bigint(20) DEFAULT '0',
  `status` int(11) DEFAULT NULL,
  `batch_no` bigint(20) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unique_code` bigint(20) NOT NULL,
  `goal_ids` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '目标id集合',
  `title_map` text,
  `type` int(1) DEFAULT NULL COMMENT '类型 1：规则集 2：决策',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=utf8;

/*Table structure for table `datapool_0_part` */

DROP TABLE IF EXISTS `datapool_0_part`;

CREATE TABLE `datapool_0_part` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '存储ID',
  `rid` varchar(32) NOT NULL DEFAULT '' COMMENT '数据全局ID',
  `name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `cid` varchar(20) DEFAULT NULL COMMENT '手机号',
  `id_number` varchar(32) DEFAULT NULL COMMENT '身份证号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `rid` (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `datapool_3_part` */

DROP TABLE IF EXISTS `datapool_3_part`;

CREATE TABLE `datapool_3_part` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '存储ID',
  `rid` varchar(32) NOT NULL DEFAULT '' COMMENT '数据全局ID',
  `name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `cid` varchar(20) DEFAULT NULL COMMENT '手机号',
  `id_number` varchar(32) DEFAULT NULL COMMENT '身份证号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `rid` (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `datapool_4_part` */

DROP TABLE IF EXISTS `datapool_4_part`;

CREATE TABLE `datapool_4_part` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '存储ID',
  `rid` varchar(32) NOT NULL DEFAULT '' COMMENT '数据全局ID',
  `name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `cid` varchar(20) DEFAULT NULL COMMENT '手机号',
  `id_number` varchar(32) DEFAULT NULL COMMENT '身份证号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `rid` (`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `datapool_detail_0_part` */

DROP TABLE IF EXISTS `datapool_detail_0_part`;

CREATE TABLE `datapool_detail_0_part` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '存储ID',
  `rid` varchar(32) NOT NULL DEFAULT '' COMMENT '数据全局ID',
  `dimension` varchar(20) DEFAULT NULL COMMENT '监控维度',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `query_times` int(20) DEFAULT NULL COMMENT '查询次数',
  `update_times` int(20) DEFAULT NULL COMMENT '更新次数',
  `query_status` int(11) DEFAULT NULL COMMENT '查询状态',
  `update_status` int(11) DEFAULT NULL COMMENT '更新状态',
  `min_interval` int(11) DEFAULT NULL COMMENT '最小分发间隔',
  `max_interval` int(11) DEFAULT NULL COMMENT '最大分发间隔',
  `recent_data` text COMMENT '最近更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_rid_dimension` (`rid`,`dimension`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `datapool_detail_3_part` */

DROP TABLE IF EXISTS `datapool_detail_3_part`;

CREATE TABLE `datapool_detail_3_part` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '存储ID',
  `rid` varchar(32) NOT NULL DEFAULT '' COMMENT '数据全局ID',
  `dimension` varchar(20) DEFAULT NULL COMMENT '监控维度',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `query_times` int(20) DEFAULT NULL COMMENT '查询次数',
  `update_times` int(20) DEFAULT NULL COMMENT '更新次数',
  `query_status` int(11) DEFAULT NULL COMMENT '查询状态',
  `update_status` int(11) DEFAULT NULL COMMENT '更新状态',
  `min_interval` int(11) DEFAULT NULL COMMENT '最小分发间隔',
  `max_interval` int(11) DEFAULT NULL COMMENT '最大分发间隔',
  `recent_data` text COMMENT '最近更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_rid_dimension` (`rid`,`dimension`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `datapool_detail_4_part` */

DROP TABLE IF EXISTS `datapool_detail_4_part`;

CREATE TABLE `datapool_detail_4_part` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '存储ID',
  `rid` varchar(32) NOT NULL DEFAULT '' COMMENT '数据全局ID',
  `dimension` varchar(20) DEFAULT NULL COMMENT '监控维度',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `query_times` int(20) DEFAULT NULL COMMENT '查询次数',
  `update_times` int(20) DEFAULT NULL COMMENT '更新次数',
  `query_status` int(11) DEFAULT NULL COMMENT '查询状态',
  `update_status` int(11) DEFAULT NULL COMMENT '更新状态',
  `min_interval` int(11) DEFAULT NULL COMMENT '最小分发间隔',
  `max_interval` int(11) DEFAULT NULL COMMENT '最大分发间隔',
  `recent_data` text COMMENT '最近更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_rid_dimension` (`rid`,`dimension`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `decision_event` */

DROP TABLE IF EXISTS `decision_event`;

CREATE TABLE `decision_event` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `channel` varchar(255) DEFAULT NULL COMMENT '访问渠道（Web、Android、ios...）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `result_map` text COMMENT '决策集',
  `expend_time` int(11) DEFAULT NULL COMMENT '决策流耗时',
  `user_id` bigint(11) DEFAULT NULL COMMENT '关联客户编号',
  `app_status` int(11) DEFAULT '0' COMMENT '审批状态',
  `sys_status` int(11) DEFAULT '0' COMMENT '系统审核状态',
  `decision_id` int(11) DEFAULT NULL COMMENT '决策集编号',
  `real_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证',
  `add_user` varchar(50) DEFAULT NULL COMMENT '进件人',
  `business_id` int(11) DEFAULT NULL COMMENT '业务类型',
  `scene_id` int(11) DEFAULT NULL COMMENT '场景',
  `score` int(11) DEFAULT NULL COMMENT '分数',
  `excute_flow` text COMMENT '执行流程',
  `parameters` text COMMENT '参数集',
  `remark` varchar(255) DEFAULT NULL COMMENT '批次标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=110406 DEFAULT CHARSET=utf8;

/*Table structure for table `decision_history_log` */

DROP TABLE IF EXISTS `decision_history_log`;

CREATE TABLE `decision_history_log` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `decision_map` longtext COMMENT '决策集',
  `decision_id` int(11) DEFAULT NULL COMMENT '决策编号',
  `decision_name` varchar(50) DEFAULT NULL COMMENT '决策名称',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '用户编号',
  `record_time` datetime DEFAULT NULL COMMENT '记录时间',
  `action_type` int(11) DEFAULT NULL COMMENT '操作类型',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `version` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '版本号',
  `last_version` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '上次版本号',
  `submitter` varchar(50) DEFAULT NULL COMMENT '添加人',
  `app_status` tinyint(1) DEFAULT '0' COMMENT '审批状态（0:未审批，1:正在审批，2:审批通过 3:审批失败 4:失效 5:撤回）',
  `approval_id` int(11) DEFAULT NULL COMMENT '审批编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=286 DEFAULT CHARSET=utf8;

/*Table structure for table `decision_model` */

DROP TABLE IF EXISTS `decision_model`;

CREATE TABLE `decision_model` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '决策集编号',
  `name` varchar(50) DEFAULT NULL COMMENT '决策名称',
  `decision_flow` text COMMENT '决策流',
  `scene_id` int(11) DEFAULT NULL COMMENT '场景编号',
  `business_id` int(11) DEFAULT NULL COMMENT '业务编号',
  `creater` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `parameters` text COMMENT '参数集',
  `app_status` int(255) DEFAULT '0' COMMENT '审批状态（0:未审批，1:审批中，2:审批成功，3:审批失败）',
  `active_status` int(255) DEFAULT '0' COMMENT '激活状态（0:未激活，1:已激活）',
  `status` int(255) DEFAULT '0' COMMENT '自身状态',
  `user_id` bigint(11) DEFAULT NULL COMMENT '关联客户编号',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `used_rules_ids` varchar(255) DEFAULT NULL COMMENT '用到的规则集编号',
  `used_model_ids` varchar(255) DEFAULT NULL COMMENT '用到的模型',
  `usage_rules_ids` varchar(255) DEFAULT NULL COMMENT '使用到的规则集数组',
  `usage_model_ids` varchar(255) DEFAULT NULL COMMENT '决策流使用到的模型',
  `recommend` varchar(255) DEFAULT NULL COMMENT '1推荐额度；2推荐分期；3推荐利率',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=157 DEFAULT CHARSET=utf8;

/*Table structure for table `decision_model_online` */

DROP TABLE IF EXISTS `decision_model_online`;

CREATE TABLE `decision_model_online` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '决策集编号',
  `name` varchar(50) DEFAULT NULL COMMENT '决策名称',
  `decision_flow` text COMMENT '决策流',
  `scene_id` int(11) DEFAULT NULL COMMENT '场景编号',
  `business_id` int(11) DEFAULT NULL COMMENT '业务编号',
  `creater` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `parameters` text COMMENT '参数集',
  `app_status` int(255) DEFAULT '0' COMMENT '审批状态（0:未审批，1:审批中，2:审批成功，3:审批失败）',
  `active_status` int(255) DEFAULT '0' COMMENT '激活状态（0:未激活，1:已激活）',
  `status` int(255) DEFAULT '0' COMMENT '自身状态',
  `user_id` bigint(11) DEFAULT NULL COMMENT '关联客户编号',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `used_rules_ids` varchar(255) DEFAULT NULL COMMENT '用到的规则集编号',
  `used_model_ids` varchar(255) DEFAULT NULL COMMENT '用到的模型',
  `usage_rules_ids` varchar(255) DEFAULT NULL COMMENT '使用到的规则集数组',
  `usage_model_ids` varchar(255) DEFAULT NULL COMMENT '决策流使用到的模型',
  `version` varchar(255) DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=155 DEFAULT CHARSET=utf8;

/*Table structure for table `decision_user_model` */

DROP TABLE IF EXISTS `decision_user_model`;

CREATE TABLE `decision_user_model` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '决策用户关联编号',
  `user_id` bigint(11) DEFAULT NULL COMMENT '客户编号',
  `decision_id` int(11) DEFAULT NULL COMMENT '决策编号',
  `log_id` bigint(11) DEFAULT NULL COMMENT '日志编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=105091 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_business_type` */

DROP TABLE IF EXISTS `engine_business_type`;

CREATE TABLE `engine_business_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '业务编号',
  `type_name` varchar(255) DEFAULT NULL COMMENT '类型名称',
  `describ` varchar(255) DEFAULT NULL COMMENT '业务类型描述',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '唯一id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10030 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_conditions` */

DROP TABLE IF EXISTS `engine_conditions`;

CREATE TABLE `engine_conditions` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '条件编号',
  `name` varchar(255) DEFAULT NULL COMMENT '规则名称',
  `rule_id` int(11) DEFAULT NULL COMMENT '所属规则编号',
  `rules_id` int(11) DEFAULT NULL COMMENT '所属规则集编号',
  `field_relationship` varchar(255) DEFAULT NULL COMMENT '字段关联关系',
  `describ` varchar(255) DEFAULT NULL COMMENT '描述',
  `active` int(1) DEFAULT '1' COMMENT '激活状态（0：禁用，1：激活）',
  `verify` int(11) DEFAULT '1' COMMENT '审核状态（0：未审核， 1：未审核，2：已通过，3：未通过）',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '客户唯一标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4330 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_data_type` */

DROP TABLE IF EXISTS `engine_data_type`;

CREATE TABLE `engine_data_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '类型编号',
  `type_name` varchar(50) DEFAULT NULL COMMENT '类型名称',
  `parent_id` int(11) DEFAULT NULL COMMENT '父集编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_field_type` */

DROP TABLE IF EXISTS `engine_field_type`;

CREATE TABLE `engine_field_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '字段类型编号',
  `type_name` varchar(20) DEFAULT NULL COMMENT '字段类型名称',
  `type` varchar(20) DEFAULT NULL COMMENT '字段类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_fields` */

DROP TABLE IF EXISTS `engine_fields`;

CREATE TABLE `engine_fields` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '字段编号',
  `show_name` varchar(255) DEFAULT NULL COMMENT '显示名',
  `field_id` int(11) DEFAULT NULL COMMENT '字段Id',
  `field_name` varchar(255) DEFAULT NULL COMMENT '字段名称',
  `rules_id` bigint(11) DEFAULT NULL COMMENT '规则集Id',
  `condition_id` bigint(20) DEFAULT NULL COMMENT '条件id',
  `operator` varchar(255) DEFAULT NULL COMMENT '运算符',
  `describ` varchar(255) DEFAULT NULL COMMENT '字段描述',
  `active` int(1) DEFAULT '1' COMMENT '激活状态（0：禁用，1：激活）',
  `verify` int(11) DEFAULT '1' COMMENT '审核状态（0：未审核， 1：未审核，2：已通过，3：未通过）',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `value` varchar(255) DEFAULT NULL COMMENT '初始值',
  `value_desc` varchar(255) DEFAULT NULL COMMENT '初始值描述',
  `unique_code` bigint(11) DEFAULT NULL COMMENT '客户唯一标识',
  `field_type` varchar(20) DEFAULT NULL COMMENT '字段类型名称',
  `field_type_id` int(11) DEFAULT NULL COMMENT '字段类型编号',
  `parameter` varchar(100) DEFAULT NULL COMMENT '对比值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16655 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_history_log` */

DROP TABLE IF EXISTS `engine_history_log`;

CREATE TABLE `engine_history_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '规则集历史编号',
  `rules_map` longtext COMMENT '规则集信息',
  `rules_id` bigint(20) DEFAULT NULL COMMENT '规则集编号',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '客户标识',
  `record_time` datetime DEFAULT NULL COMMENT '记录时间',
  `describ` varchar(255) DEFAULT NULL COMMENT '描述',
  `action_type` int(11) DEFAULT '0' COMMENT '操作类型',
  `rules_name` varchar(50) DEFAULT NULL COMMENT '规则集名称',
  `version` varchar(20) DEFAULT NULL COMMENT '版本号',
  `last_version` varchar(20) DEFAULT NULL COMMENT '上个版本版本号  ',
  `approval_id` bigint(20) DEFAULT NULL COMMENT '关联审核id ',
  `app_status` tinyint(1) DEFAULT '0' COMMENT '审批状态（0:未审批，1:正在审批，2:审批通过 3:审批失败 4:失效 5:撤回）',
  `submitter` varchar(255) DEFAULT NULL COMMENT '提交人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `approval_id` (`approval_id`)
) ENGINE=InnoDB AUTO_INCREMENT=754 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_inter_permission` */

DROP TABLE IF EXISTS `engine_inter_permission`;

CREATE TABLE `engine_inter_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `inter_id` bigint(20) DEFAULT NULL COMMENT '接口表id',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '客户标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6498 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_inter_type` */

DROP TABLE IF EXISTS `engine_inter_type`;

CREATE TABLE `engine_inter_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '类型编号',
  `name` varchar(50) DEFAULT NULL COMMENT '类型名称',
  `data_type` varchar(10) DEFAULT NULL COMMENT '数据类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_patch_data` */

DROP TABLE IF EXISTS `engine_patch_data`;

CREATE TABLE `engine_patch_data` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '临时表编号',
  `action_id` int(11) DEFAULT NULL COMMENT '操作类型',
  `obj_id` int(11) DEFAULT NULL COMMENT '对象名称',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `field_type` int(11) DEFAULT NULL COMMENT '字段类型',
  `data_type` int(11) DEFAULT NULL COMMENT '数据类型',
  `describ` varchar(20) DEFAULT NULL COMMENT '字段显示名',
  `business_id` int(11) DEFAULT NULL COMMENT '业务类型',
  `rules_id` bigint(20) DEFAULT NULL COMMENT '所属规则集',
  `scene_id` bigint(20) DEFAULT NULL COMMENT '适用场景',
  `active` int(1) DEFAULT NULL COMMENT '是否激活',
  `threshold` int(11) DEFAULT NULL COMMENT '风险系数',
  `level` int(1) DEFAULT NULL COMMENT '风险决策',
  `rule_code` varchar(20) DEFAULT NULL COMMENT '规则编码',
  `black_filter` varchar(20) DEFAULT NULL COMMENT '黑名单过滤',
  `white_filter` varchar(20) DEFAULT NULL COMMENT '白名单过滤',
  `describtion` varchar(255) DEFAULT NULL COMMENT '描述',
  `only_id` bigint(20) DEFAULT NULL COMMENT '原表编号',
  `verify` int(11) DEFAULT NULL COMMENT '审核状态',
  `match_type` int(11) DEFAULT NULL COMMENT '匹配模式',
  `threshold_min` int(11) DEFAULT NULL COMMENT '最小值',
  `threshold_max` int(11) DEFAULT NULL COMMENT '最大值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_raw_fields` */

DROP TABLE IF EXISTS `engine_raw_fields`;

CREATE TABLE `engine_raw_fields` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '字段编号',
  `name` varchar(255) DEFAULT NULL COMMENT '字段名称',
  `inter_id` bigint(20) DEFAULT NULL COMMENT '接口id',
  `field_type` int(255) DEFAULT NULL COMMENT '字段类型',
  `data_type` int(255) DEFAULT NULL COMMENT '数据分类',
  `describ` varchar(255) DEFAULT NULL COMMENT '显示名',
  `active` int(1) DEFAULT '0' COMMENT '激活状态（0：禁用，1：激活）',
  `verify` int(11) DEFAULT '0' COMMENT '审核状态（0：未审核， 1：未审核，2：已通过，3：未通过）',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `describtion` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1111360 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_raw_fields_copy` */

DROP TABLE IF EXISTS `engine_raw_fields_copy`;

CREATE TABLE `engine_raw_fields_copy` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '字段编号',
  `name` varchar(255) DEFAULT NULL COMMENT '字段名称',
  `inter_id` bigint(20) DEFAULT NULL COMMENT '接口id',
  `field_type` int(255) DEFAULT NULL COMMENT '字段类型',
  `data_type` int(255) DEFAULT NULL COMMENT '数据分类',
  `describ` varchar(255) DEFAULT NULL COMMENT '显示名',
  `active` int(1) DEFAULT '0' COMMENT '激活状态（0：禁用，1：激活）',
  `verify` int(11) DEFAULT '0' COMMENT '审核状态（0：未审核， 1：未审核，2：已通过，3：未通过）',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `describtion` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1111192 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_raw_fields_copy1` */

DROP TABLE IF EXISTS `engine_raw_fields_copy1`;

CREATE TABLE `engine_raw_fields_copy1` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '字段编号',
  `name` varchar(255) DEFAULT NULL COMMENT '字段名称',
  `inter_id` bigint(20) DEFAULT NULL COMMENT '接口id',
  `field_type` int(255) DEFAULT NULL COMMENT '字段类型',
  `data_type` int(255) DEFAULT NULL COMMENT '数据分类',
  `describ` varchar(255) DEFAULT NULL COMMENT '显示名',
  `active` int(1) DEFAULT '0' COMMENT '激活状态（0：禁用，1：激活）',
  `verify` int(11) DEFAULT '0' COMMENT '审核状态（0：未审核， 1：未审核，2：已通过，3：未通过）',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `describtion` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '鎻忚堪',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14003 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_raw_inter` */

DROP TABLE IF EXISTS `engine_raw_inter`;

CREATE TABLE `engine_raw_inter` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '字段编号',
  `name` varchar(255) DEFAULT NULL COMMENT '接口名称',
  `describ` varchar(255) DEFAULT NULL COMMENT '描述',
  `active` int(1) DEFAULT '0' COMMENT '激活状态（0：禁用，1：激活）',
  `verify` int(11) DEFAULT '0' COMMENT '审核状态（0：未审核， 1：未审核，2：已通过，3：未通过）',
  `add_user` varchar(255) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `parser_type` varchar(255) DEFAULT '硬编码解析' COMMENT '解析方式',
  `parameters` text,
  `request_type` varchar(50) DEFAULT NULL COMMENT '请求类型',
  `parameters_desc` varchar(255) DEFAULT NULL COMMENT '参数描述',
  `inter_type` int(11) DEFAULT NULL COMMENT '接口类型',
  `private_label` int(10) DEFAULT NULL COMMENT '私有标识 1:公有0:私有',
  `data_type` int(10) DEFAULT NULL COMMENT '数据类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10462 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_ros_black` */

DROP TABLE IF EXISTS `engine_ros_black`;

CREATE TABLE `engine_ros_black` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '黑名单编号',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `mobilephone` varchar(20) DEFAULT '' COMMENT '手机号',
  `card_no` varchar(20) DEFAULT NULL COMMENT '银行卡号',
  `admin` varchar(50) DEFAULT NULL COMMENT '添加人',
  `add_time` date DEFAULT NULL COMMENT '添加时间',
  `status` int(11) DEFAULT '1' COMMENT '名单状态',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '用户关联',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_ros_white` */

DROP TABLE IF EXISTS `engine_ros_white`;

CREATE TABLE `engine_ros_white` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '黑名单编号',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `mobilephone` varchar(20) DEFAULT '' COMMENT '手机号',
  `card_no` varchar(20) DEFAULT NULL COMMENT '银行卡号',
  `admin` varchar(50) DEFAULT NULL COMMENT '添加人',
  `add_time` date DEFAULT NULL COMMENT '添加时间',
  `status` int(11) DEFAULT '1' COMMENT '名单状态（1，正常 默认正常）',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '关联用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_rule` */

DROP TABLE IF EXISTS `engine_rule`;

CREATE TABLE `engine_rule` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '规则编号',
  `name` varchar(255) DEFAULT NULL COMMENT '规则名称',
  `rules_id` int(11) DEFAULT NULL COMMENT '所属规则集id',
  `decision` varchar(255) DEFAULT NULL COMMENT '描述',
  `level` int(11) DEFAULT NULL COMMENT '风险等级(1:无风险2:低风险3:高风险)',
  `threshold` int(11) DEFAULT NULL COMMENT '总分（风险系数）',
  `condition_number` int(11) DEFAULT '0' COMMENT '条件数',
  `describ` varchar(255) DEFAULT NULL COMMENT '描述',
  `active` int(1) DEFAULT '1' COMMENT '激活状态（0：禁用，1：激活）',
  `verify` int(11) DEFAULT '0' COMMENT '审核状态（0：未审核， 1：未审核，2：已通过，3：未通过）',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `condition_relationship` varchar(255) DEFAULT NULL COMMENT '条件逻辑关系',
  `unique_code` bigint(11) DEFAULT NULL COMMENT '客户标识',
  `rule_code` varchar(50) DEFAULT NULL COMMENT '派生指标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12964 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_rules` */

DROP TABLE IF EXISTS `engine_rules`;

CREATE TABLE `engine_rules` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '规则集id',
  `name` varchar(255) DEFAULT NULL COMMENT '规则集名称',
  `business_id` int(11) DEFAULT NULL COMMENT '业务类型id',
  `sence_id` bigint(11) DEFAULT NULL COMMENT '场景类型id',
  `match_type` int(1) DEFAULT '0' COMMENT '匹配模式（0：均值匹配，1：最优匹配）',
  `threshold` int(11) DEFAULT NULL COMMENT '阈值',
  `threshold_min` int(11) DEFAULT NULL COMMENT '最小值',
  `threshold_max` int(11) DEFAULT NULL COMMENT '最大值',
  `describ` varchar(255) DEFAULT NULL COMMENT '规则集描述',
  `verify` int(11) DEFAULT '0' COMMENT '审核状态（0：未审核， 1：正在审核，2：已通过，3：未通过）',
  `active` int(1) DEFAULT '0' COMMENT '激活状态（0：禁用，1：激活）',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '客户标识',
  `parameters` text COMMENT '参数集',
  `white_filter` varchar(255) DEFAULT NULL COMMENT '白名单过滤',
  `black_filter` varchar(255) DEFAULT NULL COMMENT '黑名单过滤',
  `status` int(1) DEFAULT '0' COMMENT '删除标识（0：未删除；1：已删除）',
  `rule_json` text COMMENT '规则json',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=915 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_rules_online` */

DROP TABLE IF EXISTS `engine_rules_online`;

CREATE TABLE `engine_rules_online` (
  `id` bigint(11) unsigned NOT NULL COMMENT '规则集id',
  `name` varchar(255) DEFAULT NULL COMMENT '规则集名称',
  `business_id` int(11) DEFAULT NULL COMMENT '业务类型id',
  `sence_id` bigint(11) DEFAULT NULL COMMENT '场景类型id',
  `match_type` int(1) DEFAULT '0' COMMENT '匹配模式（0：均值匹配，1：最优匹配）',
  `threshold` int(11) DEFAULT NULL COMMENT '阈值',
  `threshold_min` int(11) DEFAULT NULL COMMENT '最小值',
  `threshold_max` int(11) DEFAULT NULL COMMENT '最大值',
  `describ` varchar(255) DEFAULT NULL COMMENT '规则集描述',
  `verify` int(11) DEFAULT '0' COMMENT '审核状态（0：未审核， 1：正在审核，2：已通过，3：未通过）',
  `active` int(1) DEFAULT '0' COMMENT '激活状态（0：禁用，1：激活）',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '客户标识',
  `parameters` text COMMENT '参数集',
  `white_filter` varchar(255) DEFAULT NULL COMMENT '白名单过滤',
  `black_filter` varchar(255) DEFAULT NULL COMMENT '黑名单过滤',
  `rules_json` text COMMENT '规则集合',
  `version` varchar(20) DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `engine_rules_public_log` */

DROP TABLE IF EXISTS `engine_rules_public_log`;

CREATE TABLE `engine_rules_public_log` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '规则集id',
  `name` varchar(255) DEFAULT NULL COMMENT '规则集名称',
  `business_id` int(11) DEFAULT NULL COMMENT '业务类型id',
  `sence_id` bigint(11) DEFAULT NULL COMMENT '场景类型id',
  `match_type` int(1) DEFAULT '0' COMMENT '匹配模式（0：均值匹配，1：最优匹配）',
  `threshold` int(11) DEFAULT NULL COMMENT '阈值',
  `threshold_min` int(11) DEFAULT NULL COMMENT '最小值',
  `threshold_max` int(11) DEFAULT NULL COMMENT '最大值',
  `describ` varchar(255) DEFAULT NULL COMMENT '规则集描述',
  `verify` int(11) DEFAULT '0' COMMENT '审核状态（0：未审核， 1：正在审核，2：已通过，3：未通过）',
  `active` int(1) DEFAULT '0' COMMENT '激活状态（0：禁用，1：激活）',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '客户标识',
  `parameters` text COMMENT '参数集',
  `white_filter` varchar(255) DEFAULT NULL COMMENT '白名单过滤',
  `black_filter` varchar(255) DEFAULT NULL COMMENT '黑名单过滤',
  `rules_json` text COMMENT '规则集合',
  `approval_id` bigint(20) DEFAULT NULL COMMENT '审批ID',
  `rule_set_id` bigint(20) DEFAULT NULL COMMENT '规则集id',
  `version` varchar(20) DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=334 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_rules_publish_logs` */

DROP TABLE IF EXISTS `engine_rules_publish_logs`;

CREATE TABLE `engine_rules_publish_logs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '最新规则集日志id',
  `rules_id` bigint(11) DEFAULT NULL COMMENT '规则集id',
  `rules_name` varchar(255) DEFAULT NULL COMMENT '规则集名称',
  `rule_set` text COMMENT '规则集json',
  `active` tinyint(2) DEFAULT NULL COMMENT '是否激活（0：禁用，1：激活）',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '客户编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Table structure for table `engine_scenes` */

DROP TABLE IF EXISTS `engine_scenes`;

CREATE TABLE `engine_scenes` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '场景编号',
  `business_id` int(11) DEFAULT NULL COMMENT '业务类型id',
  `name` varchar(255) DEFAULT NULL COMMENT '场景名称',
  `describ` varchar(255) DEFAULT NULL COMMENT '场景描述',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '唯一id',
  `active` tinyint(1) DEFAULT '1',
  `verify` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10158 DEFAULT CHARSET=utf8;

/*Table structure for table `event_history_log` */

DROP TABLE IF EXISTS `event_history_log`;

CREATE TABLE `event_history_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `model_id` bigint(20) DEFAULT NULL COMMENT '模型编号',
  `model_name` varchar(255) DEFAULT NULL COMMENT '模型名称',
  `model_type` tinyint(1) DEFAULT NULL COMMENT '模型类型（0:决策；1:规则集；2:模型）',
  `identify` varchar(255) DEFAULT NULL COMMENT '批次号',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '客户编号',
  `pass_num` int(11) DEFAULT '0' COMMENT '通过量',
  `refuse_num` int(11) DEFAULT '0' COMMENT '拒绝量',
  `artificial_num` int(11) DEFAULT '0' COMMENT '人工审核量',
  `invalid_num` int(11) DEFAULT '0' COMMENT '失效量',
  `file_name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `entry_time` datetime DEFAULT NULL COMMENT '进件时间',
  `job_type` tinyint(1) DEFAULT NULL COMMENT '任务类型（0:小批量任务；1:大批量任务）',
  `job_progress` int(50) DEFAULT NULL COMMENT '任务进度(已完成量)',
  `job_status` tinyint(1) DEFAULT NULL COMMENT '任务状态（0:新建；1:暂停；2:终止；3:完成;4:失败）',
  `task_num` int(11) DEFAULT '0' COMMENT '总量',
  `remark` varchar(255) DEFAULT NULL COMMENT '批次',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=787 DEFAULT CHARSET=utf8;

/*Table structure for table `geo_role_menu` */

DROP TABLE IF EXISTS `geo_role_menu`;

CREATE TABLE `geo_role_menu` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '运营端角色权限关联编号',
  `geo_id` bigint(20) DEFAULT NULL COMMENT '运营端编号',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单编号',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=547 DEFAULT CHARSET=utf8;

/*Table structure for table `geo_user` */

DROP TABLE IF EXISTS `geo_user`;

CREATE TABLE `geo_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '员工管理ID',
  `contact` varchar(255) DEFAULT NULL COMMENT '联系人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creater` varchar(255) DEFAULT NULL COMMENT '创建人',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `mobilephone` varchar(255) DEFAULT NULL COMMENT '手机号',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  `salt` varchar(255) DEFAULT NULL COMMENT '盐',
  `status` int(11) DEFAULT '1' COMMENT '员工状态（1，正常 -默认正常）',
  `username` varchar(255) DEFAULT NULL COMMENT '登录用户名',
  `login_ip` varchar(50) DEFAULT NULL COMMENT '登录IP',
  `login_date` date DEFAULT NULL COMMENT '登录日期',
  `role_type` varchar(50) DEFAULT NULL COMMENT '角色类型',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '客户标识',
  `company` varchar(50) DEFAULT NULL COMMENT '公司名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Table structure for table `geo_user_role` */

DROP TABLE IF EXISTS `geo_user_role`;

CREATE TABLE `geo_user_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '运营端用户角色关联编号',
  `geo_id` bigint(20) DEFAULT NULL COMMENT '用户编号',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `geo_user_token` */

DROP TABLE IF EXISTS `geo_user_token`;

CREATE TABLE `geo_user_token` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '运营端用户token关联编号',
  `geo_id` bigint(20) DEFAULT NULL COMMENT '用户编号',
  `token` varchar(255) DEFAULT NULL COMMENT 'token',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;

/*Table structure for table `job_log_monitor` */

DROP TABLE IF EXISTS `job_log_monitor`;

CREATE TABLE `job_log_monitor` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '????id',
  `job_id` bigint(20) NOT NULL COMMENT '??id',
  `bean_name` varchar(200) DEFAULT NULL COMMENT 'spring bean??',
  `method_name` varchar(100) DEFAULT NULL COMMENT '???',
  `params` varchar(2000) DEFAULT NULL COMMENT '??',
  `status` tinyint(4) NOT NULL COMMENT '????    0???    1???',
  `error` varchar(2000) DEFAULT NULL COMMENT '????',
  `times` int(11) NOT NULL COMMENT '??(?????)',
  `create_time` datetime DEFAULT NULL COMMENT '????',
  PRIMARY KEY (`log_id`),
  KEY `job_id` (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='??????';

/*Table structure for table `jobs_monitor` */

DROP TABLE IF EXISTS `jobs_monitor`;

CREATE TABLE `jobs_monitor` (
  `job_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '??',
  `job_name` varchar(255) DEFAULT NULL COMMENT '????',
  `bean_name` varchar(255) DEFAULT NULL COMMENT '??',
  `method_name` varchar(255) DEFAULT NULL COMMENT '???',
  `params` varchar(255) DEFAULT NULL COMMENT '??',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '???????',
  `status` varchar(255) DEFAULT NULL COMMENT '?????0:????1:???2:--?3:???4:???',
  `priority` int(255) DEFAULT NULL COMMENT '????0:???1???2:??3:???',
  `group` varchar(255) DEFAULT NULL COMMENT '??',
  `upper_limit` int(20) DEFAULT NULL COMMENT '??',
  `create_time` datetime DEFAULT NULL COMMENT '????',
  `update_time` datetime DEFAULT NULL COMMENT '????',
  `remark` varchar(255) DEFAULT NULL COMMENT '??',
  `user_id` bigint(20) DEFAULT NULL COMMENT '????',
  `rules_id` bigint(20) DEFAULT NULL COMMENT '?????',
  `cycle_num` int(11) DEFAULT NULL COMMENT '???',
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `monitor_dimension` */

DROP TABLE IF EXISTS `monitor_dimension`;

CREATE TABLE `monitor_dimension` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '监控维度编号',
  `dimension_name` varchar(20) DEFAULT NULL COMMENT '维度名称编码',
  `dimension_desc` varchar(20) DEFAULT NULL COMMENT '维度名称描述',
  `policy_id` bigint(20) NOT NULL COMMENT '接口id',
  `user_name` varchar(10) DEFAULT NULL COMMENT '添加人/修改人',
  `unicode` bigint(11) NOT NULL COMMENT '所属公司唯一标识',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `policy_type` tinyint(1) DEFAULT NULL COMMENT '策略类型(0:规则集,1:决策集,2:模型,3:接口)\n',
  `alarm_policy` varchar(50) DEFAULT NULL COMMENT '复选--1:通过,2:人工审核,3:拒绝）\n',
  `status` tinyint(1) DEFAULT '0' COMMENT '0:激活／1:禁用',
  `policy_name` varchar(50) DEFAULT NULL COMMENT '维度名称',
  `param_json` varchar(255) DEFAULT NULL COMMENT '进件参数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

/*Table structure for table `monitor_jobs` */

DROP TABLE IF EXISTS `monitor_jobs`;

CREATE TABLE `monitor_jobs` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `job_name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `bean_name` varchar(255) DEFAULT NULL COMMENT '类名',
  `method_name` varchar(255) DEFAULT NULL COMMENT '方法名',
  `params` varchar(50) DEFAULT NULL COMMENT '参数',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '表达式（检查任务频率）',
  `status` varchar(255) DEFAULT '1' COMMENT '任务状态（0:运行中，1:暂停，2:--，3:结束，4:异常;5:已删除）',
  `priority` int(255) DEFAULT NULL COMMENT '优先级（1:最高，2:高，3:中4:低）',
  `group` varchar(255) DEFAULT NULL COMMENT '分组',
  `upper_limit` int(20) DEFAULT NULL COMMENT '上限',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `user_id` bigint(20) DEFAULT NULL COMMENT '客户标识',
  `dimension` bigint(20) DEFAULT NULL COMMENT '维度编号',
  `cycle_num` int(11) DEFAULT NULL COMMENT '周期数',
  `monitor_unit` tinyint(1) DEFAULT NULL COMMENT '监控单位（1:天；2:月，3:周）',
  `monitored_num` int(11) DEFAULT '0' COMMENT '已执行期数',
  `interval` int(11) DEFAULT NULL COMMENT '间隔',
  `hit_policy` tinyint(1) DEFAULT NULL COMMENT '命中策略（0:停止监控,1:继续监控）',
  `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮箱,多个邮箱用;隔开',
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

/*Table structure for table `monitor_jobs_log` */

DROP TABLE IF EXISTS `monitor_jobs_log`;

CREATE TABLE `monitor_jobs_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志id',
  `job_id` bigint(20) NOT NULL COMMENT '任务id',
  `bean_name` varchar(200) DEFAULT NULL COMMENT 'spring bean名称',
  `method_name` varchar(100) DEFAULT NULL COMMENT '方法名',
  `params` varchar(50) DEFAULT NULL COMMENT '参数',
  `status` tinyint(4) NOT NULL COMMENT '任务状态    0：成功    1：失败',
  `error` varchar(2000) DEFAULT NULL COMMENT '失败信息',
  `times` int(11) NOT NULL COMMENT '耗时(单位：毫秒)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`log_id`),
  KEY `job_id` (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='定时任务日志';

/*Table structure for table `monitor_task_0_part` */

DROP TABLE IF EXISTS `monitor_task_0_part`;

CREATE TABLE `monitor_task_0_part` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `id_number` varchar(20) DEFAULT NULL COMMENT '身份证',
  `cid` bigint(11) DEFAULT NULL COMMENT '手机号',
  `job_id` int(11) DEFAULT NULL COMMENT '任务编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `task_status` tinyint(1) DEFAULT '0' COMMENT '执行状态（0:未执行;1:监控中;2完成;3:停止监控）',
  `distribute_status` tinyint(1) DEFAULT '0' COMMENT '分发状态(0:未分发，1：已分发)',
  `distribute_time` datetime DEFAULT NULL COMMENT '分发时间',
  `distribute_num` int(11) DEFAULT '0' COMMENT '分发次数',
  `update_time` datetime DEFAULT '2018-01-01 00:00:00' COMMENT '更新时间',
  `result_map` text COMMENT '查询结果',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '周期',
  `user_id` bigint(11) DEFAULT NULL COMMENT '客户标识',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `credit_cycle` int(11) DEFAULT NULL COMMENT '贷款周期',
  `execute_status` tinyint(1) DEFAULT NULL COMMENT '上次执行状态3:报警；0:错误；1:正常；2:执行中',
  `read_status` tinyint(1) DEFAULT '0' COMMENT '阅读状态（0:未读，1:已读）',
  `parms_json` varchar(255) DEFAULT NULL COMMENT '参数mapJson',
  PRIMARY KEY (`id`),
  KEY `gather_index` (`job_id`,`user_id`,`execute_status`,`task_status`,`distribute_status`)
) ENGINE=InnoDB AUTO_INCREMENT=1012 DEFAULT CHARSET=utf8;

/*Table structure for table `monitor_task_111_part` */

DROP TABLE IF EXISTS `monitor_task_111_part`;

CREATE TABLE `monitor_task_111_part` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `id_number` varchar(20) DEFAULT NULL COMMENT '身份证',
  `cid` bigint(11) DEFAULT NULL COMMENT '手机号',
  `job_id` int(11) DEFAULT NULL COMMENT '任务编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `task_status` int(255) DEFAULT '0' COMMENT '执行状态（0:未执行;1:监控中;2完成;）',
  `distribute_status` int(255) DEFAULT '0' COMMENT '分发状态',
  `distribute_time` datetime DEFAULT NULL COMMENT '分发时间',
  `distribute_num` int(11) DEFAULT '0' COMMENT '分发次数',
  `update_time` datetime DEFAULT '2018-01-01 00:00:00' COMMENT '更新时间',
  `result_map` varchar(255) DEFAULT NULL COMMENT '查询结果',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '周期',
  `user_id` bigint(11) DEFAULT NULL COMMENT '客户标识',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `credit_cycle` int(11) DEFAULT NULL COMMENT '贷款周期',
  `execute_status` int(11) DEFAULT NULL COMMENT '上次执行状态3:异常；0:错误；1:成功；2:执行中;4：未执行',
  `read_status` int(11) DEFAULT NULL COMMENT '阅读状态',
  `parms_json` varchar(255) DEFAULT NULL COMMENT '进件参数',
  PRIMARY KEY (`id`),
  KEY `gather_index` (`job_id`,`user_id`,`execute_status`,`task_status`,`distribute_status`)
) ENGINE=InnoDB AUTO_INCREMENT=508390 DEFAULT CHARSET=utf8;

/*Table structure for table `monitor_task_2_part` */

DROP TABLE IF EXISTS `monitor_task_2_part`;

CREATE TABLE `monitor_task_2_part` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `id_number` varchar(20) DEFAULT NULL COMMENT '身份证',
  `cid` bigint(11) DEFAULT NULL COMMENT '手机号',
  `job_id` int(11) DEFAULT NULL COMMENT '任务编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `task_status` int(255) DEFAULT '0' COMMENT '执行状态（0:未执行;1:监控中;2完成;）',
  `distribute_status` int(255) DEFAULT '0' COMMENT '分发状态',
  `distribute_time` datetime DEFAULT NULL COMMENT '分发时间',
  `distribute_num` int(11) DEFAULT '0' COMMENT '分发次数',
  `update_time` datetime DEFAULT '2018-01-01 00:00:00' COMMENT '更新时间',
  `result_map` varchar(255) DEFAULT NULL COMMENT '查询结果',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '周期',
  `user_id` bigint(11) DEFAULT NULL COMMENT '客户标识',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `credit_cycle` int(11) DEFAULT NULL COMMENT '贷款周期',
  `execute_status` int(11) DEFAULT NULL COMMENT '上次执行状态3:报警；0:错误；1:正常；2:执行中',
  `read_status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gather_index` (`job_id`,`user_id`,`execute_status`,`task_status`,`distribute_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `monitor_task_33_part` */

DROP TABLE IF EXISTS `monitor_task_33_part`;

CREATE TABLE `monitor_task_33_part` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `id_number` varchar(20) DEFAULT NULL COMMENT '身份证',
  `cid` bigint(11) DEFAULT NULL COMMENT '手机号',
  `job_id` int(11) DEFAULT NULL COMMENT '任务编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `task_status` int(255) DEFAULT '0' COMMENT '执行状态（0:未执行;1:监控中;2完成;）',
  `distribute_status` int(255) DEFAULT '0' COMMENT '分发状态',
  `distribute_time` datetime DEFAULT NULL COMMENT '分发时间',
  `distribute_num` int(11) DEFAULT '0' COMMENT '分发次数',
  `update_time` datetime DEFAULT '2018-01-01 00:00:00' COMMENT '更新时间',
  `result_map` text COMMENT '查询结果',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '周期',
  `user_id` bigint(11) DEFAULT NULL COMMENT '客户标识',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `credit_cycle` int(11) DEFAULT NULL COMMENT '贷款周期',
  `execute_status` int(11) DEFAULT NULL COMMENT '上次执行状态3:报警；0:错误；1:正常；2:执行中',
  `read_status` int(11) DEFAULT NULL,
  `parms_json` varchar(255) DEFAULT NULL COMMENT '参数mapJson',
  PRIMARY KEY (`id`),
  KEY `gather_index` (`job_id`,`user_id`,`execute_status`,`task_status`,`distribute_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `monitor_task_34_part` */

DROP TABLE IF EXISTS `monitor_task_34_part`;

CREATE TABLE `monitor_task_34_part` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `id_number` varchar(20) DEFAULT NULL COMMENT '身份证',
  `cid` bigint(11) DEFAULT NULL COMMENT '手机号',
  `job_id` int(11) DEFAULT NULL COMMENT '任务编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `task_status` int(255) DEFAULT '0' COMMENT '执行状态（0:未执行;1:监控中;2完成;）',
  `distribute_status` int(255) DEFAULT '0' COMMENT '分发状态',
  `distribute_time` datetime DEFAULT NULL COMMENT '分发时间',
  `distribute_num` int(11) DEFAULT '0' COMMENT '分发次数',
  `update_time` datetime DEFAULT '2018-01-01 00:00:00' COMMENT '更新时间',
  `result_map` text COMMENT '查询结果',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '周期',
  `user_id` bigint(11) DEFAULT NULL COMMENT '客户标识',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `credit_cycle` int(11) DEFAULT NULL COMMENT '贷款周期',
  `execute_status` int(11) DEFAULT NULL COMMENT '上次执行状态3:报警；0:错误；1:正常；2:执行中',
  `read_status` int(11) DEFAULT NULL,
  `parms_json` varchar(255) DEFAULT NULL COMMENT '参数mapJson',
  PRIMARY KEY (`id`),
  KEY `gather_index` (`job_id`,`user_id`,`execute_status`,`task_status`,`distribute_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `monitor_task_36_part` */

DROP TABLE IF EXISTS `monitor_task_36_part`;

CREATE TABLE `monitor_task_36_part` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `id_number` varchar(20) DEFAULT NULL COMMENT '身份证',
  `cid` bigint(11) DEFAULT NULL COMMENT '手机号',
  `job_id` int(11) DEFAULT NULL COMMENT '任务编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `task_status` int(255) DEFAULT '0' COMMENT '执行状态（0:未执行;1:监控中;2完成;）',
  `distribute_status` int(255) DEFAULT '0' COMMENT '分发状态',
  `distribute_time` datetime DEFAULT NULL COMMENT '分发时间',
  `distribute_num` int(11) DEFAULT '0' COMMENT '分发次数',
  `update_time` datetime DEFAULT '2018-01-01 00:00:00' COMMENT '更新时间',
  `result_map` text COMMENT '查询结果',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '周期',
  `user_id` bigint(11) DEFAULT NULL COMMENT '客户标识',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `credit_cycle` int(11) DEFAULT NULL COMMENT '贷款周期',
  `execute_status` int(11) DEFAULT NULL COMMENT '上次执行状态3:报警；0:错误；1:正常；2:执行中',
  `read_status` int(11) DEFAULT NULL,
  `parms_json` varchar(255) DEFAULT NULL COMMENT '参数mapJson',
  PRIMARY KEY (`id`),
  KEY `gather_index` (`job_id`,`user_id`,`execute_status`,`task_status`,`distribute_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `monitor_task_38_part` */

DROP TABLE IF EXISTS `monitor_task_38_part`;

CREATE TABLE `monitor_task_38_part` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `id_number` varchar(20) DEFAULT NULL COMMENT '身份证',
  `cid` bigint(11) DEFAULT NULL COMMENT '手机号',
  `job_id` int(11) DEFAULT NULL COMMENT '任务编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `task_status` tinyint(1) DEFAULT '0' COMMENT '执行状态（0:未执行;1:监控中;2完成;）',
  `distribute_status` tinyint(1) DEFAULT '0' COMMENT '分发状态(0:未分发，1：已分发)',
  `distribute_time` datetime DEFAULT NULL COMMENT '分发时间',
  `distribute_num` int(11) DEFAULT '0' COMMENT '分发次数',
  `update_time` datetime DEFAULT '2018-01-01 00:00:00' COMMENT '更新时间',
  `result_map` text COMMENT '查询结果',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '周期',
  `user_id` bigint(11) DEFAULT NULL COMMENT '客户标识',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `credit_cycle` int(11) DEFAULT NULL COMMENT '贷款周期',
  `execute_status` tinyint(1) DEFAULT NULL COMMENT '上次执行状态3:报警；0:错误；1:正常；2:执行中',
  `read_status` tinyint(1) DEFAULT '0' COMMENT '阅读状态（0:未读，1:已读）',
  `parms_json` varchar(255) DEFAULT NULL COMMENT '参数mapJson',
  PRIMARY KEY (`id`),
  KEY `gather_index` (`job_id`,`user_id`,`execute_status`,`task_status`,`distribute_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `monitor_task_39_part` */

DROP TABLE IF EXISTS `monitor_task_39_part`;

CREATE TABLE `monitor_task_39_part` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `id_number` varchar(20) DEFAULT NULL COMMENT '身份证',
  `cid` bigint(11) DEFAULT NULL COMMENT '手机号',
  `job_id` int(11) DEFAULT NULL COMMENT '任务编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `task_status` tinyint(1) DEFAULT '0' COMMENT '执行状态（0:未执行;1:监控中;2完成;）',
  `distribute_status` tinyint(1) DEFAULT '0' COMMENT '分发状态(0:未分发，1：已分发)',
  `distribute_time` datetime DEFAULT NULL COMMENT '分发时间',
  `distribute_num` int(11) DEFAULT '0' COMMENT '分发次数',
  `update_time` datetime DEFAULT '2018-01-01 00:00:00' COMMENT '更新时间',
  `result_map` text COMMENT '查询结果',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '周期',
  `user_id` bigint(11) DEFAULT NULL COMMENT '客户标识',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `credit_cycle` int(11) DEFAULT NULL COMMENT '贷款周期',
  `execute_status` tinyint(1) DEFAULT NULL COMMENT '上次执行状态3:报警；0:错误；1:正常；2:执行中',
  `read_status` tinyint(1) DEFAULT '0' COMMENT '阅读状态（0:未读，1:已读）',
  `parms_json` varchar(255) DEFAULT NULL COMMENT '参数mapJson',
  PRIMARY KEY (`id`),
  KEY `gather_index` (`job_id`,`user_id`,`execute_status`,`task_status`,`distribute_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `monitor_task_log_0_part` */

DROP TABLE IF EXISTS `monitor_task_log_0_part`;

CREATE TABLE `monitor_task_log_0_part` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `task_id` bigint(20) DEFAULT NULL COMMENT '名单编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `job_id` bigint(11) DEFAULT NULL COMMENT '任务编号',
  `result_map` text COMMENT '结果',
  `cycle_num` int(11) DEFAULT '1' COMMENT '期数',
  `status` int(11) DEFAULT NULL COMMENT '0失败,1成功,2执行中',
  `remark` text COMMENT '备注',
  `user_id` bigint(20) DEFAULT NULL COMMENT '客户编号',
  `operator` varchar(10) DEFAULT NULL COMMENT '操作人',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `audit_status` int(11) DEFAULT NULL COMMENT '审核状态(0: 重试；1:报警；2:正常；3:误报)',
  PRIMARY KEY (`id`),
  KEY `gather_index` (`create_time`,`job_id`),
  KEY `task_index` (`task_id`),
  KEY `status_index` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=2019 DEFAULT CHARSET=utf8;

/*Table structure for table `schedule_job` */

DROP TABLE IF EXISTS `schedule_job`;

CREATE TABLE `schedule_job` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务编号',
  `bean_name` varchar(200) DEFAULT NULL COMMENT 'spring bean类名',
  `method_name` varchar(100) DEFAULT NULL COMMENT '方法名',
  `params` varchar(2000) DEFAULT NULL COMMENT '参数',
  `cron_expression` varchar(100) DEFAULT NULL COMMENT '表达式',
  `status` tinyint(4) DEFAULT NULL COMMENT '任务状态（0:未启动；1:启动）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='????';

/*Table structure for table `schedule_job_log` */

DROP TABLE IF EXISTS `schedule_job_log`;

CREATE TABLE `schedule_job_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志编号',
  `job_id` bigint(20) NOT NULL COMMENT '任务编号',
  `bean_name` varchar(255) DEFAULT NULL COMMENT 'spring bean??',
  `method_name` varchar(50) DEFAULT NULL COMMENT '方法名',
  `params` varchar(255) DEFAULT NULL COMMENT '参数',
  `status` tinyint(4) NOT NULL COMMENT '状态',
  `error` varchar(255) DEFAULT NULL COMMENT '错误信息',
  `times` int(11) NOT NULL COMMENT '耗时',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`log_id`),
  KEY `job_id` (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='??????';

/*Table structure for table `sys_customer` */

DROP TABLE IF EXISTS `sys_customer`;

CREATE TABLE `sys_customer` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '客户编号',
  `server` varchar(255) DEFAULT NULL COMMENT '加密类型和加密秘钥向GEO索取 AES(秘钥长度不固定)、AES2(秘钥长度16)、DES(秘钥长度8)、DESede(秘钥长度24)、XOR(秘钥只能是数字)',
  `encrypted` int(11) DEFAULT NULL COMMENT '是否加密传输  1是0否',
  `encryption_type` varchar(50) DEFAULT NULL COMMENT '加密类型',
  `encryption_key` varchar(255) DEFAULT NULL COMMENT '加密秘钥',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `uno` varchar(255) DEFAULT NULL COMMENT '合同号',
  `etype` varchar(11) DEFAULT NULL COMMENT '只能填写""!',
  `dsign` int(11) DEFAULT NULL COMMENT '是否进行数字签名 1是0否',
  `user_id` bigint(20) DEFAULT NULL COMMENT '客户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_customer_version` */

DROP TABLE IF EXISTS `sys_customer_version`;

CREATE TABLE `sys_customer_version` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '客户版本关联编号',
  `creater` varchar(255) DEFAULT NULL COMMENT '创建人',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `expiretime` datetime DEFAULT NULL COMMENT '结束时间',
  `updater` varchar(255) DEFAULT NULL COMMENT '修改人',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `version_id` int(11) DEFAULT NULL COMMENT '版本类型',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_customer_zxw` */

DROP TABLE IF EXISTS `sys_customer_zxw`;

CREATE TABLE `sys_customer_zxw` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '客户编号',
  `server` varchar(255) DEFAULT NULL COMMENT '加密类型和加密秘钥向GEO索取 AES(秘钥长度不固定)、AES2(秘钥长度16)、DES(秘钥长度8)、DESede(秘钥长度24)、XOR(秘钥只能是数字)',
  `encrypted` int(11) DEFAULT NULL COMMENT '是否加密传输  1是0否',
  `encryption_type` varchar(50) DEFAULT NULL COMMENT '加密类型',
  `encryption_key` varchar(255) DEFAULT NULL COMMENT '加密秘钥',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `uno` varchar(255) DEFAULT NULL COMMENT '合同号',
  `dsign` int(11) DEFAULT NULL COMMENT '是否进行数字签名 1是0否',
  `user_id` bigint(20) DEFAULT NULL COMMENT '客户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_id_generator` */

DROP TABLE IF EXISTS `sys_id_generator`;

CREATE TABLE `sys_id_generator` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `key` varchar(64) NOT NULL,
  `value` bigint(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_id_generator_copy1` */

DROP TABLE IF EXISTS `sys_id_generator_copy1`;

CREATE TABLE `sys_id_generator_copy1` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `key` varchar(64) NOT NULL,
  `value` bigint(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_login_log` */

DROP TABLE IF EXISTS `sys_login_log`;

CREATE TABLE `sys_login_log` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '登录日志编号',
  `logname` varchar(255) DEFAULT NULL COMMENT '日志名称',
  `userid` bigint(11) DEFAULT NULL COMMENT '用户编号',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `succeed` varchar(255) DEFAULT NULL COMMENT '是否成功',
  `message` varchar(255) DEFAULT NULL COMMENT '信息',
  `ip` varchar(255) DEFAULT NULL COMMENT 'ip地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2663 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单id',
  `parent_name` varchar(20) DEFAULT NULL COMMENT '父菜单名称',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `url` varchar(255) DEFAULT NULL COMMENT '菜单url',
  `perms` varchar(255) DEFAULT NULL COMMENT '授权',
  `type` int(11) DEFAULT '1' COMMENT '类型',
  `open` tinyint(1) DEFAULT NULL COMMENT 'ztree属性',
  `front_icon` varchar(255) DEFAULT NULL COMMENT '前面的图标',
  `back_icon` varchar(255) DEFAULT NULL COMMENT '后面的图标',
  `order_id` int(11) DEFAULT NULL COMMENT '排序',
  `filter_type` int(1) DEFAULT NULL COMMENT '是否过滤（2-可显不能点 1-不可显 0-可显）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=341 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_menu2` */

DROP TABLE IF EXISTS `sys_menu2`;

CREATE TABLE `sys_menu2` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单id',
  `parent_name` varchar(20) DEFAULT NULL COMMENT '父菜单名称',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `url` varchar(255) DEFAULT NULL COMMENT '菜单url',
  `perms` varchar(255) DEFAULT NULL COMMENT '授权',
  `type` int(11) DEFAULT '1' COMMENT '类型',
  `open` tinyint(1) DEFAULT NULL COMMENT 'ztree属性',
  `front_icon` varchar(255) DEFAULT NULL COMMENT '前面的图标',
  `back_icon` varchar(255) DEFAULT NULL COMMENT '后面的图标',
  `order_id` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creater` varchar(255) DEFAULT NULL COMMENT '创建人',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `encode` varchar(255) DEFAULT NULL COMMENT '角色编码',
  `role_name` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `pid` int(11) DEFAULT NULL COMMENT '父级ID',
  `status` int(11) DEFAULT '1' COMMENT '状态',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `user_id` bigint(20) DEFAULT NULL COMMENT '客户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色权限关联编号',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色编号',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8739 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_role_menu2` */

DROP TABLE IF EXISTS `sys_role_menu2`;

CREATE TABLE `sys_role_menu2` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL,
  `menu_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=259 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '客户id',
  `contact` varchar(255) DEFAULT NULL COMMENT '联系人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creater` varchar(255) DEFAULT NULL COMMENT '创建人',
  `email` varchar(255) DEFAULT NULL COMMENT '电子邮箱',
  `mobilephone` varchar(255) DEFAULT NULL COMMENT '手机号',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `password` varchar(255) DEFAULT NULL COMMENT '登录密码',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `salt` varchar(255) DEFAULT NULL COMMENT '盐值',
  `status` int(11) DEFAULT '1' COMMENT '状态',
  `username` varchar(255) DEFAULT NULL COMMENT '登录用户名',
  `login_ip` varchar(50) DEFAULT NULL COMMENT '登录ip',
  `login_date` datetime DEFAULT NULL COMMENT '登录时间',
  `unique_code` bigint(20) DEFAULT NULL COMMENT '客户标识',
  `company` varchar(50) DEFAULT NULL COMMENT '公司名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=402 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `u_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `id` bigint(100) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户角色关联编号',
  PRIMARY KEY (`id`),
  KEY `FKt7u9ggroj0hseyo20nexvep86` (`u_id`),
  KEY `FKhh52n8vd4ny9ff4x9fb8v65qx` (`role_id`),
  CONSTRAINT `FKhh52n8vd4ny9ff4x9fb8v65qx` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`),
  CONSTRAINT `FKt7u9ggroj0hseyo20nexvep86` FOREIGN KEY (`u_id`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=528 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_user_token` */

DROP TABLE IF EXISTS `sys_user_token`;

CREATE TABLE `sys_user_token` (
  `user_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `token` varchar(255) DEFAULT NULL COMMENT 'token标识',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户token关联编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=454 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_version` */

DROP TABLE IF EXISTS `sys_version`;

CREATE TABLE `sys_version` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '版本编号',
  `name` varchar(50) DEFAULT NULL COMMENT '版本名称',
  `day` int(11) DEFAULT NULL COMMENT '有效期（1个月，1年）',
  `price` double(11,0) DEFAULT NULL COMMENT '价格',
  `event` int(1) DEFAULT '1' COMMENT '在线进件（1-有 0-无）',
  `ab_status` int(1) DEFAULT '1' COMMENT 'AB测试（1-有 0-无）',
  `service` varchar(50) DEFAULT NULL COMMENT '服务',
  `customizable` int(1) DEFAULT '1' COMMENT '是否可定制化（1-是 0-否）',
  `active` int(1) DEFAULT '1' COMMENT '激活状态（0：禁用，1：激活）',
  `user_num` int(20) DEFAULT NULL COMMENT '成员数量',
  `model_num` int(20) DEFAULT NULL COMMENT '模板数量',
  `rules_num` int(20) DEFAULT NULL COMMENT '规则集数量',
  `decision_num` int(20) DEFAULT NULL COMMENT '决策集数量',
  `entry_num` int(20) DEFAULT NULL COMMENT '进件数',
  `erupt_num` int(20) DEFAULT NULL COMMENT '并发数',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `creater` varchar(255) DEFAULT NULL COMMENT '创建人',
  `updater` varchar(255) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_version_log` */

DROP TABLE IF EXISTS `sys_version_log`;

CREATE TABLE `sys_version_log` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '版本日志id',
  `name` varchar(255) DEFAULT NULL COMMENT '日志名称',
  `old_version_id` int(11) DEFAULT NULL COMMENT '版本编号',
  `times` int(11) DEFAULT NULL COMMENT '周期（次数）',
  `customer_id` bigint(11) DEFAULT NULL COMMENT '客户id',
  `creater` varchar(255) DEFAULT NULL COMMENT '操作人',
  `createtime` datetime DEFAULT NULL COMMENT '操作时间',
  `message` varchar(255) DEFAULT NULL COMMENT '信息',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态（1:成功，2:失败）',
  `type` tinyint(1) DEFAULT NULL COMMENT '类型（1:续期，2:升级）',
  `price` double(11,0) DEFAULT NULL COMMENT '价格',
  `new_version_id` int(11) DEFAULT NULL COMMENT '版本编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=367 DEFAULT CHARSET=utf8;

/*Table structure for table `task_messages` */

DROP TABLE IF EXISTS `task_messages`;

CREATE TABLE `task_messages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `queue_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '对列名',
  `parameters` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '参数',
  `task_status` int(1) DEFAULT '0' COMMENT '任务执行后具体状态',
  `status` int(1) DEFAULT '0' COMMENT '状态',
  `priority` int(1) DEFAULT '0' COMMENT '优先级',
  `job_id` bigint(20) NOT NULL,
  `worker_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '执行器bean名',
  `batch_no` bigint(20) DEFAULT NULL COMMENT '批次号',
  `log_id` bigint(20) DEFAULT NULL COMMENT '关联日志id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `dispatch_time` timestamp NULL DEFAULT NULL COMMENT '分发时间',
  `execute_time` timestamp NULL DEFAULT NULL COMMENT '执行完成时间',
  `user_id` bigint(20) DEFAULT NULL COMMENT '关联用户id',
  `remark` text COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `STATUS` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=193825 DEFAULT CHARSET=utf8;

/*Table structure for table `tbl_per_info_sumarry` */

DROP TABLE IF EXISTS `tbl_per_info_sumarry`;

CREATE TABLE `tbl_per_info_sumarry` (
  `id` int(22) DEFAULT NULL,
  `report_id` varchar(30) DEFAULT NULL,
  `house_loan_count` int(22) DEFAULT NULL,
  `busi_house_loan_count` int(22) DEFAULT NULL,
  `other_loan_count` int(22) DEFAULT NULL,
  `first_loan_open_month` varchar(7) DEFAULT NULL,
  `loan_card_count` int(22) DEFAULT NULL,
  `first_loan_card_open_month` varchar(7) DEFAULT NULL,
  `standard_loan_card_count` int(22) DEFAULT NULL,
  `f_standard_loan_card_openmonth` varchar(7) DEFAULT NULL,
  `announce_count` int(22) DEFAULT NULL,
  `dissent_count` int(22) DEFAULT NULL,
  `score` varchar(20) DEFAULT NULL,
  `score_month` varchar(7) DEFAULT NULL,
  `fell_back_count` int(22) DEFAULT NULL,
  `fell_back_balance` int(22) DEFAULT NULL,
  `asset_diss_count` int(22) DEFAULT NULL,
  `asset_diss_balcnce` int(22) DEFAULT NULL,
  `assure_repay_count` int(22) DEFAULT NULL,
  `assure_repay_balance` int(22) DEFAULT NULL,
  `loan_overdue_count` int(22) DEFAULT NULL,
  `loan_overdue_months` int(22) DEFAULT NULL,
  `loan_overdue_higest_amount_p_m` int(22) DEFAULT NULL,
  `loan_overdue_max_duration` int(22) DEFAULT NULL,
  `loan_card_overdue_count` int(22) DEFAULT NULL,
  `loan_card_overdue_months` int(22) DEFAULT NULL,
  `loan_card_overdue_h_amount_p_m` int(22) DEFAULT NULL,
  `loan_card_overdue_max_duration` int(22) DEFAULT NULL,
  `s_loan_card_odue_count` int(22) DEFAULT NULL,
  `s_loan_card_odue_months` int(22) DEFAULT NULL,
  `s_loan_card_odue_h_amount_p_m` int(22) DEFAULT NULL,
  `s_loan_card_odue_max_duration` int(22) DEFAULT NULL,
  `unpaidloan_finance_corp_count` int(22) DEFAULT NULL,
  `unpaidloan_finance_org_count` int(22) DEFAULT NULL,
  `unpaidloan_account_count` int(22) DEFAULT NULL,
  `unpaidloan_credit_limit` int(22) DEFAULT NULL,
  `unpaidloan_balance` int(22) DEFAULT NULL,
  `unpaidloan_lastsix_month_repay` int(22) DEFAULT NULL,
  `undestyryloancard_fanc_count` int(22) DEFAULT NULL,
  `undestyryloancard_org_count` int(22) DEFAULT NULL,
  `undestyryloancard_accountcoun` int(22) DEFAULT NULL,
  `undestyryloancard_credit_limit` int(22) DEFAULT NULL,
  `undestyryloancard_max_credit` int(22) DEFAULT NULL,
  `undestyryloancard_min_credit` int(22) DEFAULT NULL,
  `undestyryloancard_used_credit` int(22) DEFAULT NULL,
  `undestyryloancard_used_avg_6` int(22) DEFAULT NULL,
  `undestyrysloancard_fanc_count` int(22) DEFAULT NULL,
  `undestyrysloancard_org_count` int(22) DEFAULT NULL,
  `undestyrysloancard_accountcoun` int(22) DEFAULT NULL,
  `undestyrysloancard_creditlimit` int(22) DEFAULT NULL,
  `undestyrysloancard_max_credit` int(22) DEFAULT NULL,
  `undestyrysloancard_min_credit` int(22) DEFAULT NULL,
  `undestyrysloancard_used_credit` int(22) DEFAULT NULL,
  `undestyrysloancard_used_avg_6` int(22) DEFAULT NULL,
  `guar_count` int(22) DEFAULT NULL,
  `guar_amount` int(22) DEFAULT NULL,
  `guar_balance` int(22) DEFAULT NULL,
  `app_no` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `tbl_per_loan_card_info` */

DROP TABLE IF EXISTS `tbl_per_loan_card_info`;

CREATE TABLE `tbl_per_loan_card_info` (
  `id` int(22) DEFAULT NULL,
  `report_id` varchar(30) DEFAULT NULL,
  `card_type` varchar(1) DEFAULT NULL,
  `cue` varchar(1000) DEFAULT NULL,
  `financeorg` varchar(200) DEFAULT NULL,
  `state` varchar(100) DEFAULT NULL,
  `sharecreditlimitamount` int(22) DEFAULT NULL,
  `usedcreditlimitamount` int(22) DEFAULT NULL,
  `latest6monthusedavgamount` int(22) DEFAULT NULL,
  `usedhighestamount` int(22) DEFAULT NULL,
  `scheduledpaymentdate` varchar(20) DEFAULT NULL,
  `scheduledpaymentamount` int(22) DEFAULT NULL,
  `actualpaymentamount` int(22) DEFAULT NULL,
  `recentpaydate` varchar(10) DEFAULT NULL,
  `curroverduecyc` int(22) DEFAULT NULL,
  `curroverdueamount` int(22) DEFAULT NULL,
  `loan_card_id` varchar(30) DEFAULT NULL,
  `account` varchar(50) DEFAULT NULL,
  `currency` varchar(30) DEFAULT NULL,
  `opendate` varchar(10) DEFAULT NULL,
  `creditlimitamount` int(22) DEFAULT NULL,
  `guaranteetype` varchar(200) DEFAULT NULL,
  `stateenddate` varchar(10) DEFAULT NULL,
  `stateendmonth` varchar(10) DEFAULT NULL,
  `guananteemoney` int(22) DEFAULT NULL,
  `overdue31to60amount` int(22) DEFAULT NULL,
  `overdue61to90amount` int(22) DEFAULT NULL,
  `overdue91to180amount` int(22) DEFAULT NULL,
  `overdueover180amount` int(22) DEFAULT NULL,
  `beginmonth_24month` varchar(10) DEFAULT NULL,
  `endmonth_24month` varchar(10) DEFAULT NULL,
  `last_24state` varchar(30) DEFAULT NULL,
  `beginmonth_fi` varchar(10) DEFAULT NULL,
  `endmonth_fiveyear` varchar(10) DEFAULT NULL,
  `app_no` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `tbl_per_loan_info` */

DROP TABLE IF EXISTS `tbl_per_loan_info`;

CREATE TABLE `tbl_per_loan_info` (
  `id` int(22) DEFAULT NULL,
  `report_id` varchar(30) DEFAULT NULL,
  `financeorg` varchar(200) DEFAULT NULL,
  `state` varchar(20) DEFAULT NULL,
  `stateenddate` varchar(10) DEFAULT NULL,
  `stateendmonth` varchar(10) DEFAULT NULL,
  `class5state` varchar(30) DEFAULT NULL,
  `balance` int(22) DEFAULT NULL,
  `remainpaymentcyc` int(22) DEFAULT NULL,
  `scheduledpaymentamount` int(22) DEFAULT NULL,
  `scheduledpaymentdate` varchar(10) DEFAULT NULL,
  `actualpaymentamount` int(22) DEFAULT NULL,
  `recentpaydate` varchar(10) DEFAULT NULL,
  `curroverduecyc` int(22) DEFAULT NULL,
  `curroverdueamount` int(22) DEFAULT NULL,
  `overdue31to60amount` int(22) DEFAULT NULL,
  `overdue61to90amount` int(22) DEFAULT NULL,
  `overdue91to180amount` int(22) DEFAULT NULL,
  `overdueover180amount` int(22) DEFAULT NULL,
  `last24monthbeginmonth` varchar(10) DEFAULT NULL,
  `account` varchar(50) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `currency` varchar(30) DEFAULT NULL,
  `opendate` varchar(10) DEFAULT NULL,
  `enddate` varchar(10) DEFAULT NULL,
  `creditlimitamount` int(22) DEFAULT NULL,
  `guaranteetype` varchar(50) DEFAULT NULL,
  `paymentrating` varchar(50) DEFAULT NULL,
  `paymentcyc` varchar(20) DEFAULT NULL,
  `last24monthendmonth` varchar(10) DEFAULT NULL,
  `latest24state` varchar(30) DEFAULT NULL,
  `lastfiveyearbeginmonth` varchar(10) DEFAULT NULL,
  `lastfiveyearendmonth` varchar(10) DEFAULT NULL,
  `loan_id` varchar(80) DEFAULT NULL,
  `cue` varchar(512) DEFAULT NULL,
  `app_no` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `tbl_per_persional_info` */

DROP TABLE IF EXISTS `tbl_per_persional_info`;

CREATE TABLE `tbl_per_persional_info` (
  `id` int(22) DEFAULT NULL,
  `report_id` varchar(30) DEFAULT NULL,
  `gender` varchar(15) DEFAULT NULL,
  `birthday` varchar(10) DEFAULT NULL,
  `marital_stste` varchar(25) DEFAULT NULL,
  `mobile` varchar(25) DEFAULT NULL,
  `office_telephone_no` varchar(25) DEFAULT NULL,
  `home_telephone_no` varchar(25) DEFAULT NULL,
  `edu_level` varchar(100) DEFAULT NULL,
  `edu_degree` varchar(100) DEFAULT NULL,
  `post_address` varchar(100) DEFAULT NULL,
  `registered_address` varchar(100) DEFAULT NULL,
  `spouse_name` varchar(30) DEFAULT NULL,
  `spouse_cert_type` varchar(40) DEFAULT NULL,
  `spouse_cert_no` varchar(30) DEFAULT NULL,
  `spouse_employer` varchar(60) DEFAULT NULL,
  `spouse_telephome_no` varchar(25) DEFAULT NULL,
  `app_no` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `tbl_per_result_info` */

DROP TABLE IF EXISTS `tbl_per_result_info`;

CREATE TABLE `tbl_per_result_info` (
  `id` int(22) DEFAULT NULL,
  `bar_code` varchar(32) DEFAULT NULL,
  `report_sn` varchar(24) DEFAULT NULL,
  `query_time` varchar(19) DEFAULT NULL,
  `report_create_time` varchar(19) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `cert_type` varchar(40) DEFAULT NULL,
  `cert_no` varchar(30) DEFAULT NULL,
  `query_reason` varchar(128) DEFAULT NULL,
  `qyery_format` varchar(20) DEFAULT NULL,
  `query_org` varchar(80) DEFAULT NULL,
  `user_code` varchar(64) DEFAULT NULL,
  `query_result_cue` varchar(20) DEFAULT NULL,
  `app_no` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Table structure for table `template_import_log` */

DROP TABLE IF EXISTS `template_import_log`;

CREATE TABLE `template_import_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '模版导入日志编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '导入客户编号',
  `model_id` int(11) DEFAULT NULL COMMENT '规则模版编号',
  `import_time` datetime DEFAULT NULL COMMENT '导入时间',
  `import_status` int(11) DEFAULT NULL COMMENT '导入状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=582 DEFAULT CHARSET=utf8;

/*Table structure for table `template_type` */

DROP TABLE IF EXISTS `template_type`;

CREATE TABLE `template_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '规则集模版类型编号',
  `name` varchar(255) DEFAULT NULL COMMENT '类型名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `templates` */

DROP TABLE IF EXISTS `templates`;

CREATE TABLE `templates` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '规则集模版编号',
  `ruleset_json` longtext COMMENT '规则集json串',
  `export_num` int(11) DEFAULT '0' COMMENT '导出量',
  `user_num` int(11) DEFAULT '0' COMMENT '使用量',
  `add_user` varchar(255) DEFAULT NULL COMMENT '添加人',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `describ` varchar(255) DEFAULT NULL COMMENT '描述',
  `type` int(11) DEFAULT NULL COMMENT '分类',
  `version` varchar(20) DEFAULT NULL COMMENT '版本号',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10085 DEFAULT CHARSET=utf8;

/*Table structure for table `verfied_log` */

DROP TABLE IF EXISTS `verfied_log`;

CREATE TABLE `verfied_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '鐟欏嫬鍨疘d',
  `queryTime` datetime DEFAULT NULL COMMENT '查询时间',
  `verfiedType` int(11) DEFAULT NULL COMMENT '效验类型（1.接口效验目前只有接口效验）',
  `realName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '姓名',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '接口名',
  `cid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号',
  `idNumber` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '身份证号',
  `requestStatus` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '请求状态',
  `result` longtext,
  `parameters` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '参数',
  `ruleName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '閻熸瑥瀚崹顖炲触瀹ュ泦?',
  `responseTime` bigint(20) DEFAULT NULL COMMENT '闂備礁鎲＄换鍌滅矓鐎垫瓕濮抽柡灞诲劚缁秹鏌涢锝嗙闁?',
  `channel` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '闁岸浜?',
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '1涓€閿牎楠屾爣璇嗭紝鍏朵粬鏁板瓧涓哄簳灞傛棩蹇楁爣璇?',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3959025 DEFAULT CHARSET=utf8;

/*Table structure for table `wuxi` */

DROP TABLE IF EXISTS `wuxi`;

CREATE TABLE `wuxi` (
  `id` int(20) DEFAULT NULL,
  `xml` text COMMENT '修改后的字段注释'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `wuxiwhitelist` */

DROP TABLE IF EXISTS `wuxiwhitelist`;

CREATE TABLE `wuxiwhitelist` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '姓名',
  `useridnumber` varchar(50) DEFAULT NULL COMMENT '身份证号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

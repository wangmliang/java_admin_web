
drop database fmp_db_02;
CREATE DATABASE fmp_db_02;
ALTER DATABASE fmp_db_02 DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE fmp_db_02;


-- ----------------------------
-- Table structure for attachment_group
-- ----------------------------
DROP TABLE IF EXISTS `attachment_group`;
CREATE TABLE `attachment_group` (
  `ATTACH_GROUP_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '附件组ID',
  `ATTACH_TYPE_ID` varchar(12) DEFAULT NULL COMMENT '附件类型Id（为空表示无类型）',
  `ATTACH_GROUP_STATUS` varchar(12) NOT NULL COMMENT '附件状态（temp：暂存；formal：正式）',
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '附件组创建时间',
  PRIMARY KEY (`ATTACH_GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='附件组';

-- ----------------------------
-- Records of attachment_group
-- ----------------------------

-- ----------------------------
-- Table structure for attachment_file
-- ----------------------------
DROP TABLE IF EXISTS `attachment_file`;
CREATE TABLE `attachment_file` (
  `ATTACH_FILE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文件流水号',
  `ATTACH_GROUP_ID` bigint(20) NOT NULL COMMENT '附件组ID',
  `FILE_NAME` varchar(256) NOT NULL COMMENT '文件名称',
  `FILE_SAVE_NAME` varchar(256) NOT NULL COMMENT '文件存储名',
  `FILE_SIZE` int(11) NOT NULL COMMENT '文件大小（字节为单位）',
  `FILE_TYPE` varchar(100) NOT NULL COMMENT '文件类型（doc,pdf等）',
  `ATTACH_FILE_STATUS` varchar(21) NOT NULL COMMENT '附件状态（temp：暂存；formal：正式）',
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '附件创建时间',
  PRIMARY KEY (`ATTACH_FILE_ID`),
  KEY `idx_attach_group_id` (`ATTACH_GROUP_ID`),
  CONSTRAINT `FK_attachment_file` FOREIGN KEY (`ATTACH_GROUP_ID`) REFERENCES `attachment_group` (`ATTACH_GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='附件';

-- ----------------------------
-- Records of attachment_file
-- ----------------------------

-- ----------------------------
-- Table structure for attachment_type
-- ----------------------------
DROP TABLE IF EXISTS `attachment_type`;
CREATE TABLE `attachment_type` (
  `ATTACH_TYPE_ID` varchar(12) NOT NULL COMMENT '附件类型Id',
  `ATTACH_TYPE_NAME` varchar(32) NOT NULL COMMENT '附件类型名称',
  `ATTACH_TYPE_DESC` varchar(200) NOT NULL COMMENT '附件类型描述',
  `ATTACH_COUNT_LIMIT` int(11) NOT NULL COMMENT '附件数量限制',
  `ATTACH_SIZE_LIMIT` int(11) NOT NULL COMMENT '附件大小总限制（字节为单位）',
  `SINGLE_SIZE_LIMIT` int(11) NOT NULL COMMENT '单个附件大小限制（字节为单位）',
  `FILE_SUFFIX_LIMIT` varchar(64) DEFAULT NULL COMMENT '文件后缀名限制（空表示不限制，如果支持多种后缀，通过；号分隔，如doc;pdf;rar等）',
  `SYNC_TYPE` varchar(10) DEFAULT NULL COMMENT '同步标志（同步：sync,为空表示不同步）',
  `MAX_FILE_NAME_LENGTH` int(11) NOT NULL DEFAULT '255' COMMENT '最大文件名长度,不能大于256',
  PRIMARY KEY (`ATTACH_TYPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='附件类型';

-- ----------------------------
-- Records of attachment_type
-- ----------------------------
INSERT INTO `attachment_type` VALUES ('lock_staff', 'lockStaffUploadFile', '锁定成员上传的附件', '1', '10485760', '10485760', 'doc;pdf;rar;zip;xls', '', '255');



-- ----------------------------
-- Table structure for business_log_category
-- ----------------------------
DROP TABLE IF EXISTS `business_log_category`;
CREATE TABLE `business_log_category` (
  `CATEGORY_NAME` varchar(32) NOT NULL COMMENT '日志分类名称',
  `DESCRIPTION` varchar(32) DEFAULT NULL COMMENT '分类说明',
  `PARENT_NAME` varchar(32) DEFAULT NULL COMMENT '父分类名称（一级日志分类的父分类是root）',
  `CATEGORY_LEVEL` int(11) DEFAULT NULL COMMENT '级别（1级、2级、3级等，方便查询）',
  PRIMARY KEY (`CATEGORY_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志分类';


-- ----------------------------
-- Table structure for business_log_type
-- ----------------------------
DROP TABLE IF EXISTS `business_log_type`;
CREATE TABLE `business_log_type` (
  `LOG_TYPE` varchar(20) NOT NULL COMMENT '日志类型',
  `LOG_TYPE_NAME` varchar(50) NOT NULL COMMENT '日志类型名称',
  `CATEGORY_NAME` varchar(32) NOT NULL COMMENT '日志分类名称',
  `PATTERN` varchar(500) NOT NULL COMMENT '日志操作说明模式字符串（采用java字符串的format格式）',
  PRIMARY KEY (`LOG_TYPE`),
  KEY `FK_BUSINESS_LOG_TYPE` (`CATEGORY_NAME`),
  CONSTRAINT `FK_BUSINESS_LOG_TYPE` FOREIGN KEY (`CATEGORY_NAME`) REFERENCES `business_log_category` (`CATEGORY_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志类型';


-- ----------------------------
-- Table structure for business_log
-- ----------------------------
DROP TABLE IF EXISTS `business_log`;
CREATE TABLE `business_log` (
  `LOG_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '业务日志ID',
  `LOG_TYPE` varchar(32) NOT NULL COMMENT '日志类型',
  `OPERATOR_ID` varchar(64) NOT NULL COMMENT '操作员ID',
  `OPERATOR_NAME` varchar(64) NOT NULL COMMENT '操作员名称',
  `CLIENT_IP` varchar(100) NOT NULL COMMENT '客户端IP',
  `BUSINESS_ID` varchar(64) NOT NULL COMMENT '被操作实体ID',
  `BUSINESS_NAME` varchar(64) NOT NULL COMMENT '被操作实体名称',
  `DESCRIPTION` varchar(600) NOT NULL COMMENT '操作说明',
  `OPERATION_RESULT` varchar(400) NOT NULL COMMENT '操作结果',
  `OPERATION_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `BUSINESS_TYPE` int(11) NOT NULL COMMENT '被操作实体类型',
  `OPERATION_DOMAIN` int(11) NOT NULL COMMENT '操作所属域（admin,sp）',
  PRIMARY KEY (`LOG_ID`),
  KEY `idx_business_log_type` (`LOG_TYPE`),
  KEY `idx_business_log_date` (`OPERATION_DATE`),
  KEY `idx_business_log_OPR_id` (`OPERATOR_ID`),
  CONSTRAINT `FK_BUSINESS_LOG` FOREIGN KEY (`LOG_TYPE`) REFERENCES `business_log_type` (`LOG_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务日志';

-- ----------------------------
-- Records of business_log
-- ----------------------------

-- ----------------------------
-- Table structure for province
-- ----------------------------
DROP TABLE IF EXISTS `province`;
CREATE TABLE `province` (
  `PROVINCE_ID` int(8) NOT NULL COMMENT '省份ID',
  `PROV_NAME` varchar(30) NOT NULL COMMENT '省份名称',
  `SHORT_NAME` varchar(20) DEFAULT NULL COMMENT '简称',
  PRIMARY KEY (`PROVINCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='省份';

-- ----------------------------
-- Records of province
-- ----------------------------
-- ----------------------------
-- Table structure for city
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `CITY_ID` int(8) NOT NULL COMMENT '市ID',
  `CITY_NAME` varchar(30) NOT NULL COMMENT '城市名称',
  `PROVINCE_ID` int(8) NOT NULL COMMENT '省份ID',
  `AREA_CODE` varchar(10) DEFAULT NULL COMMENT '区号',
  PRIMARY KEY (`CITY_ID`),
  KEY `FK_PROVINCE` (`PROVINCE_ID`),
  CONSTRAINT `FK_PROVINCE` FOREIGN KEY (`PROVINCE_ID`) REFERENCES `province` (`PROVINCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='市';

-- ----------------------------
-- Records of city
-- ----------------------------

-- ----------------------------
-- Table structure for links
-- ----------------------------
DROP TABLE IF EXISTS `links`;
CREATE TABLE `links` (
  `LINK_ID` tinyint(4) NOT NULL AUTO_INCREMENT COMMENT '链接ID',
  `LINK_NAME` varchar(100) NOT NULL COMMENT '链接名称',
  `DISP_TYPE` varchar(20) NOT NULL DEFAULT 'before' COMMENT '显示类型,登陆前(before),登陆后(after)',
  `LINK_TYPE` varchar(20) NOT NULL DEFAULT 'commonlink' COMMENT '链接类型,commonlink,iframe',
  `LINK_URL` varchar(200) NOT NULL COMMENT '链接URL',
  `TARGET` varchar(30) NOT NULL DEFAULT 'self' COMMENT '链接打开方式:self,popup',
  `SUB_SYSTEM` varchar(20) NOT NULL COMMENT '链接归属子系统',
  `DOMAIN` varchar(20) NOT NULL COMMENT '链接归属域',
  `NEED_RIGHT` varchar(100) DEFAULT NULL COMMENT '显示此链接需要的权限，支持三形式的权限表示：menu(menuKey, ...) role(roleKey, ...),resoper(resKey-operKey,...)分别表示权限菜单描述,权限角色描述及权限资源操作key描述.各权限间以“,”分割，表示或的关系。空表示无权限限制',
  PRIMARY KEY (`LINK_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='链接';

-- ----------------------------
-- Records of links
-- ----------------------------

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `MENU_ID` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `MENU_NAME` varchar(64) NOT NULL COMMENT '菜单名称',
  `MENU_KEY` varchar(100) NOT NULL COMMENT '菜单外码',
  `PARENT_ID` bigint(10) NOT NULL COMMENT '父菜单ID',
  `IMAGE_URL` varchar(128) DEFAULT NULL COMMENT '图标Url',
  `URL` varchar(128) DEFAULT NULL COMMENT '资源URL',
  `MENU_ORDER` varchar(6) DEFAULT NULL COMMENT '菜单顺序',
  `SUBSYSTEM` varchar(32) DEFAULT NULL COMMENT '子系统名称',
  `DOMAIN` varchar(32) DEFAULT NULL COMMENT '菜单所属域（admin、sp）',
  PRIMARY KEY (`MENU_ID`),
  UNIQUE KEY `idx_menu_key` (`MENU_KEY`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='菜单';


-- ----------------------------
-- Table structure for person_shortcut
-- ----------------------------
DROP TABLE IF EXISTS `person_shortcut`;
CREATE TABLE `person_shortcut` (
  `SHORTCUT_ID` tinyint(4) NOT NULL AUTO_INCREMENT COMMENT '快捷方式ID',
  `STAFF_ID` bigint(20) NOT NULL COMMENT '用户ID',
  `TITLE` varchar(64) NOT NULL COMMENT '标题',
  `MENU_ID` bigint(10) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`SHORTCUT_ID`),
  KEY `FK_PERSON_SHORTCUT` (`MENU_ID`),
  CONSTRAINT `FK_PERSON_SHORTCUT` FOREIGN KEY (`MENU_ID`) REFERENCES `menu` (`MENU_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户快捷方式设置';

-- ----------------------------
-- Table structure for sec_department
-- ----------------------------
DROP TABLE IF EXISTS `sec_department`;
CREATE TABLE `sec_department` (
  `DEPARTMENT_ID` bigint(16) NOT NULL AUTO_INCREMENT COMMENT '组织ID',
  `DEPARTMENT_NAME` varchar(100) NOT NULL COMMENT '组织名称',
  `DEPARTMENT_DESC` varchar(100) DEFAULT NULL COMMENT '组织描述',
  `PARENT_ID` bigint(16) DEFAULT NULL COMMENT '父级组织',
  `EMAIL` varchar(50) DEFAULT NULL COMMENT '组织的邮件',
  `ADDRESS` varchar(200) DEFAULT NULL COMMENT '组织地址',
  `ADD_SUB` varchar(1) NOT NULL DEFAULT '1' COMMENT '是否可以创建下级组织',
  `CREATE_USER` varchar(20) NOT NULL COMMENT '组织创建者',
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '组织创建时间',
  `LAST_UPDATE_DATE` timestamp NULL DEFAULT NULL COMMENT '组织最后修改时间',
  `DOMAIN` varchar(10) DEFAULT 'SYS_ADMIN' COMMENT '管理域：SP、SYS_ADMIN',
  PRIMARY KEY (`DEPARTMENT_ID`),
  KEY `FK_sec_department` (`PARENT_ID`),
  CONSTRAINT `FK_sec_department` FOREIGN KEY (`PARENT_ID`) REFERENCES `sec_department` (`DEPARTMENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织';

INSERT INTO `sec_department` VALUES ('-999', '根组织', '所有组织的根', null, 'xx@xx.com', 'xx', '0', 'system', '2016-11-01 09:14:07', '2016-11-01 09:14:07', '');
INSERT INTO `sec_department` VALUES ('-998', '测试管理组', '测试的根组织', '-999', 'xx@xx.com', 'xx', '1', 'system', '2016-11-01 09:14:07', '2016-11-01 09:14:07', 'SYS_ADMIN');

-- ----------------------------
-- Table structure for sec_role
-- ----------------------------
DROP TABLE IF EXISTS `sec_role`;
CREATE TABLE `sec_role` (
  `ROLE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `ROLE_NAME` varchar(100) NOT NULL COMMENT '角色名称',
  `ROLE_DESC` varchar(400) DEFAULT NULL COMMENT '角色描述',
  `CREATE_USER` varchar(20) NOT NULL COMMENT '角色创建者',
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '角色创建时间',
  `CAN_MODIFY` int(11) NOT NULL DEFAULT '1' COMMENT '是否允许修改 1：允许 0: 不充许 缺省=1',
  `LAST_UPDATE_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '角色最后修改时间',
  `ROLE_KEY` varchar(64) DEFAULT NULL COMMENT '角色外码',
  `AUTO_ASSIGN` int(1) NOT NULL DEFAULT '0' COMMENT '自动分配(0:不自动分配；1：自动分配给所有注册成员)',
  `visible` int(1) NOT NULL DEFAULT '1' COMMENT '是否在界面上可见(0：不可见；1：可见)',
  PRIMARY KEY (`ROLE_ID`),
  UNIQUE KEY `ROLE_KEY` (`ROLE_KEY`),
  KEY `IDX_ROLE_NAME` (`ROLE_NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of sec_role
-- ----------------------------

-- ----------------------------
-- Table structure for sec_department_role
-- ----------------------------
DROP TABLE IF EXISTS `sec_department_role`;
CREATE TABLE `sec_department_role` (
  `DEPARTMENT_ID` bigint(16) NOT NULL COMMENT '组织ID',
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`DEPARTMENT_ID`,`ROLE_ID`),
  KEY `FK_sec_department_role2` (`ROLE_ID`),
  CONSTRAINT `FK_sec_department_role` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `sec_department` (`DEPARTMENT_ID`),
  CONSTRAINT `FK_sec_department_role2` FOREIGN KEY (`ROLE_ID`) REFERENCES `sec_role` (`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织角色关联表';

-- ----------------------------
-- Records of sec_department_role
-- ----------------------------

-- ----------------------------
-- Table structure for sec_login_history
-- ----------------------------
DROP TABLE IF EXISTS `sec_login_history`;
CREATE TABLE `sec_login_history` (
  `STAFF_ID` bigint(20) NOT NULL COMMENT '成员ID',
  `LOGIN_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '登录时间',
  `LOGIN_FLAG` varchar(10) NOT NULL COMMENT 'LOGOUT：注销；SUCCESS：登录成功；FAIL：登录失败；TIMEOUT：用户超时；UNLOCK：解锁；AUTOLOCK：密码尝试过多被锁',
  `DESCRIPTION` varchar(20) DEFAULT NULL COMMENT '备注，现在用来记录用户登录密码尝试过多被锁前的用户状态',
  PRIMARY KEY (`STAFF_ID`,`LOGIN_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='成员登录日志';

-- ----------------------------
-- Records of sec_login_history
-- ----------------------------

-- ----------------------------
-- Table structure for sec_metadata_log
-- ----------------------------
DROP TABLE IF EXISTS `sec_metadata_log`;
CREATE TABLE `sec_metadata_log` (
  `LOG_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Logid',
  `HOST_NAME` varchar(250) DEFAULT NULL COMMENT '执行导入操作的主机名',
  `CLIENT_IP` varchar(32) DEFAULT NULL COMMENT '执行导入操作的主机IP',
  `FILE_NAME` varchar(500) DEFAULT NULL COMMENT '导入的文件名',
  `BACKUP_FILE_NAME` varchar(500) DEFAULT NULL COMMENT '导入的文件名备份的名字',
  `SUFFIX` varchar(20) DEFAULT NULL COMMENT '备份表的后缀',
  `OPERATION_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `OPERATOR` varchar(50) DEFAULT NULL COMMENT '操作人(操作系统的当前成员)',
  `RESULT` varchar(50) DEFAULT NULL COMMENT '导入结果',
  `OPERATION` varchar(50) DEFAULT NULL COMMENT '操作类型:delete| import',
  `METADATA_ID` varchar(500) DEFAULT NULL COMMENT '元数据ID',
  `SYSTEM_NAME` varchar(32) DEFAULT NULL COMMENT '系统名称',
  `PREFIX` varchar(5) DEFAULT NULL COMMENT '备份表的前缀',
  `domain` varchar(20) DEFAULT NULL COMMENT '管理域：SP、ADMIN',
  PRIMARY KEY (`LOG_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='元数据导入日志';

-- ----------------------------
-- Records of sec_metadata_log
-- ----------------------------

-- ----------------------------
-- Table structure for sec_resource_category
-- ----------------------------
DROP TABLE IF EXISTS `sec_resource_category`;
CREATE TABLE `sec_resource_category` (
  `CATEGORY_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源分类ID',
  `PARENT_ID` bigint(20) DEFAULT NULL COMMENT '上级资源分类ID',
  `CATEGORY_NAME` varchar(100) DEFAULT NULL COMMENT '资源分类名称',
  `CATEGORY_DESC` varchar(100) DEFAULT NULL COMMENT '资源分类描述',
  `METADATA_ID` varchar(32) DEFAULT NULL COMMENT '元数据ID',
  `CATEGORY_KEY` varchar(100) DEFAULT NULL COMMENT '资源分类外码',
  `DOMAIN` varchar(20) DEFAULT NULL COMMENT '管理域：SP、ADMIN',
  `ORDER_KEY` int(11) DEFAULT NULL COMMENT '资源分类的顺序',
  PRIMARY KEY (`CATEGORY_ID`),
  KEY `FK_sec_resource_category` (`PARENT_ID`),
  CONSTRAINT `FK_sec_resource_category` FOREIGN KEY (`PARENT_ID`) REFERENCES `sec_resource_category` (`CATEGORY_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='资源分类';

-- ----------------------------
-- Records of sec_resource_category
-- ----------------------------

-- ----------------------------
-- Table structure for sec_resources
-- ----------------------------
DROP TABLE IF EXISTS `sec_resources`;
CREATE TABLE `sec_resources` (
  `RESOURCE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `RESOURCE_KEY` varchar(100) NOT NULL COMMENT '资源外码',
  `RESOURCE_NAME` varchar(100) NOT NULL COMMENT '资源名称',
  `RESOURCE_DESC` varchar(100) DEFAULT NULL COMMENT '资源描述',
  `CATEGORY_ID` bigint(20) NOT NULL COMMENT '资源分类ID',
  `AUTH_TYPE` varchar(10) NOT NULL COMMENT '鉴权类型(AUTH：鉴权，UNAUTH：不鉴权，LOGIN_AUTH：登录鉴权)',
  `METADATA_ID` varchar(32) DEFAULT NULL COMMENT '元数据ID',
  `DOMAIN` varchar(20) DEFAULT NULL COMMENT '管理域：SP、ADMIN',
  `ORDER_KEY` int(11) DEFAULT NULL COMMENT '资源的顺序',
  PRIMARY KEY (`RESOURCE_ID`),
  UNIQUE KEY `UNQ_RESOURCE_KEY` (`RESOURCE_KEY`),
  KEY `FK_sec_resources` (`CATEGORY_ID`),
  CONSTRAINT `FK_sec_resources` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `sec_resource_category` (`CATEGORY_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='资源';

-- ----------------------------
-- Records of sec_resources
-- ----------------------------

-- ----------------------------
-- Table structure for sec_operation
-- ----------------------------
DROP TABLE IF EXISTS `sec_operation`;
CREATE TABLE `sec_operation` (
  `OPERATION_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源操作主键',
  `RESOURCE_ID` bigint(20) NOT NULL COMMENT '资源主键',
  `OPERATION_KEY` varchar(32) NOT NULL COMMENT '操作关键字',
  `OPERATION_NAME` varchar(100) NOT NULL COMMENT '操作名称',
  `OPERATION_DESC` varchar(100) DEFAULT NULL COMMENT '操作描述',
  `DEPEND_KEY` varchar(32) DEFAULT NULL COMMENT '依赖操作KEY',
  `DEPEND_BY_KEY` varchar(500) DEFAULT NULL COMMENT '被依赖操作KEY',
  `METADATA_ID` varchar(32) DEFAULT NULL COMMENT '元数据ID',
  `DOMAIN` varchar(20) DEFAULT NULL COMMENT '管理域：SP、ADMIN',
  `ORDER_KEY` int(5) DEFAULT NULL COMMENT '操作的顺序',
  PRIMARY KEY (`OPERATION_ID`),
  UNIQUE KEY `SEC_OPERATION_IDX` (`RESOURCE_ID`,`OPERATION_KEY`),
  CONSTRAINT `FK_sec_operation` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `sec_resources` (`RESOURCE_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='资源操作';

-- ----------------------------
-- Records of sec_operation
-- ----------------------------

-- ----------------------------
-- Table structure for sec_operation_address
-- ----------------------------
DROP TABLE IF EXISTS `sec_operation_address`;
CREATE TABLE `sec_operation_address` (
  `ADDRESS_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问地址ID',
  `RESOURCE_ID` bigint(20) NOT NULL COMMENT '资源ID',
  `OPERATION_KEY` varchar(32) NOT NULL COMMENT '资源操作外码',
  `OPERATION_ADDRESS_NAME` varchar(100) DEFAULT NULL COMMENT '访问地址名称',
  `OPERATION_ADDRESS_URL` varchar(128) NOT NULL COMMENT '访问地址URL（不包含协议、IP、端口、contextpath）',
  `PARAMETER_NAME1` varchar(64) DEFAULT NULL COMMENT '参数名一',
  `PARAMETER_VALUE1` varchar(64) DEFAULT NULL COMMENT '参数值一',
  `PARAMETER_NAME2` varchar(64) DEFAULT NULL COMMENT '参数名二',
  `PARAMETER_VALUE2` varchar(64) DEFAULT NULL COMMENT '参数值二',
  `PARAMETER_NAME3` varchar(64) DEFAULT NULL COMMENT '参数名三',
  `PARAMETER_VALUE3` varchar(64) DEFAULT NULL COMMENT '参数值三',
  `METADATA_ID` varchar(32) DEFAULT NULL COMMENT '元数据ID',
  `DOMAIN` varchar(20) DEFAULT NULL COMMENT '管理域：SP、ADMIN',
  PRIMARY KEY (`ADDRESS_ID`),
  UNIQUE KEY `UK_OPERATION_ADDRESS_URL` (`OPERATION_ADDRESS_URL`,`PARAMETER_NAME1`,`PARAMETER_VALUE1`,`PARAMETER_NAME2`,`PARAMETER_VALUE2`,`PARAMETER_NAME3`,`PARAMETER_VALUE3`),
  KEY `FK_sec_operation_address` (`RESOURCE_ID`,`OPERATION_KEY`),
  KEY `idx_sec_operation_address` (`OPERATION_ADDRESS_URL`),
  CONSTRAINT `FK_sec_operation_address` FOREIGN KEY (`RESOURCE_ID`, `OPERATION_KEY`) REFERENCES `sec_operation` (`RESOURCE_ID`, `OPERATION_KEY`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='资源操作地址';

-- ----------------------------
-- Records of sec_operation_address
-- ----------------------------

-- ----------------------------
-- Table structure for sec_role_resource_operation
-- ----------------------------
DROP TABLE IF EXISTS `sec_role_resource_operation`;
CREATE TABLE `sec_role_resource_operation` (
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID',
  `RESOURCE_ID` bigint(32) NOT NULL COMMENT '资源ID',
  `OPERATION_KEY` varchar(32) NOT NULL COMMENT '资源操作关键字',
  PRIMARY KEY (`ROLE_ID`,`RESOURCE_ID`,`OPERATION_KEY`),
  KEY `FK_sec_role_resource_operation` (`RESOURCE_ID`,`OPERATION_KEY`),
  CONSTRAINT `FK_sec_role_resource_operation` FOREIGN KEY (`RESOURCE_ID`, `OPERATION_KEY`) REFERENCES `sec_operation` (`RESOURCE_ID`, `OPERATION_KEY`) ON DELETE CASCADE,
  CONSTRAINT `FK_sec_role_resource_operation_role` FOREIGN KEY (`ROLE_ID`) REFERENCES `sec_role` (`ROLE_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与资源操作';


-- ----------------------------
-- Table structure for sec_staff
-- ----------------------------
DROP TABLE IF EXISTS `sec_staff`;
CREATE TABLE `sec_staff` (
  `STAFF_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '成员ID',
  `LOGIN_NAME` varchar(100) NOT NULL COMMENT '登录名',
  `DEPARTMENT_ID` bigint(16) DEFAULT NULL COMMENT '组织ID',
  `REAL_NAME` varchar(100) NOT NULL COMMENT '成员姓名',
  `PASSWORD` varchar(128) NOT NULL COMMENT '密码（经过加密）',
  `STATUS` varchar(20) NOT NULL DEFAULT 'INITIAL' COMMENT '成员状态',
  `SEX` varchar(10) DEFAULT NULL COMMENT '性别：MALE-男性；FEMALE-女性；',
  `TELEPHONE` varchar(30) DEFAULT NULL COMMENT '电话',
  `MOBILE` varchar(16) NOT NULL COMMENT '手机号码',
  `EMAIL` varchar(64) DEFAULT NULL COMMENT '邮件地址',
  `CREATE_USER` varchar(16) NOT NULL COMMENT '成员创建者',
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '成员创建时间',
  `EXPIRE_DATE` timestamp NULL DEFAULT NULL COMMENT '成员帐号过期时间',
  `LAST_UPDATE_DATE` timestamp NULL DEFAULT NULL COMMENT '成员最后修改时间',
  `PASSWORD_EXPIRE_DATE` timestamp NULL DEFAULT NULL COMMENT '密码失效时间',
  `LOCK_DATE` timestamp NULL DEFAULT NULL COMMENT '用户锁定时间',
  `CITY_ID` int(11) DEFAULT NULL COMMENT '成员所在城市（参见CITY表）',
  PRIMARY KEY (`STAFF_ID`),
  UNIQUE KEY `U_LOGIN_NAME` (`LOGIN_NAME`),
  UNIQUE KEY `U_MOBILE` (`MOBILE`),
  UNIQUE KEY `U_EMAIL` (`EMAIL`),
  KEY `idx_login_name` (`LOGIN_NAME`),
  KEY `FK_sec_staff` (`DEPARTMENT_ID`),
  CONSTRAINT `FK_sec_staff` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `sec_department` (`DEPARTMENT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='成员';

-- ----------------------------
-- Records of sec_staff
-- ----------------------------
INSERT INTO `sec_staff` VALUES ('-999', 'admin', null, '超级管理员', 'TIKrZyvcvvSW24+BghE3XQ==', 'NORMAL', 'MALE', '', '13688888888', '', 'system', '2016-07-14 10:49:00', '2016-08-12 11:31:26', '2015-08-19 16:27:18', '2016-08-12 00:00:00', '2016-11-08 09:24:09', null);

-- ----------------------------
-- Table structure for sec_staff_extend_property
-- ----------------------------
DROP TABLE IF EXISTS `sec_staff_extend_property`;
CREATE TABLE `sec_staff_extend_property` (
  `STAFF_ID` bigint(20) NOT NULL,
  `PROPERTY` varchar(32) NOT NULL,
  `VALUE` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`STAFF_ID`,`PROPERTY`),
  CONSTRAINT `FK_SEC_STAFF_EXTEND_PROPERTY` FOREIGN KEY (`STAFF_ID`) REFERENCES `sec_staff` (`STAFF_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='成员扩展属性表';

-- ----------------------------
-- Table structure for sec_password_history
-- ----------------------------
DROP TABLE IF EXISTS `sec_password_history`;
CREATE TABLE `sec_password_history` (
  `STAFF_ID` bigint(20) NOT NULL COMMENT '成员ID',
  `PASSWORD` varchar(128) NOT NULL COMMENT '密码',
  `UPDATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '变更时间',
  PRIMARY KEY (`STAFF_ID`,`UPDATE_DATE`),
  CONSTRAINT `FK_SEC_PASSWORD_HISTORY` FOREIGN KEY (`STAFF_ID`) REFERENCES `sec_staff` (`STAFF_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户密码变更日志';

-- ----------------------------
-- Records of sec_password_history
-- ----------------------------


-- ----------------------------
-- Table structure for sec_staff_department_role
-- ----------------------------
DROP TABLE IF EXISTS `sec_staff_department_role`;
CREATE TABLE `sec_staff_department_role` (
  `STAFF_ID` bigint(20) NOT NULL COMMENT '成员ID',
  `DEPARTMENT_ID` bigint(16) NOT NULL COMMENT '组织ID',
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='成员组织角色关联表';

-- ----------------------------
-- Records of sec_staff_department_role
-- ----------------------------

-- ----------------------------
-- Table structure for sec_staff_role
-- ----------------------------
DROP TABLE IF EXISTS `sec_staff_role`;
CREATE TABLE `sec_staff_role` (
  `STAFF_ID` bigint(20) NOT NULL COMMENT '成员ID',
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`STAFF_ID`,`ROLE_ID`),
  KEY `FK_sec_staff_role2` (`ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='成员角色关系表';

-- ----------------------------
-- Records of sec_staff_role
-- ----------------------------

-- ----------------------------
-- Table structure for seq_sec_department_id
-- ----------------------------
DROP TABLE IF EXISTS `seq_sec_department_id`;
CREATE TABLE `seq_sec_department_id` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `stub` char(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `stub` (`stub`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of seq_sec_department_id
-- ----------------------------

-- ----------------------------
-- Table structure for seq_sec_metadata_log_id
-- ----------------------------
DROP TABLE IF EXISTS `seq_sec_metadata_log_id`;
CREATE TABLE `seq_sec_metadata_log_id` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `stub` char(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `stub` (`stub`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of seq_sec_metadata_log_id
-- ----------------------------

-- ----------------------------
-- Table structure for seq_sec_operation_id
-- ----------------------------
DROP TABLE IF EXISTS `seq_sec_operation_id`;
CREATE TABLE `seq_sec_operation_id` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `stub` char(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `stub` (`stub`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of seq_sec_operation_id
-- ----------------------------

-- ----------------------------
-- Table structure for seq_sec_resources_id
-- ----------------------------
DROP TABLE IF EXISTS `seq_sec_resources_id`;
CREATE TABLE `seq_sec_resources_id` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `stub` char(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `stub` (`stub`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of seq_sec_resources_id
-- ----------------------------

-- ----------------------------
-- Table structure for seq_sec_resource_category_id
-- ----------------------------
DROP TABLE IF EXISTS `seq_sec_resource_category_id`;
CREATE TABLE `seq_sec_resource_category_id` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `stub` char(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `stub` (`stub`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of seq_sec_resource_category_id
-- ----------------------------

-- ----------------------------
-- Table structure for seq_sec_role_id
-- ----------------------------
DROP TABLE IF EXISTS `seq_sec_role_id`;
CREATE TABLE `seq_sec_role_id` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `stub` char(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `stub` (`stub`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of seq_sec_role_id
-- ----------------------------

-- ----------------------------
-- Table structure for seq_sec_staff_id
-- ----------------------------
DROP TABLE IF EXISTS `seq_sec_staff_id`;
CREATE TABLE `seq_sec_staff_id` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `stub` char(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `stub` (`stub`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of seq_sec_staff_id
-- ----------------------------

-- ----------------------------
-- Table structure for sub_system
-- ----------------------------
DROP TABLE IF EXISTS `sub_system`;
CREATE TABLE `sub_system` (
  `SUB_SYSTEM_ID` varchar(20) NOT NULL COMMENT '子系统ID',
  `SUB_SYSTEM_NAME` varchar(50) NOT NULL COMMENT '子系统名称',
  `SUB_SYSTEM_DESC` varchar(200) DEFAULT NULL COMMENT '子系统描述',
  `HOP_DOMAIN` varchar(200) DEFAULT NULL COMMENT 'PORTAL跳转业务系统使用的域名',
  `INTERFACE_DOMAIN` varchar(200) DEFAULT NULL,
  `DEPLOY_MODE` varchar(20) NOT NULL DEFAULT 'Remote',
  `DOMAIN` varchar(20) NOT NULL COMMENT '子系统所属的域',
  PRIMARY KEY (`SUB_SYSTEM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='子系统表';

-- ----------------------------
-- Records of sub_system
-- ----------------------------

-- ----------------------------
-- Function structure for SEQ_ATTACH_FILE_ID.Nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `SEQ_ATTACH_FILE_ID.Nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `SEQ_ATTACH_FILE_ID.Nextval`() RETURNS bigint(20)
    READS SQL DATA
BEGIN
	REPLACE INTO `SEQ_ATTACH_FILE_ID` (stub) VALUES ('SEQ_ATTACH_FILE_ID');
	return LAST_INSERT_ID();
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for SEQ_ATTACH_TYPE_ID.Nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `SEQ_ATTACH_TYPE_ID.Nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `SEQ_ATTACH_TYPE_ID.Nextval`() RETURNS bigint(20)
    READS SQL DATA
BEGIN
	REPLACE INTO SEQ_ATTACH_TYPE_ID (stub) VALUES ('SEQ_ATTACH_TYPE_ID');
	return LAST_INSERT_ID();
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for Seq_email_Id.Nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `Seq_email_Id.Nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `Seq_email_Id.Nextval`() RETURNS bigint(20)
    READS SQL DATA
BEGIN
	REPLACE INTO `SEQ_EMAIL_ID` (stub) VALUES ('SEQ_EMAIL_ID');
	RETURN LAST_INSERT_ID();
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for SEQ_SEC_DEPARTMENT_ID.Nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `SEQ_SEC_DEPARTMENT_ID.Nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `SEQ_SEC_DEPARTMENT_ID.Nextval`() RETURNS bigint(20)
    READS SQL DATA
BEGIN
	REPLACE INTO SEQ_SEC_DEPARTMENT_ID (stub) VALUES ('SEQ_SEC_DEPARTMENT_ID');
	return LAST_INSERT_ID();
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for SEQ_SEC_METADATA_LOG_ID.Nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `SEQ_SEC_METADATA_LOG_ID.Nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `SEQ_SEC_METADATA_LOG_ID.Nextval`() RETURNS bigint(20)
    READS SQL DATA
BEGIN
	REPLACE INTO SEQ_SEC_METADATA_LOG_ID (stub) VALUES ('SEQ_SEC_METADATA_LOG_ID');
	RETURN LAST_INSERT_ID();
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for SEQ_SEC_OPERATION_ID.Nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `SEQ_SEC_OPERATION_ID.Nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `SEQ_SEC_OPERATION_ID.Nextval`() RETURNS bigint(20)
    READS SQL DATA
BEGIN
	REPLACE INTO SEQ_SEC_OPERATION_ID (stub) VALUES ('SEQ_SEC_OPERATION_ID');
	RETURN LAST_INSERT_ID();
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for SEQ_SEC_RESOURCES_ID.Nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `SEQ_SEC_RESOURCES_ID.Nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `SEQ_SEC_RESOURCES_ID.Nextval`() RETURNS bigint(20)
    READS SQL DATA
BEGIN
	REPLACE INTO SEQ_SEC_RESOURCES_ID (stub) VALUES ('SEQ_SEC_RESOURCES_ID');
	RETURN LAST_INSERT_ID();
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for SEQ_SEC_RESOURCE_CATEGORY_ID.Nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `SEQ_SEC_RESOURCE_CATEGORY_ID.Nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `SEQ_SEC_RESOURCE_CATEGORY_ID.Nextval`() RETURNS bigint(20)
    READS SQL DATA
BEGIN
	REPLACE INTO SEQ_SEC_RESOURCE_CATEGORY_ID (stub) VALUES ('SEQ_SEC_RESOURCE_CATEGORY_ID');
	RETURN LAST_INSERT_ID();
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for SEQ_SEC_ROLE_ID.Nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `SEQ_SEC_ROLE_ID.Nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `SEQ_SEC_ROLE_ID.Nextval`() RETURNS bigint(20)
    READS SQL DATA
BEGIN
	REPLACE INTO SEQ_SEC_ROLE_ID (stub) VALUES ('SEQ_SEC_ROLE_ID');
	return LAST_INSERT_ID();
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for SEQ_SEC_STAFF_ID.Nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `SEQ_SEC_STAFF_ID.Nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `SEQ_SEC_STAFF_ID.Nextval`() RETURNS bigint(20)
    READS SQL DATA
BEGIN
	REPLACE INTO SEQ_SEC_STAFF_ID (stub) VALUES ('SEQ_SEC_STAFF_ID');
	return LAST_INSERT_ID();
END
;;
DELIMITER ;

------------------------------------------------------------

INSERT INTO `sec_staff_role` VALUES ('-999', '2');
INSERT INTO `sec_staff_role` VALUES ('-999', '3');
INSERT INTO `sec_staff_role` VALUES ('-999', '4');

INSERT INTO `sec_staff_department_role` VALUES ('-999', '-998', '2');
INSERT INTO `sec_staff_department_role` VALUES ('-999', '-998', '3');
INSERT INTO `sec_staff_department_role` VALUES ('-999', '-998', '4');

INSERT INTO `sub_system` VALUES ('wml-admin', 'wml-admin', 'wml-admin', '/wml-admin', '/wml-admin', 'Remote', 'admin');

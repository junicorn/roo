# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.15)
# Database: roo
# Generation Time: 2017-09-02 13:53:46 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table roo_actived
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roo_actived`;

CREATE TABLE `roo_actived` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) DEFAULT NULL,
  `email` varchar(100) NOT NULL DEFAULT '',
  `code` varchar(64) NOT NULL DEFAULT '',
  `state` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0未激活 1已激活',
  `created` datetime NOT NULL COMMENT '创建时间',
  `expired` datetime NOT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`),
  KEY `idx_code` (`code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='激活码表';

LOCK TABLES `roo_actived` WRITE;
/*!40000 ALTER TABLE `roo_actived` DISABLE KEYS */;

INSERT INTO `roo_actived` (`id`, `uid`, `email`, `code`, `state`, `created`, `expired`)
VALUES
  (6,8,'biezhi.me@gmail.com','x9nSifXDGnTkBfHnIPGy52',1,'2017-08-04 22:56:02','2017-08-05 00:56:02');

/*!40000 ALTER TABLE `roo_actived` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table roo_advert
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roo_advert`;

CREATE TABLE `roo_advert` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL DEFAULT '',
  `content` varchar(1000) DEFAULT NULL,
  `sort` int(4) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `state` tinyint(2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_sort_state` (`sort`,`state`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='广告表';



# Dump of table roo_article
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roo_article`;

CREATE TABLE `roo_article` (
  `article_id` varchar(32) NOT NULL DEFAULT '',
  `node_slug` varchar(50) NOT NULL DEFAULT '' COMMENT '所属节点',
  `node_title` varchar(50) NOT NULL DEFAULT '' COMMENT '节点名称',
  `title` varchar(200) NOT NULL DEFAULT '',
  `content` text NOT NULL,
  `username` varchar(100) NOT NULL DEFAULT '' COMMENT '创建人',
  `views` int(11) NOT NULL COMMENT '阅读数',
  `comments` int(11) NOT NULL DEFAULT '0' COMMENT '评论数',
  `likes` int(11) NOT NULL DEFAULT '0' COMMENT '喜欢数',
  `excellent` tinyint(1) NOT NULL COMMENT '是否是精华贴',
  `reply_user` varchar(32) DEFAULT NULL COMMENT '最后回复人',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updated` datetime NOT NULL COMMENT '更新时间',
  `replyed` datetime DEFAULT NULL COMMENT '最后回复时间',
  PRIMARY KEY (`article_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='文章表';



# Dump of table roo_comment
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roo_comment`;

CREATE TABLE `roo_comment` (
  `coid` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'comment表主键',
  `tid` varchar(32) NOT NULL DEFAULT '0' COMMENT 'post表主键,关联字段',
  `author` varchar(50) NOT NULL DEFAULT '' COMMENT '评论作者',
  `owner` varchar(50) NOT NULL DEFAULT '0' COMMENT '评论所属内容作者id',
  `content` text NOT NULL COMMENT '评论内容',
  `type` varchar(16) NOT NULL DEFAULT 'comment' COMMENT '评论类型',
  `created` datetime NOT NULL COMMENT '评论生成时的GMT unix时间戳',
  `state` tinyint(2) DEFAULT NULL,
  PRIMARY KEY (`coid`),
  KEY `cid` (`tid`),
  KEY `idx_tid` (`tid`),
  KEY `idx_author` (`author`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='评论表';

LOCK TABLES `roo_comment` WRITE;
/*!40000 ALTER TABLE `roo_comment` DISABLE KEYS */;

INSERT INTO `roo_comment` (`coid`, `tid`, `author`, `owner`, `content`, `type`, `created`, `state`)
VALUES
  (1,'ad8tegw91d0l','biezhi','biezhi','nice job!','comment','2017-08-01 00:00:00',1),
  (2,'ad8tegw91d0l','biezhi','biezhi','akdl bugsi.. alskla \n\nhello...\n\n\n```java\nString final msg = \"hello\";\n```','comment','2017-08-01 00:00:00',1);

/*!40000 ALTER TABLE `roo_comment` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table roo_logs
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roo_logs`;

CREATE TABLE `roo_logs` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL DEFAULT '',
  `content` varchar(1000) DEFAULT NULL,
  `ip_address` varchar(20) DEFAULT NULL,
  `created` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='系统日志表';



# Dump of table roo_node
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roo_node`;

CREATE TABLE `roo_node` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL,
  `slug` varchar(200) NOT NULL DEFAULT '',
  `title` varchar(50) NOT NULL DEFAULT '',
  `description` varchar(1000) DEFAULT NULL,
  `topics` int(11) NOT NULL,
  `state` tinyint(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='节点表';

LOCK TABLES `roo_node` WRITE;
/*!40000 ALTER TABLE `roo_node` DISABLE KEYS */;

INSERT INTO `roo_node` (`id`, `pid`, `slug`, `title`, `description`, `topics`, `state`)
VALUES
  (1,0,'share-and-explore','分享探索','',0,1),
  (2,0,'cities','城市',NULL,0,1),
  (3,2,'beijing','北京',NULL,0,1),
  (4,2,'shanghai','上海',NULL,0,1),
  (5,2,'shenzhen','深圳',NULL,0,1),
  (6,2,'hangzhou','杭州',NULL,0,1),
  (7,2,'guangzhou','广州',NULL,0,1),
  (8,2,'chengdu','成都',NULL,0,1),
  (9,2,'wuhan','武汉',NULL,0,1),
  (10,2,'xian','西安',NULL,0,1),
  (11,2,'nanjing','南京',NULL,0,1),
  (12,2,'dalian','大连',NULL,0,1),
  (13,2,'changsha','长沙',NULL,0,1),
  (14,2,'suzhou','苏州',NULL,0,1),
  (15,16,'jobs','招聘求职','',0,1),
  (16,0,'life','生活','',0,1),
  (17,1,'share','分享发现','',0,1);

/*!40000 ALTER TABLE `roo_node` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table roo_notice
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roo_notice`;

CREATE TABLE `roo_notice` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) DEFAULT '' COMMENT '标题',
  `to_user` varchar(50) NOT NULL DEFAULT '' COMMENT '发送给',
  `from_user` varchar(50) NOT NULL DEFAULT '' COMMENT '来自',
  `event` varchar(50) NOT NULL DEFAULT '' COMMENT '事件类型',
  `state` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0:未读 1:已读',
  `created` datetime NOT NULL COMMENT '通知创建时间',
  `updated` datetime DEFAULT NULL COMMENT '阅读时间',
  PRIMARY KEY (`id`),
  KEY `idx_to_user` (`to_user`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='消息通知表';



# Dump of table roo_profile
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roo_profile`;

CREATE TABLE `roo_profile` (
  `uid` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `topics` int(11) DEFAULT NULL COMMENT '发布的帖子数',
  `comments` int(11) DEFAULT NULL COMMENT '评论的帖子数',
  `favorites` int(11) DEFAULT NULL COMMENT '收藏数',
  `followers` int(11) DEFAULT NULL COMMENT '粉丝数',
  `location` varchar(200) DEFAULT NULL COMMENT '所在位置',
  `website` varchar(200) DEFAULT NULL COMMENT '个人主页',
  `github` varchar(200) DEFAULT NULL COMMENT 'github账号',
  `weibo` varchar(200) DEFAULT NULL COMMENT '微博账号',
  `signature` varchar(500) DEFAULT NULL COMMENT '个性签名',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `ux_username` (`username`),
  KEY `idx_username` (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `roo_profile` WRITE;
/*!40000 ALTER TABLE `roo_profile` DISABLE KEYS */;

INSERT INTO `roo_profile` (`uid`, `username`, `topics`, `comments`, `favorites`, `followers`, `location`, `website`, `github`, `weibo`, `signature`)
VALUES
  (8,'biezhi',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*!40000 ALTER TABLE `roo_profile` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table roo_setting
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roo_setting`;

CREATE TABLE `roo_setting` (
  `skey` varchar(50) NOT NULL DEFAULT '' COMMENT '配置键',
  `svalue` varchar(5000) DEFAULT '' COMMENT '配置值',
  `state` tinyint(2) NOT NULL COMMENT '0禁用 1正常',
  PRIMARY KEY (`skey`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='系统配置表';

LOCK TABLES `roo_setting` WRITE;
/*!40000 ALTER TABLE `roo_setting` DISABLE KEYS */;

INSERT INTO `roo_setting` (`skey`, `svalue`, `state`)
VALUES
  ('site_title','Roo 社区',1),
  ('site_users','1',1),
  ('site_url','http://127.0.0.1:9000',1),
  ('mail.smtp.host','smtp.ym.163.com',1),
  ('mail.form','Roo 社区',1),
  ('mail.username','support@roo.social',1),
  ('mail.password','renqi123',1),
  ('mail.smtp.port','465',1),
  ('mail.smtp.timeout','10000',1),
  ('mail.smtp.ssl.enable','true',1),
  ('site_topics','12',1),
  ('site_comments','3',1);

/*!40000 ALTER TABLE `roo_setting` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table roo_topic
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roo_topic`;

CREATE TABLE `roo_topic` (
  `tid` varchar(32) NOT NULL DEFAULT '',
  `node_slug` varchar(50) NOT NULL DEFAULT '' COMMENT '所属节点',
  `node_title` varchar(50) NOT NULL DEFAULT '' COMMENT '节点名称',
  `title` varchar(200) NOT NULL DEFAULT '',
  `content` text NOT NULL,
  `username` varchar(100) NOT NULL DEFAULT '' COMMENT '创建人',
  `comments` int(11) DEFAULT '0' COMMENT '评论数',
  `gains` int(11) DEFAULT '0',
  `popular` tinyint(1) DEFAULT '0' COMMENT '是否是精华贴',
  `weight` double DEFAULT '0' COMMENT '帖子权重',
  `reply_user` varchar(32) DEFAULT NULL COMMENT '最后回复人',
  `reply_id` bigint(20) DEFAULT NULL COMMENT '最后回复id',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updated` datetime NOT NULL COMMENT '更新时间',
  `replyed` datetime DEFAULT NULL COMMENT '最后回复时间',
  PRIMARY KEY (`tid`),
  KEY `idx_username` (`username`),
  KEY `idx_node_slug` (`node_slug`),
  KEY `idx_weight` (`weight`),
  KEY `idx_created` (`created`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='帖子表';

LOCK TABLES `roo_topic` WRITE;
/*!40000 ALTER TABLE `roo_topic` DISABLE KEYS */;

INSERT INTO `roo_topic` (`tid`, `node_slug`, `node_title`, `title`, `content`, `username`, `comments`, `gains`, `popular`, `weight`, `reply_user`, `reply_id`, `created`, `updated`, `replyed`)
VALUES
  ('ad8tegw91d0l','share','分享发现','大王卡一点都不划算，感觉被坑了','我偶然看到这个，觉得还不错，收购价是一折，通过快递，比较方便。\n\n我今天下了个卖单，看怎么样。目前支持北京和上海地区，2004年后的书。\n\n我们都有很多因为冲动买的书，各种原因不想再看了，现在可以处理了。','biezhi',1,0,0,NULL,NULL,NULL,'2017-08-01 00:01:00','2017-08-01 00:00:00',NULL),
  ('3qytxl7l6pz5','jobs','酷工作','蚂蚁金服 OceanBase 团队社招','我偶然看到这个，觉得还不错，收购价是一折，通过快递，比较方便。\n\n我今天下了个卖单，看怎么样。目前支持北京和上海地区，2004年后的书。\n\n我们都有很多因为冲动买的书，各种原因不想再看了，现在可以处理了。','biezhi',0,0,0,NULL,NULL,NULL,'2017-08-01 00:02:00','2017-08-01 00:00:00',NULL),
  ('jw3cxjg6ygoj','share','分享发现','	AC 7265 网卡无法跑满 200M 的电信宽带','我偶然看到这个，觉得还不错，收购价是一折，通过快递，比较方便。\n\n我今天下了个卖单，看怎么样。目前支持北京和上海地区，2004年后的书。\n\n我们都有很多因为冲动买的书，各种原因不想再看了，现在可以处理了。','biezhi',0,0,0,NULL,NULL,NULL,'2017-08-01 00:10:00','2017-08-01 00:00:00',NULL),
  ('oa4uzwg6pq0w','share','分享发现','注册了个有趣的域名，不过被墙了……','我偶然看到这个，觉得还不错，收购价是一折，通过快递，比较方便。\n\n我今天下了个卖单，看怎么样。目前支持北京和上海地区，2004年后的书。\n\n我们都有很多因为冲动买的书，各种原因不想再看了，现在可以处理了。','biezhi',0,0,0,NULL,NULL,NULL,'2017-08-01 10:00:00','2017-08-01 00:00:00',NULL),
  ('bpdhablq3qlo','share','分享发现','老哥是这样的，我在咸鱼上买到假的内存条了','我偶然看到这个，觉得还不错，收购价是一折，通过快递，比较方便。\n\n我今天下了个卖单，看怎么样。目前支持北京和上海地区，2004年后的书。\n\n我们都有很多因为冲动买的书，各种原因不想再看了，现在可以处理了。','biezhi',0,0,0,NULL,NULL,NULL,'2017-08-01 12:01:00','2017-08-01 00:00:00',NULL),
  ('dndsq4o91vwx','share','分享发现','GitHub 开发者向 Adobe 请愿 Flash 开源','我偶然看到这个，觉得还不错，收购价是一折，通过快递，比较方便。\n\n我今天下了个卖单，看怎么样。目前支持北京和上海地区，2004年后的书。\n\n我们都有很多因为冲动买的书，各种原因不想再看了，现在可以处理了。','biezhi',0,0,0,NULL,NULL,NULL,'2017-08-01 15:00:00','2017-08-01 00:00:00',NULL),
  ('p0mhzx71jqgj','share','分享发现','Windows 下有没有站立提醒的工具','我偶然看到这个，觉得还不错，收购价是一折，通过快递，比较方便。\n\n我今天下了个卖单，看怎么样。目前支持北京和上海地区，2004年后的书。\n\n我们都有很多因为冲动买的书，各种原因不想再看了，现在可以处理了。','biezhi',0,0,0,NULL,NULL,NULL,'2017-08-01 18:02:00','2017-08-01 00:00:00',NULL),
  ('kkruw3y15aay','share','分享发现','如何克服想要而得不到的情绪？','我偶然看到这个，觉得还不错，收购价是一折，通过快递，比较方便。\n\n我今天下了个卖单，看怎么样。目前支持北京和上海地区，2004年后的书。\n\n我们都有很多因为冲动买的书，各种原因不想再看了，现在可以处理了。','biezhi',0,0,0,NULL,NULL,NULL,'2017-08-01 12:20:00','2017-08-01 00:00:00',NULL),
  ('ebpi45xgo8w5','share','分享发现','wordpress 如何增加修改或者新增数据库？','我偶然看到这个，觉得还不错，收购价是一折，通过快递，比较方便。\n\n我今天下了个卖单，看怎么样。目前支持北京和上海地区，2004年后的书。\n\n我们都有很多因为冲动买的书，各种原因不想再看了，现在可以处理了。','biezhi',0,0,0,NULL,NULL,NULL,'2017-08-02 20:00:00','2017-08-01 00:00:00',NULL),
  ('wdatbval6yj3','share','分享发现','［深圳/BearyChat］ Android 高级工程师（ 15-35k）','我偶然看到这个，觉得还不错，收购价是一折，通过快递，比较方便。\n\n我今天下了个卖单，看怎么样。目前支持北京和上海地区，2004年后的书。\n\n我们都有很多因为冲动买的书，各种原因不想再看了，现在可以处理了。','biezhi',0,0,0,NULL,NULL,NULL,'2017-08-01 08:28:00','2017-08-01 00:00:00',NULL),
  ('g72lu52zz2zn5d','share','分享发现','［深圳/BearyChat］ Android 高级工程师（ 15-35k）','我偶然看到这个，觉得还不错，收购价是一折，通过快递，比较方便。\n\n我今天下了个卖单，看怎么样。目前支持北京和上海地区，2004年后的书。\n\n我们都有很多因为冲动买的书，各种原因不想再看了，现在可以处理了。','biezhi',0,0,0,NULL,NULL,NULL,'2017-08-01 08:28:01','2017-08-01 00:00:00',NULL),
  ('rngpu2e55elyxw','share','分享发现','［深圳/BearyChat］ Android 高级工程师（ 15-35k）','我偶然看到这个，觉得还不错，收购价是一折，通过快递，比较方便。\n\n我今天下了个卖单，看怎么样。目前支持北京和上海地区，2004年后的书。\n\n我们都有很多因为冲动买的书，各种原因不想再看了，现在可以处理了。','biezhi',0,0,0,NULL,NULL,NULL,'2017-08-01 08:28:03','2017-08-01 00:00:00',NULL);

/*!40000 ALTER TABLE `roo_topic` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table roo_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roo_user`;

CREATE TABLE `roo_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(100) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '用户密码',
  `email` varchar(100) DEFAULT NULL COMMENT '用户邮箱',
  `avatar` varchar(100) DEFAULT NULL,
  `role` varchar(50) NOT NULL DEFAULT 'member' COMMENT '角色',
  `created` datetime NOT NULL COMMENT '注册时间',
  `logined` datetime DEFAULT NULL COMMENT '最后一次登录时间',
  `updated` datetime DEFAULT NULL COMMENT '最后一次操作时间',
  `state` tinyint(2) NOT NULL DEFAULT '0' COMMENT '用户状态 0:未激活 1:正常 2:停用 3:注销',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uidx_username_state` (`username`,`state`),
  KEY `idx_username` (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户表';

LOCK TABLES `roo_user` WRITE;
/*!40000 ALTER TABLE `roo_user` DISABLE KEYS */;

INSERT INTO `roo_user` (`uid`, `username`, `password`, `email`, `avatar`, `role`, `created`, `logined`, `updated`, `state`)
VALUES
  (8,'biezhi','916a042f1bde53eba1e49cd59cf4eb75','biezhi.me@gmail.com',NULL,'member','2017-08-04 22:56:02',NULL,'2017-08-04 22:57:13',1);

/*!40000 ALTER TABLE `roo_user` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

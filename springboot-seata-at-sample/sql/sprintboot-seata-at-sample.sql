# db_seata
DROP SCHEMA IF EXISTS db_seata;
CREATE SCHEMA db_seata;
USE db_seata;

# Account
CREATE TABLE `account_tbl` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(255) DEFAULT NULL,
  `money` INT(11) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

INSERT INTO account_tbl (id, user_id, money)
VALUES (1, '1001', 10000);
INSERT INTO account_tbl (id, user_id, money)
VALUES (2, '1002', 10000);

# Order
CREATE TABLE `order_tbl`
(
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(255) DEFAULT NULL,
  `commodity_code` VARCHAR(255) DEFAULT NULL,
  `count` INT(11) DEFAULT '0',
  `money` INT(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

# Storage
CREATE TABLE `storage_tbl` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `commodity_code` VARCHAR(255) DEFAULT NULL,
  `count` INT(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `commodity_code` (`commodity_code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;


INSERT INTO storage_tbl (id, commodity_code, count)
VALUES (1, '2001', 1000);

CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
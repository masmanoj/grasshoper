
-- drop scripts for safe drop
-- drop table if exists g_order_history;
-- drop table if exists  g_order;
-- drop table if exists g_staff_gcm;
-- drop table if exists g_staff;
-- drop table if exists g_address;
-- drop table if exists g_user;


-- create tables


-- g_user : public users
CREATE TABLE `g_user`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`name` varchar(100)  NOT NULL,
	`email` varchar(100) Unique NOT NULL,
	`password` varchar(255) NOT NULL,
	`phone_num` varchar (12) NOT NULL,
	`image_url` varchar(100) DEFAULT 'empty_face.jpg',
	`is_active` tinyint(1) NOT NULL DEFAULT 1,
	`is_password_change_needed_on_next_login` tinyint(1) NOT NULL DEFAULT '0',
	`is_public_user` tinyint(1) NOT NULL DEFAULT '1',

	PRIMARY KEY (`id`)

  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- g_address : addresses
CREATE TABLE `g_address`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`name` varchar(100)  NOT NULL,
	`address_line1` varchar(100) NOT NULL,
	`address_line2` varchar(100) DEFAULT NULL,
	`address_line3` varchar(100) DEFAULT NULL,
	`area` varchar(100) DEFAULT NULL,
	`landmark` varchar(100) NOT NULL,
	`city` varchar(100) NOT NULL,
	`pin` varchar(6) NOT NULL,
	`contact_number` varchar(12) NOT NULL,
	`extra_info` varchar(255) DEFAULT NULL,
	`latitude` decimal(19,6) DEFAULT NULL,
	`longitude` decimal(19,6) DEFAULT NULL,
	`address_type` 	int(11) NOT NULL,
	`owner_user_id` bigint(20) DEFAULT NULL, -- created user

  PRIMARY KEY (`id`),

  CONSTRAINT `address_user_id` FOREIGN KEY (`owner_user_id`) REFERENCES `g_user` (`id`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- g_staff office staff and hopers
CREATE TABLE `g_staff`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`user_id` bigint(20) NOT NULL,
	`name` varchar(100)  NOT NULL,
	`display_name`  varchar(50)  NOT NULL,
	`role`  int(5)  NOT NULL,
	`phone_num`  varchar(12)  NOT NULL,
	`vehicle_num`  varchar(30)  NOT NULL,
	`vehicle_desc`  varchar(100)  NOT NULL,
	`id_or_dl_num`  varchar(30)  NOT NULL,
	`address_line1`  varchar(100)  NOT NULL,
	`address_line2` varchar(100)  NOT NULL,
	`address_line3` varchar(100)  DEFAULT NULL,
	`city` varchar(50) NOT NULL,
	`pin`  varchar(6) NOT NULL,
	`joining_date`  datetime NOT NULL,
	`is_active`  tinyint(1) NOT NULL DEFAULT '1',

	PRIMARY KEY (`id`),
	
	CONSTRAINT `staff_user_id` FOREIGN KEY (`user_id`) REFERENCES `g_user` (`id`)

  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- g_order : orders
CREATE TABLE `g_order`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`user_id` bigint(20) NOT NULL ,
	`name` varchar(50)  DEFAULT NULL,
	`pickup_address` bigint(20) DEFAULT NULL,
	`drop_address`  bigint(20) NOT NULL,
	`assigned_hoper` bigint(20) DEFAULT NULL,
	`additional_note` varchar(300) DEFAULT NULL,
	`status_code` int(5) NOT NULL,
	`total_amount` decimal(19,6) DEFAULT '0.000000',
	`created_user_id` bigint(20) NOT NULL,
	`created_time` datetime NOT NULL,
	`lastmodifiedby_id` bigint(20) NOT NULL,
	`lastmodified_time` datetime NOT NULL,

  PRIMARY KEY (`id`),
  CONSTRAINT `order_user_id` FOREIGN KEY (`user_id`) REFERENCES `g_user` (`id`),
  CONSTRAINT `order_address_pickup_id` FOREIGN KEY (`pickup_address`) REFERENCES `g_address` (`id`),
  CONSTRAINT `order_address_drop_id` FOREIGN KEY (`drop_address`) REFERENCES `g_address` (`id`),
  CONSTRAINT `order_created_user_id` FOREIGN KEY (`created_user_id`) REFERENCES `g_user` (`id`),
  CONSTRAINT `order_staff_id` FOREIGN KEY (`assigned_hoper`) REFERENCES `g_staff` (`id`),
  CONSTRAINT `order_lastmodifiedby_id` FOREIGN KEY (`lastmodifiedby_id`) REFERENCES `g_user` (`id`)

  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- g_order_history : public users
CREATE TABLE `g_order_history`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`order_id` bigint(20) NOT NULL,
	`status` varchar(6) NOT NULL,
	`description` varchar(255) DEFAULT NULL,
	`created_user_id` bigint(20) NOT NULL,
	`created_time` datetime NOT NULL,
	`lastmodifiedby_id` bigint(20) NOT NULL,
	`lastmodified_time` datetime NOT NULL,

	PRIMARY KEY (`id`),
	CONSTRAINT `order_history_order_id` FOREIGN KEY (`order_id`) REFERENCES `g_order` (`id`),
	CONSTRAINT `order_history_created_user_id` FOREIGN KEY (`created_user_id`) REFERENCES `g_user` (`id`),
	CONSTRAINT `order_history_lastmodifiedby_id` FOREIGN KEY (`lastmodifiedby_id`) REFERENCES `g_user` (`id`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- g_staff_gcm : staff gcm reg id
CREATE TABLE `g_staff_gcm` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`staff_id` bigint(20) NOT NULL,
	`gcm_regid` text,
	`is_active` tinyint(1) NOT NULL DEFAULT '0',
	
	PRIMARY KEY (`id`),
    CONSTRAINT `gcm_user_id` FOREIGN KEY (`staff_id`) REFERENCES `g_staff` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


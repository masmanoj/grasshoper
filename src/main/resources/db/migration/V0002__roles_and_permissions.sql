-- g_role : user roles
CREATE TABLE `g_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(500) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `hierarchy` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY  `name_unq` (`name`),
  CONSTRAINT `role_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `g_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- g_role : user roles mapping
CREATE TABLE `g_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  CONSTRAINT `user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `g_role` (`id`),
  CONSTRAINT `user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `g_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- g_role : permissions
CREATE TABLE `g_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `grouping` varchar(45) DEFAULT NULL,
  `code` varchar(100) NOT NULL,
  `entity_name` varchar(100) DEFAULT NULL,
  `action_name` varchar(100) DEFAULT NULL,
  `can_maker_checker` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- g_role : user role permission mapping
CREATE TABLE `g_role_permission` (
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`),
  CONSTRAINT `role_permission_perm_id` FOREIGN KEY (`permission_id`) REFERENCES `g_permission` (`id`),
  CONSTRAINT `role_permission_role_id` FOREIGN KEY (`role_id`) REFERENCES `g_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
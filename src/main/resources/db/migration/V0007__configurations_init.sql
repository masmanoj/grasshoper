
-- g_config : configuration options
CREATE TABLE `g_config`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`name` varchar(150) Unique NOT NULL,
	`value` text NOT NULL,
	`is_active` tinyint(1) NOT NULL DEFAULT '0',
	`is_internal` tinyint(1) NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into g_config(name, value, is_active, is_internal)
	values('default-email-user-name','bot.masmatrics@gmail.com',1,1),
	('default-email-passwd','m0a0s0m0a0t0r0i0c0s',1,1), 
	('default-email-host-name','smtp.gmail.com',1,1),
	('default-email-port','587',1,1),
	('default-email-from-name','Bot Admin',1,1);



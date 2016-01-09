-- g_user_otp : user registration table
CREATE TABLE `g_user_otp`(
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`user_id` bigint(20) NOT NULL,
	`email` varchar(100) NOT NULL,
	`otp` varchar(255) NOT NULL,
	`return_url` varchar(255) NOT NULL,
	`created_time` datetime NOT NULL,
	PRIMARY KEY (`id`),
	
	CONSTRAINT `otp_user_id` FOREIGN KEY (`user_id`) REFERENCES `g_user` (`id`),
	CONSTRAINT `otp_email` FOREIGN KEY (`email`) REFERENCES `g_user` (`email`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
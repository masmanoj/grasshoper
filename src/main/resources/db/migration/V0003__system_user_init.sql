INSERT INTO `g_user` (`id`, `name`, `email`, `password`, `phone_num`, `image_url`, `is_active`, `is_password_change_needed_on_next_login`, `is_public_user`)
VALUES
	(1, 'masmatrics', 'masmatrics', '2194e3f14948ad78fd6c4192acae6b2ea1ea01d5939f9d3b3f4a6f738687439e', '00000000', NULL, 1, 0, 0);
INSERT INTO `g_user` (`id`, `name`, `email`, `password`, `phone_num`, `image_url`, `is_active`, `is_password_change_needed_on_next_login`, `is_public_user`)
VALUES
	(2, 'masmatrics_sys', 'masmatrics_sys', 'W83jdje14944192acae_THIS_IS_DUMMY_6b2e939f9d3b2rp42342jf34rd5944rde', '00000000', NULL, 0, 0, 0);
INSERT INTO `g_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`)
VALUES
	('special', 'ALL_FUNCTIONS', NULL, NULL, 0);


INSERT INTO `g_role` (`name`, `description`, `parent_id`, `hierarchy`)
VALUES
	('Super user', 'This role provides all application permissions.', NULL, '.1.');

	
INSERT INTO `g_role_permission` (`role_id`, `permission_id`)
VALUES
	(1, 1);

INSERT INTO `g_user_role` (`user_id`, `role_id`)
VALUES
	(1, 1),
	(2, 1);

-- g_tag   : defines available codes
CREATE TABLE `g_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag` varchar(150) NOT NULL,
  `label` varchar(150) NOT NULL,
  `is_internal` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tag` (`tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- g_sub_tag
CREATE TABLE `g_sub_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag_id` bigint(20) NOT NULL,
  `sub_tag`  varchar(150) DEFAULT NULL,
  `label` varchar(150) DEFAULT NULL,
  `display_order` int(3) DEFAULT 0,
  `is_internal` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_sub_tag` (`tag_id`, `sub_tag`),
  CONSTRAINT `tag_sub_tag` FOREIGN KEY (`tag_id`) REFERENCES `g_tag`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- g_product :  the products master table
CREATE TABLE `g_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_uid` varchar(15) NOT NULL,
  `name` varchar(150) NOT NULL,
  `desc0` varchar(350) NOT NULL,
  `desc1` varchar(350) DEFAULT NULL,
  `desc2` varchar(350) DEFAULT NULL,
  `quantity` decimal(19,6) DEFAULT '0.000000',
  `quantity_unit` varchar(10) DEFAULT NULL,
  `min_quantity` decimal(19,6) DEFAULT '0.000000',
  `price_per_unit` decimal(19,6)  DEFAULT '0.000000',
  `is_sold_out`  tinyint(1) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',

  PRIMARY KEY (`id`),
   UNIQUE KEY `uid` (`product_uid`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- d_product_images :  image urls
CREATE TABLE `g_product_images` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL,
  `image_url` varchar(250) NOT NULL,
  `display_order` int(3) DEFAULT 0,
  `label` varchar(150) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),

  CONSTRAINT `image_product_id` FOREIGN KEY (`product_id`) REFERENCES `g_product` (`id`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- g_discount_rules :  discount rules
CREATE TABLE `g_discount_rules` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `coupon_code` varchar(20) DEFAULT NULL,
  `discount_rule_enum` int(5) NOT NULL,
  `sub_tag_id` bigint(20) NOT NULL,
  `applied_on_entity_enum` int(5) NOT NULL,
  `entity_id` bigint(20) DEFAULT NULL,   -- null means all
  `desc` varchar(350) DEFAULT NULL,
  `ref_value1_datatype_enum` int(5) DEFAULT NULL,
  `ref_value1` varchar(50) DEFAULT NULL,
  `ref_value2_datatype_enum` int(5) DEFAULT NULL,
  `ref_value2` varchar(50) DEFAULT NULL,
  `ref_value3_datatype_enum` int(5) DEFAULT NULL,
  `ref_value3` varchar(50) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  
  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- g_product_packing_styles :   all available packing styles for a product mapping.
CREATE TABLE `g_product_packing_styles` (
	`product_id` bigint(20) NOT NULL,
	`style_id` bigint(20) NOT NULL,
	CONSTRAINT `pkg_style_product_id` FOREIGN KEY (`product_id`) REFERENCES `g_product` (`id`),
	CONSTRAINT `pkg_style_style_id` FOREIGN KEY (`style_id`) REFERENCES `g_sub_tag` (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8; 
 
 
 -- g_product_categories :   all available gategiries for product search.
CREATE TABLE `g_product_categories` (
	`product_id` bigint(20) NOT NULL,
	`category_id` bigint(20) NOT NULL,
	CONSTRAINT `prd_cat_product_id` FOREIGN KEY (`product_id`) REFERENCES `g_product` (`id`),
	CONSTRAINT `prd_cat_category_id_id` FOREIGN KEY (`category_id`) REFERENCES `g_sub_tag` (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
 -- g_order_cart: order product mapping
 CREATE TABLE `g_order_cart` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `quantity` decimal(19,6) NOT NULL DEFAULT '0.000000',
  `pkg_style_id` bigint(20) NOT NULL,
  `item_total_amount` decimal(19,6) DEFAULT '0.000000',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UQ_m_inventory_request_cart` (`order_id`,`product_id`),
  CONSTRAINT `cart_product_id` FOREIGN KEY (`product_id`) REFERENCES `g_product` (`id`),
  CONSTRAINT `cart_order_id` FOREIGN KEY (`order_id`) REFERENCES `g_order` (`id`),
  CONSTRAINT `cart_pkg_style_id` FOREIGN KEY (`pkg_style_id`) REFERENCES `g_sub_tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- g_product_quantity_tran :  the products quantity transactionstable
CREATE TABLE `g_product_quantity_tran` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL,
  `quantity` decimal(19,6) NOT NULL DEFAULT '0.000000',
  `quantity_unit` varchar(10) not NULL,
  `tran_type_enum` int(5) NOT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `is_reverse` tinyint(1) NOT NULL DEFAULT '0',

  `created_user_id` bigint(20) NOT NULL,
  `created_time` datetime NOT NULL,
  `lastmodifiedby_id` bigint(20) NOT NULL,
  `lastmodified_time` datetime NOT NULL,

  PRIMARY KEY (`id`),

  CONSTRAINT `qty_tran_product_id` FOREIGN KEY (`product_id`) REFERENCES `g_product` (`id`),
  CONSTRAINT `qty_tran_order_id` FOREIGN KEY (`order_id`) REFERENCES `g_order` (`id`),
  CONSTRAINT `qty_tran_user_id` FOREIGN KEY (`created_user_id`) REFERENCES `g_user` (`id`),
  CONSTRAINT `qty_tran_lastmodifiedby_id` FOREIGN KEY (`lastmodifiedby_id`) REFERENCES `g_user` (`id`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

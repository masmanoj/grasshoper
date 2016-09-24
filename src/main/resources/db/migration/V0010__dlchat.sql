
CREATE TABLE `chat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `price_per_unit` smallint(5) NOT NULL,
  PRIMARY KEY (`id`)
  ) ;
  
  
  
  

CREATE TABLE `order` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
  `token_num` bigint(20) NOT NULL ,
  `total_amount` int(11) NOT NULL,
  `status` varchar(20) NOT NULL,
  `created_date` datetime not null,
  PRIMARY KEY (`id`)
  ) ;



CREATE TABLE `order_chat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `chat_id` bigint(20) NOT NULL,
  `qty` smallint(5) NOT NULL,
   `price_per_item` smallint(5) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_chat_id` FOREIGN KEY (`chat_id`) REFERENCES `chat` (`id`),
  CONSTRAINT `FK_order_id` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`)
) ;
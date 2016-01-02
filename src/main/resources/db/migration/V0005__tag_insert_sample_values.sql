INSERT ignore INTO `g_tag` (`tag`, `label`,`is_internal`)
VALUES
	('PackagingStyles', 'Cutting Styles', 0),
	('ProductCategories', 'Item Categories', 0),
	('ProductSortOrder', 'Product Sort Order', 0),
	('ProductSortOrderQry', 'ProductSortOrderInternal', 1);

INSERT Ignore INTO `g_sub_tag` (`tag_id`, `sub_tag`, `label`, `display_order`, `is_internal`)
VALUES
	((select id from g_tag where tag = 'PackagingStyles'), 'DonotCut','Do not Cut',0,0),
	((select id from g_tag where tag = 'PackagingStyles'), 'LargePieces','Large Pieces',1,0),
	((select id from g_tag where tag = 'PackagingStyles'), 'MediumPieces','Medium Pieces',2,0),
	((select id from g_tag where tag = 'PackagingStyles'), 'SmallPieces','Small Pieces',3,0),
	((select id from g_tag where tag = 'ProductCategories'), 'FreshFish','Fresh Fish',0,0),
	((select id from g_tag where tag = 'ProductCategories'), 'ShellFish','Shell Fish',1,0),
	((select id from g_tag where tag = 'ProductCategories'), 'FrozenFish','Frozen Fish',2,0),
	((select id from g_tag where tag = 'ProductSortOrder'), 'PriceASC','Price Low to High',0,0),
	((select id from g_tag where tag = 'ProductSortOrder'), 'PriceDSC','Price High to Low',1,0),
	((select id from g_tag where tag = 'ProductSortOrderQry'), 'price_per_unit','PriceASC',0,1),
	((select id from g_tag where tag = 'ProductSortOrderQry'), 'price_per_unit desc','PriceDSC',1,1);
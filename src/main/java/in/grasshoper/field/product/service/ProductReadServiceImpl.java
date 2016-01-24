package in.grasshoper.field.product.service;

import static in.grasshoper.field.product.productConstants.ProductCategoriesTagName;
import static in.grasshoper.field.product.productConstants.ProductPackingStyleTagName;
import static in.grasshoper.field.product.productConstants.ProductSortOrderQueryTagName;
import static in.grasshoper.field.product.productConstants.ProductSortOrderTagName;
import in.grasshoper.field.product.data.ProductData;
import in.grasshoper.field.product.data.ProductImageData;
import in.grasshoper.field.tag.data.SubTagData;
import in.grasshoper.field.tag.service.TagReadService;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductReadServiceImpl implements ProductReadService {
	private final JdbcTemplate jdbcTemplate;
	private final TagReadService tagReadService;
	@Autowired
	private ProductReadServiceImpl(final DataSource dataSource, final TagReadService tagReadService) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.tagReadService = tagReadService;
	}
	

	@Override
	public Collection<ProductData> retriveAll(){
		final ProductRowMapper rowMapper = new ProductRowMapper(this, false);
		final String sql = "select " + rowMapper.schema() ;
		return this.jdbcTemplate.query(sql, rowMapper);
	}
	
	@Override
	public ProductData retriveOne(final Long productId){
		final ProductRowMapper rowMapper = new ProductRowMapper(this, false);
		final String sql = "select " + rowMapper.schema() + " where id = ? " ;
		return this.jdbcTemplate.queryForObject(sql, rowMapper, productId);
	}
	
	private Collection<SubTagData> fetchPackagingStyles(final Long productId, final boolean hideId){
		final ProductPkgStylesRowMapper pksMapper = new ProductPkgStylesRowMapper(hideId);
		final String sql = "select " + pksMapper.schema() + "where pks.product_id = ? " ;
		return this.jdbcTemplate.query(sql, pksMapper, productId);
	}
	
	private Collection<SubTagData> fetchCategories(final Long productId, final boolean hideId){
		final ProductCategoriesRowMapper catMapper = new ProductCategoriesRowMapper(hideId);
		final String sql = "select " + catMapper.schema() + "where cat.product_id = ? " ;
		return this.jdbcTemplate.query(sql, catMapper, productId);
	}
	
	private Collection<ProductImageData> fetchProductImages(final Long productId, final boolean hideId){
		final ProductImagesRowMapper imgRowMapper = new ProductImagesRowMapper(hideId);
		final StringBuffer sql =new StringBuffer().append( "select " + imgRowMapper.schema() + "where img.product_id = ? ") ;
		if(hideId)
			sql.append(" and img.is_active = true");
		return this.jdbcTemplate.query(sql.toString(), imgRowMapper, productId);
	}
	
	private static final class ProductRowMapper implements RowMapper<ProductData>{
		final ProductReadServiceImpl thisReadSrv ;
		final Boolean hide;
		public ProductRowMapper(ProductReadServiceImpl readService, Boolean hideId){
			this.thisReadSrv = readService;
			this.hide = hideId;
		}
		
		public String schema(){
			return new StringBuilder()
			.append(" id, product_uid , name, desc0, desc1, desc2,")
			.append(" quantity, quantity_unit, is_sold_out, is_active, price_per_unit, ")
			.append(" min_quantity ")
			.append(" from g_product ").toString();
		}
		public String publicSchema(){
			return new StringBuilder()
			.append(" p.id as id, product_uid , name, desc0, desc1, desc2,")
			.append(" quantity, quantity_unit, is_sold_out, is_active, price_per_unit, ")
			.append(" min_quantity ")
			.append(" from g_product p ")
			.toString();
		}
		@Override
		public ProductData mapRow(final ResultSet rs,  @SuppressWarnings("unused") final int rowNum) throws SQLException {
			final Long id = rs.getLong("id");
			final String productUid = rs.getString("product_uid");
			final String name = rs.getString("name");
			final String desc0 = rs.getString("desc0");
			final String desc1 = rs.getString("desc1");
			final String desc2 = rs.getString("desc2");
			final BigDecimal quantity = rs.getBigDecimal("quantity");
			final BigDecimal pricerPerUnit = rs.getBigDecimal("price_per_unit");
			final BigDecimal minQuantity  = rs.getBigDecimal("min_quantity");
			final String quantityUnit = rs.getString("quantity_unit");
			final Boolean isSoldOut = rs.getBoolean("is_sold_out");
			final Boolean isActive = rs.getBoolean("is_active");
			final Collection<SubTagData> packagingStyles =  this.thisReadSrv.fetchPackagingStyles(id, this.hide);
			final Collection<ProductImageData> productImages = this.thisReadSrv.fetchProductImages(id, this.hide);
			final Collection<SubTagData> categories = this.thisReadSrv.fetchCategories(id, this.hide);
			
			
			return ProductData.createNew((this.hide)? null : id, name, productUid, desc0, desc1, desc2, 
					(this.hide)? null :quantity, quantityUnit, isSoldOut, (this.hide)? null :isActive, pricerPerUnit, minQuantity, 
							packagingStyles, categories, productImages);
		}	
	}
	
	private static final class ProductPkgStylesRowMapper implements RowMapper<SubTagData>{
		private final boolean hide;
		public ProductPkgStylesRowMapper(final boolean hide) {
			this.hide = hide;
		}
		public String schema(){
			return new StringBuilder()
			.append(" st.id subTagId, st.tag_id tagId, st.sub_tag subTag, st.label subTagLabel,") 
			.append(" st.display_order displayOrder") 
			.append(" from  g_product_packing_styles pks")
 
			.append(" left join g_sub_tag st on st.id = style_id ").toString();
		}
		@Override
		public SubTagData mapRow(final ResultSet rs,  @SuppressWarnings("unused") final int rowNum) throws SQLException {
			final Long tagId = rs.getLong("tagId");
			final Long id = rs.getLong("subTagId");
			final String subTag = rs.getString("subTag");
			final String subTagLabel = rs.getString("subTagLabel");
			final Integer displayOrder = rs.getInt("displayOrder");
			return SubTagData.createNew((this.hide)? null :id, (this.hide)? null :tagId, subTag, subTagLabel,
					displayOrder, null, null);
		}	
	}
	
	private static final class ProductCategoriesRowMapper implements RowMapper<SubTagData>{
		private final boolean hide;
		public ProductCategoriesRowMapper(final boolean hide) {
			this.hide = hide;
		}
		public String schema(){
			return new StringBuilder()
			.append(" st.id subTagId, st.tag_id tagId, st.sub_tag subTag, st.label subTagLabel,") 
			.append(" st.display_order displayOrder") 
			.append(" from  g_product_categories cat")
 
			.append(" left join g_sub_tag st on st.id = category_id ").toString();
		}
		@Override
		public SubTagData mapRow(final ResultSet rs,  @SuppressWarnings("unused") final int rowNum) throws SQLException {
			final Long tagId = rs.getLong("tagId");
			final Long id = rs.getLong("subTagId");
			final String subTag = rs.getString("subTag");
			final String subTagLabel = rs.getString("subTagLabel");
			final Integer displayOrder = rs.getInt("displayOrder");
			return SubTagData.createNew((this.hide)? null :id, (this.hide)? null :tagId, subTag, subTagLabel,
					displayOrder, null, null);
		}	
	}
	private static final class ProductImagesRowMapper implements RowMapper<ProductImageData>{
		private final boolean hide;
		public ProductImagesRowMapper(final boolean hide) {
			this.hide = hide;
		}
		public String schema(){
			return new StringBuilder()
			.append(" img.id, img.product_id, img.image_url, img.display_order,")
			.append(" img.is_active, img.label")

			.append(" from g_product_images img ").toString();
		}
		@Override
		public ProductImageData mapRow(final ResultSet rs,  @SuppressWarnings("unused") final int rowNum) throws SQLException {
			final Long id = rs.getLong("id");
			final Long productId = rs.getLong("product_id");
			final String imageUrl = rs.getString("image_url");
			final String label = rs.getString("label");
			final Integer displayOrder = rs.getInt("display_order");
			final Boolean isActive = rs.getBoolean("is_active");
			return ProductImageData.createNew((this.hide)? null :id, (this.hide)? null :productId, imageUrl, displayOrder, label, (this.hide)? null :isActive);
		}	
	}
	
	@Override
	public ProductData generateTemplate(){
		Collection<SubTagData> allPkgingStyles = tagReadService.retriveAllSubTagsForTag(ProductPackingStyleTagName);
		Collection<SubTagData> allCategories = tagReadService.retriveAllSubTagsForTag(ProductCategoriesTagName);
		Collection<SubTagData> allsortOrders = tagReadService.retriveAllSubTagsForTag(ProductSortOrderTagName);
		return ProductData.tamplate(allPkgingStyles, allCategories, allsortOrders);
	}
		
	/* public Api services */
	@Override
	public Collection<ProductData> retriveAllProductsSearch(final String searchQry, final Boolean notSoldOut,
			final Integer limit, final Integer offset, final String orderby, final String category,
			String productUId){
		final ProductRowMapper rowMapper = new ProductRowMapper(this, true);
		final StringBuffer sql = new StringBuffer()
				.append("select " + rowMapper.publicSchema())
				.append(" where p.is_active = true ");
		boolean isCategoryOn = true;
		if(StringUtils.isNotBlank(searchQry) && !searchQry.contains("'")){
			sql.append(" and ( p.name like '%").append(searchQry).append("%' ")
			.append(" or p.desc0 like '%").append(searchQry).append("%' ")
			.append(" or p.desc1 like '%").append(searchQry).append("%' ")
			.append(" or p.desc2 like '%").append(searchQry).append("%' ) ");
			isCategoryOn = false;
		}
		if(null != productUId && !productUId.isEmpty() && !productUId.contains("'")){
			sql.append(" and p.product_uid = '" + productUId +"'");
		}
		
		if(null != notSoldOut && notSoldOut){
			sql.append(" and p.is_sold_out = false");
		}
		
		if(isCategoryOn && StringUtils.isNotBlank(category) && !category.contains("'")){
			String catTkns[]  = category.split(",");
			List<Long> catIds =  new ArrayList<>();
			for(String eachCatgry : catTkns){
				if(StringUtils.isNotBlank(eachCatgry)){
					SubTagData catogorySubTag = this.tagReadService.retriveOneSubTag(ProductCategoriesTagName, eachCatgry);
					catIds.add(catogorySubTag.getId());
				}
			}
			if(!catIds.isEmpty()){
				sql.append(" and p.id in ( select product_id from g_product_categories where category_id in (")
				.append(StringUtils.join(catIds, ","))
				.append(")) ");
			}
		}
		
		if(StringUtils.isNotBlank(orderby)){
			SubTagData sortQryData = tagReadService.retriveOneInternalSubTagsForTagAndSubTagLabel(
					ProductSortOrderQueryTagName, orderby);
			if(sortQryData != null){
				sql.append(" order by "+sortQryData.getSubTag());
			}
		}
		if(limit != null && ! (limit < 0)){
			if(offset !=null){
				sql.append(" limit "+offset+", "+limit);
			}else
				sql.append(" limit "+limit);
		}
		return this.jdbcTemplate.query(sql.toString(), rowMapper);
	}
}

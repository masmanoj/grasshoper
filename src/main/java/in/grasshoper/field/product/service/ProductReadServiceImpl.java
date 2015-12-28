package in.grasshoper.field.product.service;

import static in.grasshoper.field.product.productConstants.ProductPackingStyleTagName;
import in.grasshoper.field.product.data.ProductData;
import in.grasshoper.field.product.data.ProductImageData;
import in.grasshoper.field.tag.data.SubTagData;
import in.grasshoper.field.tag.service.TagReadService;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

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
		final ProductRowMapper rowMapper = new ProductRowMapper();
		final String sql = "select " + rowMapper.schema() ;
		return this.jdbcTemplate.query(sql, rowMapper);
	}
	
	@Override
	public ProductData retriveOne(final Long productId){
		final ProductRowMapper rowMapper = new ProductRowMapper();
		final String sql = "select " + rowMapper.schema() + " where id = ? " ;
		final ProductData productData =  this.jdbcTemplate.queryForObject(sql, rowMapper, productId);
		final Collection<SubTagData> packagingStyles =  fetchPackagingStyles(productId);
		final Collection<ProductImageData> productImages = fetchProductImages(productId);
		
		return ProductData.createNew(productData, packagingStyles, productImages);
	}
	
	private Collection<SubTagData> fetchPackagingStyles(final Long productId){
		final ProductPkgStylesRowMapper pksMapper = new ProductPkgStylesRowMapper();
		final String sql = "select " + pksMapper.schema() + "where pks.product_id = ? " ;
		return this.jdbcTemplate.query(sql, pksMapper, productId);
	}
	
	private Collection<ProductImageData> fetchProductImages(final Long productId){
		final ProductImagesRowMapper imgRowMapper = new ProductImagesRowMapper();
		final String sql = "select " + imgRowMapper.schema() + "where img.product_id = ? " ;
		return this.jdbcTemplate.query(sql, imgRowMapper, productId);
	}
	
	private static final class ProductRowMapper implements RowMapper<ProductData>{
		
		public String schema(){
			return new StringBuilder()
			.append(" id, product_uid , name, desc0, desc1, desc2,")
			.append(" quantity, quantity_unit, is_sold_out, is_active, price_per_unit, ")
			.append(" min_quantity ")
			.append(" from g_product ").toString();
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
			
			return ProductData.createNew(id, name, productUid, desc0, desc1, desc2, 
					quantity, quantityUnit, isSoldOut, isActive, pricerPerUnit, minQuantity);
		}	
	}
	
	private static final class ProductPkgStylesRowMapper implements RowMapper<SubTagData>{
		
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
			return SubTagData.createNew(id, tagId, subTag, subTagLabel,
					displayOrder, null, null);
		}	
	}
	
	private static final class ProductImagesRowMapper implements RowMapper<ProductImageData>{
		
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
			return ProductImageData.createNew(id, productId, imageUrl, displayOrder, label, isActive);
		}	
	}
	
	@Override
	public ProductData generateTemplate(){
		Collection<SubTagData> allPkgingStyles = tagReadService.retriveAllSubTagsForTag(ProductPackingStyleTagName);
		return ProductData.tamplate(allPkgingStyles);
	}
	
}

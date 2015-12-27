package in.grasshoper.field.product.service;

import in.grasshoper.field.product.data.ProductData;

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
	@Autowired
	private ProductReadServiceImpl(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
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
		final String sql = "select " + rowMapper.schema() + "where id = ? " ;
		return this.jdbcTemplate.queryForObject(sql, rowMapper, productId);
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
}

package in.grasshoper.field.address.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import in.grasshoper.core.security.service.PlatformSecurityContext;
import in.grasshoper.field.address.data.AddressData;
import in.grasshoper.user.domain.User;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class AddressReadServiceImpl implements AddressReadService {
	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;
	
	@Autowired
	private AddressReadServiceImpl(final DataSource dataSource,
			final PlatformSecurityContext context) {
		 //this.jdbcTemplate = new JdbcTemplate();
		 //this.jdbcTemplate.setDataSource(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.context = context;
	}
	
	
	//public api
	@Override
	public Collection<AddressData> retriveAllAddresOfLogedInUser(){
		final User thisUser = this.context.authenticatedUser();
		final Long userId = thisUser.getId();
		final AddressRowMapper rowMapper = new AddressRowMapper(true);
		final String sql = "select " + rowMapper.schema() + " where a.owner_user_id = ? and is_deleted = false ";
		return this.jdbcTemplate.query(sql, rowMapper, new Object[] {userId});
	}
	
	private static final class AddressRowMapper implements RowMapper<AddressData> {
		private final boolean hide;
		public AddressRowMapper(final boolean hide) {
			this.hide = hide;
		}
		public String schema(){
			final StringBuilder builder = new StringBuilder();
			
			builder.append(" id, name, address_line1, address_line2, ");
			builder.append(" address_line3, area, landmark, city, pin, ");
			builder.append(" contact_number, extra_info, latitude, ");
			builder.append(" longitude, address_type, owner_user_id ");
			builder.append(" from g_address a ");
			return builder.toString();
		}
		
		@Override
		public AddressData mapRow(ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
			final Long id = rs.getLong("id");
			final String addressLine1 = rs.getString("address_line1");
			final String name = rs.getString("name");
			final String addressLine2 = rs.getString("address_line2");
			final String addressLine3 = rs.getString("address_line3");
			final String area = rs.getString("area");
			final String landmark = rs.getString("landmark");
			final String city = rs.getString("city");
			final String pin = rs.getString("pin");
			final String contactNumber = rs.getString("contact_number");
			final String extraInfo = rs.getString("extra_info");
			final BigDecimal latitude = rs.getBigDecimal("latitude");
			final BigDecimal longitude = rs.getBigDecimal("latitude");
			final Integer addressType = rs.getInt("address_type");
			final Long ownerUserId = rs.getLong("owner_user_id");

			
			return AddressData.createNew(id, name, addressLine1,
					addressLine2, addressLine3, area, landmark, city, pin, 
					contactNumber, extraInfo, latitude, longitude, 
					addressType, (this.hide) ? null : ownerUserId);
		}
		
	}
	@Override
	public boolean isaddressLinkedwithOrder(final Long addressId){
		String qry  = "select count(1) from g_order where drop_address = ? or pickup_address = ?";
		 Integer count = this.jdbcTemplate.queryForObject(qry, Integer.class, new Object[] {addressId, addressId});
		 return (count!=null && count > 0 );
	}
}

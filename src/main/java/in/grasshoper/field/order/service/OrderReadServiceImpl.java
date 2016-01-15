package in.grasshoper.field.order.service;

import in.grasshoper.field.address.data.AddressData;
import in.grasshoper.field.order.data.OrderData;
import in.grasshoper.field.order.domain.OrderStatus;
import in.grasshoper.user.data.UserData;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderReadServiceImpl implements OrderReadService{

	private final JdbcTemplate jdbcTemplate;
	@Autowired
	private OrderReadServiceImpl(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public Integer getNewOrderCount(){
		String sql = "select count(1) from g_order where status_code = ?" ;
		final Integer count = this.jdbcTemplate.queryForObject(sql, Integer.class, OrderStatus.Received.getStatusCode());
		return count;
	}
	
	
	
	private static final class OrderRowMapper implements  RowMapper<OrderData>{

		public String schema(){
			return new StringBuilder()
			.append("o.id orderId, o.name orderName, o.assigned_hoper hoper,  ")
			.append("o.additional_note additionalNote, o.status_code statusCode,  ")
			.append("o.total_amount total_amount, pa.id paddrId, ")
			.append("pa.name paddrName, pa.address_line1 paddrLn1, ")
			.append("pa.address_line2 paddrLn2, pa.address_line3 paddrLn3, ")
			.append("pa.area parea, pa.landmark plandmark, pa.city pcity, pa.pin ppin, ")
			.append("pa.contact_number pcontactNum, pa.extra_info ppextraInfo, pa.latitude plat, ")
			.append("pa.longitude plong, pa.address_type ptype, pa.owner_user_id puserId, ")
			.append("da.id daddrId, da.name daddrName, da.address_line1 daddrLn1, ")
			.append("da.address_line2 daddrLn2, da.address_line3 daddrLn3, da.area darea, ")
			.append("da.landmark dlandmark, da.city dcity, da.pin dpin, da.contact_number dcontactNum, ")
			.append("da.extra_info dpextraInfo, da.latitude dlat, da.longitude dlong, ")
			.append("da.address_type dtype, da.owner_user_id duserId, u.id userId, ")
			.append("u.name userName, u.email userEmail, u.phone_num userPhone, ")
			.append("u.image_url userImgUrl, u.is_active userIsActive  ")
			.append("from g_order o  ")
			.append("inner join g_user u on u.id = o.user_id ")
			.append("left join g_address pa on pa.id = o.pickup_address ")
			.append("left join g_address da on da.id = o.drop_address ")
			.toString();
		}
		@Override
		public OrderData mapRow(ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
			
			final Long orderId = rs.getLong("orderId");
			final String orderName = rs.getString("orderName");
			//final Long hoper = rs.getLong("hoper");
			final String additionalNote = rs.getString("additionalNote");
			final Integer statusCode = rs.getInt("statusCode");
			final BigDecimal totalamount = rs.getBigDecimal("total_amount");
			final Long paddrId = rs.getLong("paddrId");
			final String paddrName = rs.getString("paddrName");
			final String paddrLn1 = rs.getString("paddrLn1");
			final String paddrLn2 = rs.getString("paddrLn2");
			final String paddrLn3 = rs.getString("paddrLn3");
			final String parea = rs.getString("parea");
			final String plandmark = rs.getString("plandmark");
			final String pcity = rs.getString("pcity");
			final String ppin = rs.getString("ppin");
			final String pcontactNum = rs.getString("pcontactNum");
			final String ppextraInfo = rs.getString("ppextraInfo");
			final BigDecimal plat = rs.getBigDecimal("plat");
			final BigDecimal plong = rs.getBigDecimal("plong");
			final Integer ptype = rs.getInt("ptype");
			final Long puserId = rs.getLong("puserId");
			final Long daddrId = rs.getLong("daddrId");
			final String daddrName = rs.getString("daddrName");
			final String daddrLn1 = rs.getString("daddrLn1");
			final String daddrLn2 = rs.getString("daddrLn2");
			final String daddrLn3 = rs.getString("daddrLn3");
			final String darea = rs.getString("darea");
			final String dlandmark = rs.getString("dlandmark");
			final String dcity = rs.getString("dcity");
			final String dpin = rs.getString("dpin");
			final String dcontactNum = rs.getString("dcontactNum");
			final String dpextraInfo = rs.getString("dpextraInfo");
			final BigDecimal dlat = rs.getBigDecimal("dlat");
			final BigDecimal dlong = rs.getBigDecimal("dlong");
			final Integer dtype = rs.getInt("dtype");
			final Long duserId = rs.getLong("duserId");
			final Long userId = rs.getLong("userId");
			final String userName = rs.getString("userName");
			final String userEmail = rs.getString("userEmail");
			final String userPhone = rs.getString("userPhone");
			final String userImgUrl = rs.getString("userImgUrl");
			final Boolean userIsActiv = rs.getBoolean("userIsActiv");
			final UserData user =  UserData.createNew(userId, userName, userEmail, userPhone, userImgUrl,
					userIsActiv, null, null, null);
			final AddressData pickupAddress = AddressData.createNew(paddrId, paddrName, paddrLn1, 
					paddrLn2, paddrLn3, parea, plandmark, pcity, ppin, pcontactNum, 
					ppextraInfo, plat, plong, ptype, puserId);
			final AddressData dropAddress = AddressData.createNew(daddrId, daddrName, daddrLn1, 
					daddrLn2, daddrLn3, darea, dlandmark, dcity, dpin, dcontactNum, 
					dpextraInfo, dlat, dlong, dtype, duserId);
			final OrderStatus status = OrderStatus.fromInt(statusCode);
			return OrderData.createNew(orderId, userId, user, orderName, pickupAddress,
					dropAddress, additionalNote, statusCode, status.getStatusName(), 
					totalamount, null, null);
					//,orderCart, orderHistory)
		}
		
	}
	
	private static final class OrderCartRowMapper implements  RowMapper<OrderData>{
		public String schema(){
			return new StringBuilder()
			
			.toString();
		}

		@Override
		public OrderData mapRow(ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	
}

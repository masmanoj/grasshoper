package in.grasshoper.field.order.service;

import in.grasshoper.field.address.data.AddressData;
import in.grasshoper.field.order.data.OrderCartData;
import in.grasshoper.field.order.data.OrderData;
import in.grasshoper.field.order.data.OrderHistoryData;
import in.grasshoper.field.order.domain.OrderStatus;
import in.grasshoper.user.data.UserData;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.joda.time.LocalDateTime;
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
	@Override
	public Collection<OrderData> retriveAll(Integer limit, Integer offset, Integer statusCode){
		final OrderRowMapper rowMapper = new OrderRowMapper(this, false, true);
		StringBuilder sql = new StringBuilder();
		sql.append("select ").append( rowMapper.schema() ); 
		if(statusCode != null){
			sql.append(" where  o.status_code = ? ");
		}
		sql.append(" order by o.created_time ");
		if(limit != null){
			if(offset !=null){
				sql.append(" limit ").append(offset).append(", ").append(limit);
			}else
				sql.append(" limit ").append(limit);
		}
		
		if(statusCode != null)
			return this.jdbcTemplate.query(sql.toString(), rowMapper,statusCode);
		return this.jdbcTemplate.query(sql.toString(), rowMapper);
		
	}
	
	@Override
	public OrderData retriveOne(final Long orderId){
		final OrderRowMapper rowMapper = new OrderRowMapper(this, false, false);
		final String sql = "select " + rowMapper.schema() + " where o.id = ? ";
		return this.jdbcTemplate.queryForObject(sql, rowMapper, orderId);
	}
	
	private Collection<OrderCartData> fetchOrderCart(final Long orderid , final boolean hideId){
		final OrderCartRowMapper cartRowMapper = new OrderCartRowMapper(hideId);
		final StringBuffer sql =new StringBuffer().append( "select " + cartRowMapper.schema() + "where o.order_id = ? ") ;
		return this.jdbcTemplate.query(sql.toString(), cartRowMapper, orderid);
	}
	
	private Collection<OrderHistoryData> fetchOrderHistory(final Long orderid , final boolean hideId){
		final OrderHistoryRowMapper historyRowMapper = new OrderHistoryRowMapper(hideId);
		final StringBuffer sql =new StringBuffer().append( "select " + historyRowMapper.schema() + "where h.order_id = ? ")
				.append(" order by created_time desc");
		return this.jdbcTemplate.query(sql.toString(), historyRowMapper, orderid);
	}
	
	private static final class OrderRowMapper implements  RowMapper<OrderData>{
		
		final OrderReadServiceImpl thisReadSrv ;
		final Boolean hide;
		final Boolean listOnly;
		public OrderRowMapper(OrderReadServiceImpl readService, Boolean hideId, Boolean listOnly){
			this.thisReadSrv = readService;
			this.hide = hideId;
			this.listOnly  = listOnly;
		}

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
			final Boolean userIsActiv = rs.getBoolean("userIsActive");
			final UserData user =  UserData.createNew(userId, userName, userEmail, userPhone, userImgUrl,
					userIsActiv, null, null, null);
			final AddressData pickupAddress = AddressData.createNew(paddrId, paddrName, paddrLn1, 
					paddrLn2, paddrLn3, parea, plandmark, pcity, ppin, pcontactNum, 
					ppextraInfo, plat, plong, ptype, puserId);
			final AddressData dropAddress = AddressData.createNew(daddrId, daddrName, daddrLn1, 
					daddrLn2, daddrLn3, darea, dlandmark, dcity, dpin, dcontactNum, 
					dpextraInfo, dlat, dlong, dtype, duserId);
			final OrderStatus status = OrderStatus.fromInt(statusCode);
			
			Collection<OrderCartData> orderCart = null;
			Collection<OrderHistoryData> orderHystory = null;
			if(!listOnly){
				orderCart = this.thisReadSrv.fetchOrderCart(orderId, this.hide);
				orderHystory = this.thisReadSrv.fetchOrderHistory(orderId, this.hide);
			}
			return OrderData.createNew((this.hide)? null :orderId, userId, user, orderName, pickupAddress,
					dropAddress, additionalNote, statusCode, status.getStatusName(), 
					totalamount, orderCart, orderHystory, (this.listOnly || this.hide)? null : OrderStatus.getAllAsMap());
		}
		
	}
	
	private static final class OrderCartRowMapper implements  RowMapper<OrderCartData>{
		
		final Boolean hide;
		public OrderCartRowMapper(Boolean hideId){
			this.hide = hideId;
		}
		
		public String schema(){
			return new StringBuilder()
			.append("o.id cartId, ")
			.append("o.order_id orderId, ")
			.append("o.quantity, ")
			.append("o.item_total_amount itemTotalAmount, ")

			.append("p.id productId, ")
			.append("p.product_uid productUid, p.quantity_unit quantityUnit, ")
			.append("p.name productName, p.price_per_unit pricePerUnit, ")

			.append("pkg.sub_tag pkngStyle, ")
			.append("pkg.label pkngStyleLabel, ")
			.append("pkg.id pkngStyleId ")

			.append("from g_order_cart o ")
			.append("left join g_sub_tag pkg on pkg.id = o.pkg_style_id ")
			.append("inner join g_product p on p.id = o.product_id ")
			.toString();
		}

		@Override
		public OrderCartData mapRow(ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
			final Long cartId = rs.getLong("cartId");
			final Long orderId  = rs.getLong("orderId");
			final BigDecimal quantity = rs.getBigDecimal("quantity");
			final BigDecimal itemTotalAmount = rs.getBigDecimal("itemTotalAmount");
			final Long productId = rs.getLong("productId");
			final String productUid = rs.getString("productUid");
			final String productName = rs.getString("productName");
			final BigDecimal pricePerUnit = rs.getBigDecimal("pricePerUnit");
			final String pkngStyle = rs.getString("pkngStyle");
			final String pkngStyleLabel = rs.getString("pkngStyleLabel");
			final Long pkngStyleId = rs.getLong("pkngStyleId");
			final String quantityUnit = rs.getString("quantityUnit");
			return OrderCartData.CreateNew((this.hide)? null :cartId, (this.hide)? null :orderId, quantity, 
					quantityUnit, itemTotalAmount,(this.hide)? null :	productId, productUid, 
					productName, pricePerUnit,	pkngStyle, pkngStyleLabel, pkngStyleId);
		}
	}
	private static final class OrderHistoryRowMapper implements  RowMapper<OrderHistoryData>{

		final Boolean hide;
		public OrderHistoryRowMapper(Boolean hideId){
			this.hide = hideId;
		}
		public String schema(){
			return new StringBuilder()
			.append(" h.id historyId, ")
			.append("h.order_id orderId, ")
			.append("h.status statusCode, ")
			.append("h.description, ")
			.append("h.created_user_id userId, ")
			.append("h.created_time createTime, ")
			.append("u.name userName, u.email userEmail ")
			.append("from g_order_history h ")
			.append("inner join g_user u on u.id = h.created_user_id ")
			.toString();
		}
		@Override
		public OrderHistoryData mapRow(ResultSet rs, @SuppressWarnings("unused") final int rowNum)
				throws SQLException {
			final Long historyId = rs.getLong("historyId");
			final Long orderId = rs.getLong("orderId");
			final Integer statusCode = rs.getInt("statusCode");
			final String description = rs.getString("description");
			final Long userId = rs.getLong("userId");
			final String  userName = rs.getString("userName");
			final String userEmail = rs.getString("userEmail");
			final LocalDateTime createTime = new LocalDateTime( rs.getTimestamp("createTime"));
			final OrderStatus status = OrderStatus.fromInt(statusCode);
			return OrderHistoryData.createNew((this.hide)? null :historyId, (this.hide)? null :orderId, 
					statusCode, status.getStatusName(),
					description, (this.hide)? null :userId, (this.hide)? null :userName, userEmail, createTime);
		}
		
	}
	
	
}

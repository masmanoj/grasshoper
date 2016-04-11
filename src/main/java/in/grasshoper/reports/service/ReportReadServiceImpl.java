package in.grasshoper.reports.service;

import in.grasshoper.core.GrassHoperMainConstants;
import in.grasshoper.reports.data.ReportData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReportReadServiceImpl implements ReportReadService{
	private final JdbcTemplate jdbcTemplate;
	@Autowired
	public ReportReadServiceImpl(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		
	}
	
	@Override
	public ReportData getProductSalesReport(Long productId,
			LocalDate fromDate, LocalDate toDate, Integer limit, Integer offset, Integer orderBy,
			Integer orderType, String reportType) {
		List<String> header = new ArrayList<>(Arrays.asList("Product Id"
				, "Product Name"
				, "Quantity"
				, "Unit"
				, "Action Type"
				, "Created User"
				, "Created User Email"
				, "Is Public User"
				, "Order Name"
				, "Time"
				));
		
		StringBuffer sqlBfr = new StringBuffer()
		.append(" select ")
		.append(" p.product_uid productId, ")
		.append(" p.name productName, ")
		.append(" tran.quantity,  ")
		.append(" tran.quantity_unit unit,  ")
		.append(" if(tran.tran_type_enum = 0,'Reset',if(tran.tran_type_enum = 1,'Order','Refill')) ActionType, ")
		.append(" u.name createdUser, ")
		.append(" u.email createduserEmail, ")
		.append(" if(u.is_public_user,'Yes','No') isPublicUser, ")
		.append(" ifnull(o.name, '' ) orderName, ")
		.append(" tran.created_time createdTime ");
		if("sales".equals(reportType)){
			sqlBfr.append(" ,cart.item_total_amount totalAmount");
			header.add("Amount("+GrassHoperMainConstants.CurrencyCode+")");
		}
		
		sqlBfr.append(" from g_product_quantity_tran  tran  ")
		.append(" join g_product p on p.id   = tran.product_id ")
		.append(" join g_user u on u.id = tran.created_user_id ")
		.append(" left join g_order o on o.id = tran.order_id ");
		
		if("sales".equals(reportType)){
			sqlBfr.append(" left join g_order_cart cart on cart.order_id = tran.order_id and cart.product_id = tran.product_id ");
		}
		
		String prefix = " where ";
		List<Object> args = new ArrayList<>();
		if("sales".equals(reportType)){
			sqlBfr.append(prefix);
			sqlBfr.append(" tran.order_id is not null ");
			prefix =" and ";
		}
		
		if(productId != null ){
			sqlBfr.append(prefix);
			sqlBfr.append(" p.id =  ? ");
			args.add(productId);
			prefix =" and ";
		}
		
		if(fromDate != null){
			sqlBfr.append(prefix);
			sqlBfr.append(" tran.created_time  >=   ? ");
			args.add(fromDate.toString("yyyy-MM-dd"));
			prefix =" and ";
		}

		if(toDate != null){
			sqlBfr.append(prefix);
			sqlBfr.append("  tran.created_time <=  ? ");
			args.add(toDate.toString("yyyy-MM-dd"));
			prefix =" and ";
		}else if(fromDate != null){
			sqlBfr.append(prefix);
			sqlBfr.append("  tran.created_time <=  curdate() ");
			prefix =" and ";
		}
		if(orderBy !=null && orderBy < header.size()){
			sqlBfr.append(" order by "+ orderBy);
			if(orderType == 1)
				sqlBfr.append(" desc ");
		}else
			sqlBfr.append(" order by tran.created_time desc ");

		if(limit != null && ! (limit < 0)){
			if(offset !=null){
				sqlBfr.append(" limit "+offset+", "+limit);
			}else
				sqlBfr.append(" limit "+limit);
		}
		
		List<Map<String,Object>> dataList  = this.jdbcTemplate.queryForList(sqlBfr.toString(), args.toArray());
		return ReportData.createNew(header, dataList);
	}

	
}

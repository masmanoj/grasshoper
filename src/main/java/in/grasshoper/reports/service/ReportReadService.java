package in.grasshoper.reports.service;

import in.grasshoper.reports.data.ReportData;

import java.util.Map;

import org.joda.time.LocalDate;

public interface ReportReadService {

	ReportData getProductSalesReport(Long productId, LocalDate fromDate,
			LocalDate toDate, Integer limit, Integer offset, Integer orderBy,
			Integer orderType,  String reportType);

}

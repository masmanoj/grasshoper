package in.grasshoper.reports.api;

import in.grasshoper.core.infra.ApiSerializer;
import in.grasshoper.core.security.service.PlatformSecurityContext;
import in.grasshoper.reports.data.ReportData;
import in.grasshoper.reports.service.ReportReadService;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController  
@RequestMapping("/report")
public class ReportApiResource {
	private final ApiSerializer<ReportData> apiJsonSerializerService;
	private final PlatformSecurityContext context;
	private final ReportReadService reportReadService;
	
	@Autowired
	public ReportApiResource(final ReportReadService reportReadService,
			ApiSerializer<ReportData> apiJsonSerializerService,
			final PlatformSecurityContext context) {
		super();
		this.reportReadService = reportReadService;
		this.apiJsonSerializerService = apiJsonSerializerService;
		this.context = context;
	}
	@RequestMapping(value="/{reportname}", method = RequestMethod.GET)  
	@Transactional(readOnly = true)
    public String retrieveOne(@PathVariable("reportname")final String reportname,
    		@RequestParam(value = "fromDate", required = false) LocalDate fromDate,@RequestParam(value = "toDate", required = false) LocalDate toDate,
    		@RequestParam(value= "productId" , required = false) Long productId,
    		@RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "offset", required = false) Integer offset,
    		@RequestParam(value = "orderBy", required = false) Integer orderBy, @RequestParam(value = "orderType", required = false) Integer orderType
    		) {
		this.context.restrictPublicUser();
		
		ReportData result = null;
		switch(reportname){
			case "product-quantity" : result = this.reportReadService.getProductSalesReport(productId, fromDate, toDate, limit, offset, orderBy,
					 orderType,"product-quantity"); break;
			case "sales" : result = this.reportReadService.getProductSalesReport(productId, fromDate, toDate, limit, offset, orderBy,
					 orderType,"sales"); break;
		}
        
        return this.apiJsonSerializerService.serialize(result);
    }
}

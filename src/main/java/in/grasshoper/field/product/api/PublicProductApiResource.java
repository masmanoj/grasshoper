package in.grasshoper.field.product.api;

import in.grasshoper.core.infra.ApiSerializer;
import in.grasshoper.field.product.data.ProductData;
import in.grasshoper.field.product.service.ProductReadService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController  
@RequestMapping("/stage")
public class PublicProductApiResource {
	private final ProductReadService productReadService;
	private final ApiSerializer<ProductData> apiJsonSerializerService;
	
	
	@Autowired
	private PublicProductApiResource(final ProductReadService productReadService,
			final ApiSerializer<ProductData> apiJsonSerializerService) {
		super();
		this.productReadService = productReadService;
		this.apiJsonSerializerService = apiJsonSerializerService;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
    public String getStage(){
		ProductData result = this.productReadService.generateTemplate();
		return this.apiJsonSerializerService.serialize(result);
	}

	@RequestMapping(value="/search-items",method = RequestMethod.GET)
	@ResponseBody
    public String getAll(@RequestParam(value="qry", required = false) String searchQry, @RequestParam(value="notSoldOut" , required = false) Boolean notSoldOut,
    		@RequestParam(value="l" , required = false) Integer limit, @RequestParam(value="o" , required = false) Integer offset,
    		@RequestParam(value="sort" , required = false) String orderby,@RequestParam(value="types" , required = false) String category,
    		@RequestParam(value="product", required = false) String productUId) {
		Collection<ProductData> result = this.productReadService.retriveAllProductsSearch(searchQry,
				notSoldOut, limit, offset, orderby, category, productUId);
		return this.apiJsonSerializerService.serialize(result);
	}
}

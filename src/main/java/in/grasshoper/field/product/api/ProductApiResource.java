package in.grasshoper.field.product.api;

import in.grasshoper.core.infra.ApiSerializer;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.field.product.data.ProductData;
import in.grasshoper.field.product.service.ProductReadService;
import in.grasshoper.field.product.service.ProductWriteService;
import in.grasshoper.field.tag.data.TagData;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonParser;

@RestController  
@RequestMapping("/product")
public class ProductApiResource {
	
	private final ProductWriteService productWriteService;
	private final ProductReadService productReadService;
	final ApiSerializer<ProductData> apiJsonSerializerService;
	private final FromJsonHelper fromApiJsonHelper;
	
	@Autowired
	private ProductApiResource(final ProductWriteService productWriteService,
			final ApiSerializer<ProductData> apiJsonSerializerService,
			final FromJsonHelper fromApiJsonHelper,
			final ProductReadService productReadService) {
		super();
		this.productWriteService = productWriteService;
		this.apiJsonSerializerService = apiJsonSerializerService;
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.productReadService = productReadService;
	}


	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
    public CommandProcessingResult createProduct(@RequestBody final  String reqBody){
		return this.productWriteService.createProduct(JsonCommand.from(reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
    public String getAll() {
		Collection<ProductData> result = this.productReadService.retriveAll();
		return apiJsonSerializerService.serialize(result);
	}
	@RequestMapping(value="/{productId}", method = RequestMethod.GET)
	@ResponseBody
    public String getProduct(@PathVariable("productId") final Long productId) {
		ProductData result = this.productReadService.retriveOne(productId);
		return apiJsonSerializerService.serialize(result);
	}
	
	@RequestMapping(value="/{productId}", method = RequestMethod.PUT)
	@ResponseBody
    public CommandProcessingResult update(@PathVariable("productId") final Long productId,
    		@RequestBody final  String reqBody) {
		return this.productWriteService.updateProduct(productId, JsonCommand.from(reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
	}
	
}

package in.grasshoper.field.product.api;

import in.grasshoper.core.exception.GeneralPlatformRuleException;
import in.grasshoper.core.infra.ApiSerializer;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.core.infra.srvmanager.domain.GlobalServiceEnum;
import in.grasshoper.core.infra.srvmanager.service.SynchronousServiceExecutionService;
import in.grasshoper.core.security.service.PlatformSecurityContext;
import in.grasshoper.field.product.data.ProductData;
import in.grasshoper.field.product.service.ProductReadService;
import in.grasshoper.field.product.service.ProductWriteService;

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
	
	private final SynchronousServiceExecutionService srvExecuter;
	private final ProductWriteService productWriteService;
	private final ProductReadService productReadService;
	final ApiSerializer<ProductData> apiJsonSerializerService;
	private final FromJsonHelper fromApiJsonHelper;
	private final PlatformSecurityContext context;
	
	@Autowired
	private ProductApiResource(final ProductWriteService productWriteService,
			final ApiSerializer<ProductData> apiJsonSerializerService,
			final FromJsonHelper fromApiJsonHelper,
			final ProductReadService productReadService,
			final SynchronousServiceExecutionService srvExecuter,
			final PlatformSecurityContext context) {
		super();
		this.productWriteService = productWriteService;
		this.apiJsonSerializerService = apiJsonSerializerService;
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.productReadService = productReadService;
		this.srvExecuter = srvExecuter;
		this.context = context;
	}


	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
    public CommandProcessingResult createProduct(@RequestBody final  String reqBody){
		return this.srvExecuter.executeInternalWriteService(GlobalServiceEnum.PRODUCTCREATE, (JsonCommand.from(reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper)));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
    public String getAll() {
		this.context.restrictPublicUser();
		Collection<ProductData> result = this.productReadService.retriveAll();
		return apiJsonSerializerService.serialize(result);
	}
	@RequestMapping(value="/{productId}", method = RequestMethod.GET)
	@ResponseBody
    public String getProduct(@PathVariable("productId") final Long productId) {
		this.context.restrictPublicUser();
		ProductData result = this.productReadService.retriveOne(productId);
		return apiJsonSerializerService.serialize(result);
	}
	
	@RequestMapping(value="/{productId}", method = RequestMethod.PUT)
	@ResponseBody
    public CommandProcessingResult update(@PathVariable("productId") final Long productId,
    		@RequestBody final  String reqBody) {
		this.context.restrictPublicUser();
		return this.productWriteService.updateProduct(productId, JsonCommand.from(reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
	}
	
	@RequestMapping(value="/template", method = RequestMethod.GET)
	@ResponseBody
    public String getTemplate() {
		this.context.restrictPublicUser();
		ProductData result = this.productReadService.generateTemplate();
		return apiJsonSerializerService.serialize(result);
	}
	@RequestMapping(value="/{productId}/image",method = RequestMethod.POST)
	@ResponseBody
    public CommandProcessingResult createProductImage(@PathVariable("productId") final Long productId,
    		@RequestBody final  String reqBody){
		this.context.restrictPublicUser();
		return this.productWriteService.addProductImage(productId, JsonCommand.from(reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
	}
	
	@RequestMapping(value="/{productId}/image/{imageId}", method = RequestMethod.PUT)
	@ResponseBody
    public CommandProcessingResult updateImage(@PathVariable("productId") final Long productId,
    		@PathVariable("imageId") final Long imageId,
    		@RequestBody final  String reqBody) {
		this.context.restrictPublicUser();
		return this.productWriteService.updateProductImage(productId, imageId, JsonCommand.from(reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
	}
	
	@RequestMapping(value="/{productId}/image/{imageId}", method = RequestMethod.DELETE)
	@ResponseBody
    public CommandProcessingResult deleteImage(@PathVariable("productId") final Long productId,
    		@PathVariable("imageId") final Long imageId) {
		this.context.restrictPublicUser();
		return this.productWriteService.deleteProductImage(productId, imageId);
	}
	
	@RequestMapping(value="/{productId}/qty/{command}", method = RequestMethod.PUT)
	@ResponseBody
    public CommandProcessingResult updateQty(@PathVariable("productId") final Long productId,
    		@PathVariable("command") final String command,
    		@RequestBody final  String reqBody) {
		this.context.restrictPublicUser();
		if("reset".equals(command)) return this.productWriteService.resetProductQuantity(productId);
		if("add".equals(command)) 
			return this.productWriteService.updateProductQuantity(productId, JsonCommand.from(reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
		throw new GeneralPlatformRuleException("error.msg.unknown.command", "Vnknown command : "+ command, command);
	}
}

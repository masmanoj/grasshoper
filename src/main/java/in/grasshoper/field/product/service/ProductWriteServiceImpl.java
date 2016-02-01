package in.grasshoper.field.product.service;

import static in.grasshoper.field.product.productConstants.CategoryIdsParamName;
import static in.grasshoper.field.product.productConstants.PackingStyleIdsParamName;
import static in.grasshoper.field.product.productConstants.QuantityParamName;
import in.grasshoper.core.exception.PlatformDataIntegrityException;
import in.grasshoper.core.exception.ResourceNotFoundException;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.CommandProcessingResultBuilder;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.field.order.domain.Order;
import in.grasshoper.field.product.domain.Product;
import in.grasshoper.field.product.domain.ProductImage;
import in.grasshoper.field.product.domain.ProductRepository;
import in.grasshoper.field.tag.domain.SubTag;
import in.grasshoper.field.tag.domain.SubTagRepository;
import in.grasshoper.field.transaction.domain.TranTypeEnum;
import in.grasshoper.field.transaction.service.ProductQuantityTransactionWriteService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

@Service
public class ProductWriteServiceImpl implements ProductWriteService {

	private final ProductRepository productRepository;
	private final FromJsonHelper fromJsonHelper;
	private final SubTagRepository subTagRepository;
	private final ProductQuantityTransactionWriteService quantityTranService;
	@Autowired
	public ProductWriteServiceImpl(final ProductRepository productRepository,
			final FromJsonHelper fromJsonHelper, final SubTagRepository subTagRepository,
			final ProductQuantityTransactionWriteService quantityTranService) {
		super();
		this.productRepository = productRepository;
		this.fromJsonHelper = fromJsonHelper;
		this.subTagRepository = subTagRepository;
		this.quantityTranService = quantityTranService;
	}
	
	@Override
	@Transactional
	public CommandProcessingResult createProduct(final JsonCommand command) {
		try {
			final Set<SubTag> packingStyles = new HashSet<>();
			final Set<SubTag> categories = new HashSet<>();
			//get packing styles from command.
			
			// this.dataValidator.validateForCreate(command.getJsonCommand());
	
			getPackageStylesFromCommand(command, packingStyles);
			getCategoriesFromCommand(command, categories);
			
			final Product product = Product.fromJson(command, packingStyles, categories);
			this.productRepository.save(product);
			
			creditProductQuantity(product, product.getQuantity());

			return new CommandProcessingResultBuilder().withResourceIdAsString(
					product.getId()).build();

		} catch (DataIntegrityViolationException ex) {
			ex.printStackTrace();
			final Throwable realCause = ex.getMostSpecificCause();
			throw new PlatformDataIntegrityException(
					"error.msg.unknown.data.integrity.issue",
					"Unknown data integrity issue with resource: "
							+ realCause.getMessage());
		}
	}
	
	@Override
	@Transactional
	public CommandProcessingResult updateProduct(final Long productId,
			final JsonCommand command) {
		try {
			// this.dataValidator.validateForUpdate(command.getJsonCommand());
			
			
			
			final Product product = this.productRepository.findOne(productId);
			if (product == null) {
				throw new ResourceNotFoundException(
						"error.entity.product.not.found", "Product with id " + productId
								+ "not found", productId);
			}
			final Map<String, Object> changes = product.update(command);
			
			final Set<SubTag> packingStyles = new HashSet<>();
			boolean packagingStyleChanges = false;
			getPackageStylesFromCommand(command, packingStyles);
			if(!product.getPackingStyles().containsAll(packingStyles)
					|| !packingStyles.containsAll(product.getPackingStyles())){
				product.updatePackingStyles(packingStyles);
				packagingStyleChanges = true;
			}
			final Set<SubTag> categories = new HashSet<>();
			boolean categoriesChanges = false;
			getCategoriesFromCommand(command, categories);
			if(!product.getCategories().containsAll(categories)
					|| !categories.containsAll(product.getCategories())){
				product.updateCategories(categories);
				categoriesChanges = true;
			}
			
			if (!changes.isEmpty() || packagingStyleChanges || categoriesChanges) {
				this.productRepository.save(product);
			}

			return new CommandProcessingResultBuilder() //
					.withResourceIdAsString(productId) //
					.withChanges(changes) //
					.build();
		} catch (DataIntegrityViolationException ex) {
			ex.printStackTrace();
			final Throwable realCause = ex.getMostSpecificCause();
			throw new PlatformDataIntegrityException(
					"error.msg.unknown.data.integrity.issue",
					"Unknown data integrity issue with resource: "
							+ realCause.getMessage());
		}
	}
	
	private void getPackageStylesFromCommand(final JsonCommand command, final Set<SubTag> packingStyles ){
		final String json = command.getJsonCommand();
    	final JsonElement element = this.fromJsonHelper.parse(json);
		final JsonArray packageStyleIdsJsonArray =  this.fromJsonHelper.extractJsonArrayNamed(PackingStyleIdsParamName, element);
		if( null!= packageStyleIdsJsonArray){
			for (int i = 0; i < packageStyleIdsJsonArray.size(); i++) {
				final Long  subTagId = packageStyleIdsJsonArray.get(i)
						.getAsLong();
				final SubTag subTag = this.subTagRepository.findOne(subTagId);
				if (subTag == null) {
					throw new ResourceNotFoundException(
							"error.entity.subtag.not.found", "Sub tag with id " + subTag
									+ "not found", subTag);
				}
				
				packingStyles.add(subTag);
			}
		}
	}
	private void getCategoriesFromCommand(final JsonCommand command, final Set<SubTag> categories ){
		final String json = command.getJsonCommand();
    	final JsonElement element = this.fromJsonHelper.parse(json);
		final JsonArray categoryIdsJsonArray =  this.fromJsonHelper.extractJsonArrayNamed(CategoryIdsParamName, element);
		if(null!= categoryIdsJsonArray){
			for (int i = 0; i < categoryIdsJsonArray.size(); i++) {
				final Long  subTagId = categoryIdsJsonArray.get(i)
						.getAsLong();
				final SubTag subTag = this.subTagRepository.findOne(subTagId);
				if (subTag == null) {
					throw new ResourceNotFoundException(
							"error.entity.subtag.not.found", "Sub tag with id " + subTag
									+ "not found", subTag);
				}
				
				categories.add(subTag);
			}
		}
	}
	
	
	@Override
	@Transactional
	public CommandProcessingResult addProductImage(final Long productId,
			final JsonCommand command) {
		try {
			// this.dataValidator.validateForUpdate(command.getJsonCommand());
			
			
			
			final Product product = this.productRepository.findOne(productId);
			if (product == null) {
				throw new ResourceNotFoundException(
						"error.entity.product.not.found", "Product with id " + productId
								+ "not found", productId);
			}
			final ProductImage productImage =  ProductImage.fromJson(command, product);
			
			
			
			if (productImage != null ) {
				product.addProductImage(productImage);
				this.productRepository.saveAndFlush(product);
				final ProductImage newProductImage = product.lastAddedProductImage();
				return new CommandProcessingResultBuilder() //
				.withResourceIdAsString(newProductImage.getId()) //
				.build();
			}
			return new CommandProcessingResultBuilder() //
			.withResourceIdAsString(null) //
			.build();
		} catch (DataIntegrityViolationException ex) {
			ex.printStackTrace();
			final Throwable realCause = ex.getMostSpecificCause();
			throw new PlatformDataIntegrityException(
					"error.msg.unknown.data.integrity.issue",
					"Unknown data integrity issue with resource: "
							+ realCause.getMessage());
		}
	}
	
	@Override
	@Transactional
	public CommandProcessingResult updateProductImage(final Long productId, final Long productImageId,
			final JsonCommand command) {
		try {
			// this.dataValidator.validateForUpdate(command.getJsonCommand());
			
			
			
			final Product product = this.productRepository.findOne(productId);
			if (product == null) {
				throw new ResourceNotFoundException(
						"error.entity.tag.product.found", "Product with id " + productId
								+ "not found", productId);
			}
			
			ProductImage img = product.getProductImageById(productImageId);
			if(img!=null){
				final Map<String, Object> changes = img.update(command);
				
				if (!changes.isEmpty()) {
					product.addProductImage(img);
					this.productRepository.save(product);
				}
				return new CommandProcessingResultBuilder() //
				.withResourceIdAsString(img.getId()) //
				.build();
			}
			return new CommandProcessingResultBuilder() //
			.withResourceIdAsString(null) //
			.build();
		} catch (DataIntegrityViolationException ex) {
			ex.printStackTrace();
			final Throwable realCause = ex.getMostSpecificCause();
			throw new PlatformDataIntegrityException(
					"error.msg.unknown.data.integrity.issue",
					"Unknown data integrity issue with resource: "
							+ realCause.getMessage());
		}
		
	}
	
	@Override
	@Transactional
	public CommandProcessingResult deleteProductImage(final Long productId, final Long productImageId) {
		try {
			// this.dataValidator.validateForUpdate(command.getJsonCommand());
			
			
			
			final Product product = this.productRepository.findOne(productId);
			if (product == null) {
				throw new ResourceNotFoundException(
						"error.entity.product.not.found", "Product with id " + productId
								+ "not found", productId);
			}
			
			ProductImage img = product.getProductImageById(productImageId);
			if(img!=null){
					product.removeProductImages(img);
					this.productRepository.save(product);
				return new CommandProcessingResultBuilder() //
				.withResourceIdAsString(img.getId()) //
				.build();
			}
			return new CommandProcessingResultBuilder() //
			.withResourceIdAsString(null) //
			.build();
		} catch (DataIntegrityViolationException ex) {
			ex.printStackTrace();
			final Throwable realCause = ex.getMostSpecificCause();
			throw new PlatformDataIntegrityException(
					"error.msg.unknown.data.integrity.issue",
					"Unknown data integrity issue with resource: "
							+ realCause.getMessage());
		}
		
	}
	@Override
	@Transactional
	public void debitProductQuantity(final Product product, final Order order, BigDecimal quantity ){
		product.updateQuantity(product.getQuantity().subtract(quantity));
		Long tranId = this.quantityTranService.createProductQuantityTransation(product, quantity, product.getQuantityUnit(),
				TranTypeEnum.DEBIT.getTranCode(), order, false);
		
		if(tranId != null){
			this.productRepository.save(product);
		}
		
	}
	@Override
	@Transactional
	public void reverseDebitProductQuantity(final Product product, final Order order, BigDecimal quantity ){
		product.updateQuantity(product.getQuantity().subtract(quantity));
		Long tranId = this.quantityTranService.createProductQuantityTransation(product, quantity, product.getQuantityUnit(),
				TranTypeEnum.DEBIT.getTranCode(), order, true);
		
		if(tranId != null){
			this.productRepository.save(product);
		}
	}
	
	@Override
	@Transactional
	public CommandProcessingResult updateProductQuantity(final Long productId,
			final JsonCommand command) {
		try {
			// this.dataValidator.validateForUpdate(command.getJsonCommand());
			
			
			
			final Product product = this.productRepository.findOne(productId);
			if (product == null) {
				throw new ResourceNotFoundException(
						"error.entity.product.not.found", "Product with id " + productId
								+ " not found", productId);
			}
			final Map<String, Object> changes = product.updateQuantityFromCommand(command);
			
			
			if (!changes.isEmpty()) {
				creditProductQuantity(product, (BigDecimal)changes.get(QuantityParamName));
			}

			return new CommandProcessingResultBuilder() //
					.withResourceIdAsString(productId) //
					.withChanges(changes) //
					.build();
		} catch (DataIntegrityViolationException ex) {
			ex.printStackTrace();
			final Throwable realCause = ex.getMostSpecificCause();
			throw new PlatformDataIntegrityException(
					"error.msg.unknown.data.integrity.issue",
					"Unknown data integrity issue with resource: "
							+ realCause.getMessage());
		}
	}
	private void creditProductQuantity(final Product product, BigDecimal quantity){
		product.updateQuantity(quantity);
		Long tranId = this.quantityTranService.createProductQuantityTransation(product, quantity, product.getQuantityUnit(),
				TranTypeEnum.CREDIT.getTranCode(), null, false);
		
		if(tranId != null){
			this.productRepository.save(product);
		}
	}
	@Override
	@Transactional
	public CommandProcessingResult resetProductQuantity(final Long productId){
		
		final Product product = this.productRepository.findOne(productId);
		if (product == null) {
			throw new ResourceNotFoundException(
					"error.entity.product.not.found", "Product with id " + productId
							+ " not found", productId);
		}
		
		Long tranId = this.quantityTranService.createProductQuantityTransation(product, product.getQuantity(), product.getQuantityUnit(),
				TranTypeEnum.RESET
				.getTranCode(), null, false);
		
		if(tranId != null){
			product.updateQuantity(BigDecimal.ZERO);
			this.productRepository.save(product);
			return new CommandProcessingResultBuilder() //
			.withResourceIdAsString(productId) //
			.withSuccessStatus()//
			.build();
		}
		return new CommandProcessingResultBuilder() //
		.withResourceIdAsString(productId) //
		.withFailureStatus()//
		.build();
	}
}

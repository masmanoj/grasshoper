package in.grasshoper.field.product.service;

import in.grasshoper.core.exception.PlatformDataIntegrityException;
import in.grasshoper.core.exception.ResourceNotFoundException;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.CommandProcessingResultBuilder;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.field.product.domain.Product;
import in.grasshoper.field.product.domain.ProductImage;
import in.grasshoper.field.product.domain.ProductRepository;
import in.grasshoper.field.tag.domain.SubTag;
import in.grasshoper.field.tag.domain.SubTagRepository;
import static in.grasshoper.field.product.productConstants.PackingStyleIdsParamName;

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
	@Autowired
	public ProductWriteServiceImpl(final ProductRepository productRepository,
			final FromJsonHelper fromJsonHelper, final SubTagRepository subTagRepository) {
		super();
		this.productRepository = productRepository;
		this.fromJsonHelper = fromJsonHelper;
		this.subTagRepository = subTagRepository;
	}
	
	@Override
	@Transactional
	public CommandProcessingResult createProduct(final JsonCommand command) {
		try {
			final Set<SubTag> packingStyles = new HashSet<>();
			//get packing styles from command.
			
			// this.dataValidator.validateForCreate(command.getJsonCommand());
	
			getPackageStylesFromCommand(command, packingStyles);
			
			final Product product = Product.fromJson(command, packingStyles);
			this.productRepository.save(product);

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
						"error.entity.tag.not.found", "Product with id " + productId
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
			
			if (!changes.isEmpty() || packagingStyleChanges) {
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
	
	
	@Override
	@Transactional
	public CommandProcessingResult addProductImage(final Long productId,
			final JsonCommand command) {
		try {
			// this.dataValidator.validateForUpdate(command.getJsonCommand());
			
			
			
			final Product product = this.productRepository.findOne(productId);
			if (product == null) {
				throw new ResourceNotFoundException(
						"error.entity.tag.not.found", "Product with id " + productId
								+ "not found", productId);
			}
			final ProductImage productImage =  ProductImage.fromJson(command, product);
			
			
			
			if (productImage != null ) {
				product.addProductImages(productImage);
				this.productRepository.save(product);
				return new CommandProcessingResultBuilder() //
				.withResourceIdAsString(productImage.getId()) //
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
						"error.entity.tag.not.found", "Product with id " + productId
								+ "not found", productId);
			}
			
			ProductImage img = product.getProductImageById(productImageId);
			if(img!=null){
				final Map<String, Object> changes = img.update(command);
				
				if (!changes.isEmpty()) {
					product.addProductImages(img);
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
						"error.entity.tag.not.found", "Product with id " + productId
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
}

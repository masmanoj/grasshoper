package in.grasshoper.field.product.service;

import in.grasshoper.core.exception.PlatformDataIntegrityException;
import in.grasshoper.core.exception.ResourceNotFoundException;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.CommandProcessingResultBuilder;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.field.product.domain.Product;
import in.grasshoper.field.product.domain.ProductRepository;
import in.grasshoper.field.tag.domain.SubTag;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductWriteServiceImpl implements ProductWriteService {

	private final ProductRepository productRepository;
	@Autowired
	public ProductWriteServiceImpl(final ProductRepository productRepository) {
		super();
		this.productRepository = productRepository;
	}
	
	@Override
	@Transactional
	public CommandProcessingResult createProduct(final JsonCommand command) {
		try {
			final Set<SubTag> packingStyles = new HashSet<>();
			//get packing styles from command.
			
			// this.dataValidator.validateForCreate(command.getJsonCommand());
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

			if (!changes.isEmpty()) {
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
}

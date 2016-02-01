package in.grasshoper.field.transaction.service;

import in.grasshoper.core.exception.PlatformDataIntegrityException;
import in.grasshoper.field.order.domain.Order;
import in.grasshoper.field.product.domain.Product;
import in.grasshoper.field.transaction.domain.ProductQuantityTransaction;
import in.grasshoper.field.transaction.domain.ProductQuantityTransactionRepository;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductQuantityTransactionWriteServiceImpl implements ProductQuantityTransactionWriteService{
	private final ProductQuantityTransactionRepository productQuantityTransactionRepository;
	@Autowired
	public ProductQuantityTransactionWriteServiceImpl(
			final ProductQuantityTransactionRepository productQuantityTransactionRepository){
		this.productQuantityTransactionRepository = productQuantityTransactionRepository;
	}
	
	
	@Override
	@Transactional
	public Long createProductQuantityTransation(Product product, BigDecimal quantity,
			String quantityUnit, Integer tranTypeCode, Order order,
			Boolean isReverse) {
		try {
			//All quantity related integrity validations can be moved to here
			
			
			ProductQuantityTransaction tran = ProductQuantityTransaction.create(product, quantity,
					quantityUnit, tranTypeCode, order, isReverse);
			this.productQuantityTransactionRepository.save(tran);

			return tran.getId();
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

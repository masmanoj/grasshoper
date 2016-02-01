package in.grasshoper.field.transaction.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ProductQuantityTransactionRepository extends JpaRepository<ProductQuantityTransaction, Long>, JpaSpecificationExecutor<ProductQuantityTransaction>{

}

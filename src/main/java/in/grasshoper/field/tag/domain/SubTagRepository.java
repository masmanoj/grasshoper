package in.grasshoper.field.tag.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubTagRepository  extends JpaRepository<SubTag, Long>, JpaSpecificationExecutor<SubTag> {

	//SubTag findByTagTagAndId(String tagTag, Long id);

}

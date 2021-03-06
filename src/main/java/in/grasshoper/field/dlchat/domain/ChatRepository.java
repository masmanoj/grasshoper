package in.grasshoper.field.dlchat.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChatRepository extends JpaRepository <Chat, Long>
, JpaSpecificationExecutor<Chat>{

}
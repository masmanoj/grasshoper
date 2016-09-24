package in.grasshoper.field.dlchat.service;

import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.field.dlchat.domain.ChatRepository;
import in.grasshoper.field.dlchat.domain.DlOrderRepository;
import in.grasshoper.field.dlchat.domain.OrderChatRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;

@Service
public class DlOrderWritePlatformServiceImpl implements DlOrderWritePlatformService {

	private final ChatRepository chatRepository;
	private final DlOrderRepository dlOrderRepository;
	private final OrderChatRepository orderChatRepository;
	private final FromJsonHelper fromJsonHelper;
	
	@Autowired
	public DlOrderWritePlatformServiceImpl(ChatRepository chatRepository,
			DlOrderRepository dlOrderRepository,
			OrderChatRepository orderChatRepository,
			final FromJsonHelper fromJsonHelper) {
		super();
		this.chatRepository = chatRepository;
		this.dlOrderRepository = dlOrderRepository;
		this.orderChatRepository = orderChatRepository;
		this.fromJsonHelper = fromJsonHelper;
	}
	
	public void createOrder(final JsonCommand command){
		final String json = command.getJsonCommand();
    	final JsonObject jsonObj = this.fromJsonHelper.parse(json).getAsJsonObject();
    	
    	
    	
	}
	
}

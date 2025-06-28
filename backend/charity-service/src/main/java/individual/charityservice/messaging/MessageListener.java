package individual.charityservice.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import individual.charityservice.business.charity.UpdateCharitiesAfterUserDeletionUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    private final UpdateCharitiesAfterUserDeletionUseCase updateCharitiesAfterUserDeletionUseCase;
    private final MessageSender messageSender;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "${rabbitmq.queue.userDeleted}")
    public void handleUserDeleted(String message) {
        try {
            logger.info("[CharityService] Received user.deleted message: {}", message);
            JsonNode node = objectMapper.readTree(message);
            Long userId = node.get("userId").asLong();
            updateCharitiesAfterUserDeletionUseCase.updateCharitiesAfterUserDeletion(userId);
            String response = "CharityService processed user deletion for userId: " + userId;
            messageSender.sendAsyncResponseToUser(response);
        } catch (Exception e) {
            logger.error("[CharityService] Failed to process user.deleted message", e);
            messageSender.sendAsyncResponseToUser("CharityService failed to handle user deletion");
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.responses}")
    public void handleAsyncResponse(String message) {
        logger.info("[CharityService] Received async response: {}", message);
    }
}
package individual.userservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey.userDeleted}")
    private String routingKeyUserDeleted;

    @Value("${rabbitmq.routingkey.responses.charity}")
    private String routingKeyResponsesCharity;

    public MessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendUserDeleted(Long userId) {
        String messageJson = String.format("{\"userId\": %d}", userId);
        logger.info("[UserService] Sending user.deleted message: {}", messageJson);
        rabbitTemplate.convertAndSend(exchange, routingKeyUserDeleted, messageJson);
    }

    public void sendAsyncResponseToCharity(String message) {
        logger.info("[UserService] Sending async response to CharityService: {}", message);
        rabbitTemplate.convertAndSend(exchange, routingKeyResponsesCharity, message);
    }
}
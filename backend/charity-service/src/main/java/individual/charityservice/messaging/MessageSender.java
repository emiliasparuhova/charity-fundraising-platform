package individual.charityservice.messaging;

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

    @Value("${rabbitmq.routingkey.charityCreated}")
    private String routingKeyCharityCreated;

    @Value("${rabbitmq.routingkey.responses.user}")
    private String routingKeyResponsesUser;

    public MessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAsyncResponseToUser(String message) {
        logger.info("[CharityService] Sending async response to UserService: {}", message);
        rabbitTemplate.convertAndSend(exchange, routingKeyResponsesUser, message);
    }

    public void sendCharityCreatedMessage(String message) {
        logger.info("[CharityService] Sending charity.created message: {}", message);
        rabbitTemplate.convertAndSend(exchange, routingKeyCharityCreated, message);
    }
}
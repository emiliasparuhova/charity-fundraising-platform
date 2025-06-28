package individual.donationservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private final AmqpTemplate amqpTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey.responses.user}")
    private String routingKeyResponsesUser;

    @Value("${rabbitmq.routingkey.responses.charity}")
    private String routingKeyResponsesCharity;

    public MessageSender(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendResponseToUser(String message) {
        logger.info("[DonationService] Sending async response to UserService: {}", message);
        amqpTemplate.convertAndSend(exchange, routingKeyResponsesUser, message);
    }

    public void sendResponseToCharity(String message) {
        logger.info("[DonationService] Sending async response to CharityService: {}", message);
        amqpTemplate.convertAndSend(exchange, routingKeyResponsesCharity, message);
    }
}

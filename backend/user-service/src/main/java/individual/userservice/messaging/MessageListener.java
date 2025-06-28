package individual.userservice.messaging;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);
    private final MessageSender messageSender;

    @RabbitListener(queues = "${rabbitmq.queue.responses}")
    public void handleAsyncResponse(String message) {
        logger.info("[UserService] Received async response: {}", message);
    }

    @RabbitListener(queues = "${rabbitmq.queue.charityCreated}")
    public void handleCharityCreated(String message) {
        logger.info("[UserService] Received charity.created message: {}", message);
        messageSender.sendAsyncResponseToCharity("UserService processed charityCreated event");
    }
}
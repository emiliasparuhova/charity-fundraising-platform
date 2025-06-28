package individual.donationservice.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @RabbitListener(queues = "${rabbitmq.queue.main}")
    public void handleIncoming(String message) {
        logger.info("[DonationService] Received main message: {}", message);
    }

    @RabbitListener(queues = "${rabbitmq.queue.responses}")
    public void handleAsyncResponse(String message) {
        logger.info("[DonationService] Received async response: {}", message);
    }
}

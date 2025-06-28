package individual.charityservice.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.queue.userDeleted}")
    private String userDeletedQueue;

    @Value("${rabbitmq.queue.responses}")
    private String responsesQueue;

    @Value("${rabbitmq.routingkey.userDeleted}")
    private String routingKeyUserDeleted;

    @Value("${rabbitmq.routingkey.responses.charity}")
    private String routingKeyResponsesCharity;

    @Bean
    public Queue userDeletedQueue() {
        return new Queue(userDeletedQueue, true);
    }

    @Bean
    public Queue responsesQueue() {
        return new Queue(responsesQueue, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding bindingUserDeleted(Queue userDeletedQueue, DirectExchange exchange) {
        return BindingBuilder.bind(userDeletedQueue).to(exchange).with(routingKeyUserDeleted);
    }

    @Bean
    public Binding bindingResponses(Queue responsesQueue, DirectExchange exchange) {
        return BindingBuilder.bind(responsesQueue).to(exchange).with(routingKeyResponsesCharity);
    }
}
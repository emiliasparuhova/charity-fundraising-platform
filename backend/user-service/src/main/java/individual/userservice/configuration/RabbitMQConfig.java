package individual.userservice.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.queue.charityCreated}")
    private String charityCreatedQueue;

    @Value("${rabbitmq.queue.responses}")
    private String responsesQueue;

    @Value("${rabbitmq.routingkey.charityCreated}")
    private String routingKeyCharityCreated;

    @Value("${rabbitmq.routingkey.responses.user}")
    private String routingKeyResponsesUser;

    @Bean
    public Queue charityCreatedQueue() {
        return new Queue(charityCreatedQueue, true);
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
    public Binding bindingCharityCreated(Queue charityCreatedQueue, DirectExchange exchange) {
        return BindingBuilder.bind(charityCreatedQueue).to(exchange).with(routingKeyCharityCreated);
    }

    @Bean
    public Binding bindingResponses(Queue responsesQueue, DirectExchange exchange) {
        return BindingBuilder.bind(responsesQueue).to(exchange).with(routingKeyResponsesUser);
    }
}
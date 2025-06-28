package individual.donationservice.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.queue.main}")
    private String mainQueue;

    @Value("${rabbitmq.queue.responses}")
    private String responsesQueue;

    @Value("${rabbitmq.routingkey.main}")
    private String routingKeyMain;

    @Value("${rabbitmq.routingkey.responses.donation}")
    private String routingKeyResponsesDonation;

    @Bean
    public Queue mainQueue() {
        return new Queue(mainQueue, true);
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
    public Binding bindingMain(Queue mainQueue, DirectExchange exchange) {
        return BindingBuilder.bind(mainQueue).to(exchange).with(routingKeyMain);
    }

    @Bean
    public Binding bindingResponses(Queue responsesQueue, DirectExchange exchange) {
        return BindingBuilder.bind(responsesQueue).to(exchange).with(routingKeyResponsesDonation);
    }
}

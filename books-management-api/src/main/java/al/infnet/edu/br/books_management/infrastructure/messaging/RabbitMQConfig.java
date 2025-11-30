package al.infnet.edu.br.books_management.infrastructure.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.books}")
    private String booksExchange;

    @Value("${rabbitmq.queue.book-created}")
    private String bookCreatedQueue;

    @Value("${rabbitmq.queue.book-updated}")
    private String bookUpdatedQueue;

    @Value("${rabbitmq.queue.book-deleted}")
    private String bookDeletedQueue;

    @Value("${rabbitmq.routing.book-created}")
    private String bookCreatedRoutingKey;

    @Value("${rabbitmq.routing.book-updated}")
    private String bookUpdatedRoutingKey;

    @Value("${rabbitmq.routing.book-deleted}")
    private String bookDeletedRoutingKey;

    @Bean
    public TopicExchange booksExchange() {
        return new TopicExchange(booksExchange);
    }

    @Bean
    public Queue bookCreatedQueue() {
        return new Queue(bookCreatedQueue, true);
    }

    @Bean
    public Queue bookUpdatedQueue() {
        return new Queue(bookUpdatedQueue, true);
    }

    @Bean
    public Queue bookDeletedQueue() {
        return new Queue(bookDeletedQueue, true);
    }

    @Bean
    public Binding bookCreatedBinding() {
        return BindingBuilder
            .bind(bookCreatedQueue())
            .to(booksExchange())
            .with(bookCreatedRoutingKey);
    }

    @Bean
    public Binding bookUpdatedBinding() {
        return BindingBuilder
            .bind(bookUpdatedQueue())
            .to(booksExchange())
            .with(bookUpdatedRoutingKey);
    }

    @Bean
    public Binding bookDeletedBinding() {
        return BindingBuilder
            .bind(bookDeletedQueue())
            .to(booksExchange())
            .with(bookDeletedRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}

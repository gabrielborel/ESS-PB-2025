package al.infnet.edu.br.books_management.infrastructure.messaging;

import al.infnet.edu.br.books_management.domain.event.BookCreatedEvent;
import al.infnet.edu.br.books_management.domain.event.BookUpdatedEvent;
import al.infnet.edu.br.books_management.domain.event.BookDeletedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BookEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.books}")
    private String exchange;

    @Value("${rabbitmq.routing.book-created}")
    private String bookCreatedRoutingKey;

    @Value("${rabbitmq.routing.book-updated}")
    private String bookUpdatedRoutingKey;

    @Value("${rabbitmq.routing.book-deleted}")
    private String bookDeletedRoutingKey;

    public BookEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishBookCreated(BookCreatedEvent event) {
        System.out.println("Publishing BookCreatedEvent: " + event.getBookId());
        rabbitTemplate.convertAndSend(exchange, bookCreatedRoutingKey, event);
    }

    public void publishBookUpdated(BookUpdatedEvent event) {
        System.out.println("Publishing BookUpdatedEvent: " + event.getBookId());
        rabbitTemplate.convertAndSend(exchange, bookUpdatedRoutingKey, event);
    }

    public void publishBookDeleted(BookDeletedEvent event) {
        System.out.println("Publishing BookDeletedEvent: " + event.getBookId());
        rabbitTemplate.convertAndSend(exchange, bookDeletedRoutingKey, event);
    }
}

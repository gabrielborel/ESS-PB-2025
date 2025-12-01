package al.infnet.edu.br.books_management.infrastructure.messaging;

import al.infnet.edu.br.books_management.domain.event.BookCreatedEvent;
import al.infnet.edu.br.books_management.domain.event.BookUpdatedEvent;
import al.infnet.edu.br.books_management.domain.event.BookDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BookEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(BookEventPublisher.class);

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
        log.info("Publishing BookCreatedEvent: bookId={}, title={}", event.getBookId(), event.getTitle());
        rabbitTemplate.convertAndSend(exchange, bookCreatedRoutingKey, event);
    }

    public void publishBookUpdated(BookUpdatedEvent event) {
        log.info("Publishing BookUpdatedEvent: bookId={}, title={}", event.getBookId(), event.getTitle());
        rabbitTemplate.convertAndSend(exchange, bookUpdatedRoutingKey, event);
    }

    public void publishBookDeleted(BookDeletedEvent event) {
        log.info("Publishing BookDeletedEvent: bookId={}", event.getBookId());
        rabbitTemplate.convertAndSend(exchange, bookDeletedRoutingKey, event);
    }
}

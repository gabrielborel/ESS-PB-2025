package al.infnet.edu.br.reviews.infrastructure.messaging;

import al.infnet.edu.br.reviews.domain.event.BookCreatedEvent;
import al.infnet.edu.br.reviews.domain.event.BookUpdatedEvent;
import al.infnet.edu.br.reviews.domain.event.BookDeletedEvent;
import al.infnet.edu.br.reviews.application.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BookEventListener {

    private static final Logger log = LoggerFactory.getLogger(BookEventListener.class);

    private final ReviewService reviewService;

    public BookEventListener(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.book-created}")
    public void handleBookCreated(BookCreatedEvent event) {
        log.info("Evento recebido: Livro criado - ID: {}, Título: {}", event.getBookId(), event.getTitle());
    }

    @RabbitListener(queues = "${rabbitmq.queue.book-updated}")
    public void handleBookUpdated(BookUpdatedEvent event) {
        log.info("Evento recebido: Livro atualizado - ID: {}, Novo título: {}, Autor: {}", 
                 event.getBookId(), event.getTitle(), event.getAuthor());
    }

    @RabbitListener(queues = "${rabbitmq.queue.book-deleted}")
    public void handleBookDeleted(BookDeletedEvent event) {
        log.info("Evento recebido: Livro deletado - ID: {}", event.getBookId());
        
        try {
            reviewService.deleteAllByBookId(event.getBookId());
            log.info("Reviews do livro {} foram deletadas com sucesso", event.getBookId());
        } catch (Exception e) {
            log.error("Erro ao deletar reviews do livro {}: {}", event.getBookId(), e.getMessage());
        }
    }
}

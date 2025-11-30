package al.infnet.edu.br.reviews.infrastructure.messaging;

import al.infnet.edu.br.reviews.domain.event.BookCreatedEvent;
import al.infnet.edu.br.reviews.domain.event.BookUpdatedEvent;
import al.infnet.edu.br.reviews.domain.event.BookDeletedEvent;
import al.infnet.edu.br.reviews.application.service.ReviewService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BookEventListener {

    private final ReviewService reviewService;

    public BookEventListener(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.book-created}")
    public void handleBookCreated(BookCreatedEvent event) {
        System.out.println("==> Evento recebido: Livro criado - ID: " + event.getBookId() + 
                         ", Título: " + event.getTitle());
    }

    @RabbitListener(queues = "${rabbitmq.queue.book-updated}")
    public void handleBookUpdated(BookUpdatedEvent event) {
        System.out.println("==> Evento recebido: Livro atualizado - ID: " + event.getBookId() + 
                         ", Novo título: " + event.getTitle() + 
                         ", Autor: " + event.getAuthor());
    }

    @RabbitListener(queues = "${rabbitmq.queue.book-deleted}")
    public void handleBookDeleted(BookDeletedEvent event) {
        System.out.println("==> Evento recebido: Livro deletado - ID: " + event.getBookId());
        
        try {
            reviewService.deleteAllByBookId(event.getBookId());
            System.out.println("==> Reviews do livro " + event.getBookId() + " foram deletadas com sucesso");
        } catch (Exception e) {
            System.err.println("==> Erro ao deletar reviews do livro " + event.getBookId() + ": " + e.getMessage());
        }
    }
}

package al.infnet.edu.br.books_management.infrastructure.listener;

import al.infnet.edu.br.books_management.domain.model.Book;
import al.infnet.edu.br.books_management.domain.model.BookHistory;
import al.infnet.edu.br.books_management.domain.repository.BookHistoryRepository;
import jakarta.persistence.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BookAuditListener {

    private static ApplicationContext applicationContext;

    public BookAuditListener(ApplicationContext applicationContext) {
        BookAuditListener.applicationContext = applicationContext;
    }

    @PostPersist
    public void onPostPersist(Book book) {
        createHistoryRecord(book, BookHistory.OperationType.CREATE);
    }

    @PostUpdate
    public void onPostUpdate(Book book) {
        createHistoryRecord(book, BookHistory.OperationType.UPDATE);
    }

    @PreRemove
    public void onPreRemove(Book book) {
        createHistoryRecord(book, BookHistory.OperationType.DELETE);
    }

    private void createHistoryRecord(Book book, BookHistory.OperationType operationType) {
        if (applicationContext != null && book.getId() != null) {
            try {
                BookHistoryRepository repository = applicationContext.getBean(BookHistoryRepository.class);
                
                BookHistory history = new BookHistory(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublicationYear(),
                        operationType,
                        "system"
                );

                String description = buildChangeDescription(operationType, book);
                history.setChangeDescription(description);

                repository.save(history);
            } catch (Exception e) {
            }
        }
    }

    private String buildChangeDescription(BookHistory.OperationType operationType, Book book) {
        return switch (operationType) {
            case CREATE -> String.format("Livro '%s' criado", book.getTitle());
            case UPDATE -> String.format("Livro '%s' atualizado", book.getTitle());
            case DELETE -> String.format("Livro '%s' removido", book.getTitle());
        };
    }
}

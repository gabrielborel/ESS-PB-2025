package al.infnet.edu.br.books_management.application.mapper;

import al.infnet.edu.br.books_management.application.dto.BookHistoryResponseDTO;
import al.infnet.edu.br.books_management.domain.model.BookHistory;
import org.springframework.stereotype.Component;

@Component
public class BookHistoryMapper {

    public BookHistoryResponseDTO toResponseDTO(BookHistory bookHistory) {
        if (bookHistory == null) {
            return null;
        }

        return new BookHistoryResponseDTO(
                bookHistory.getId(),
                bookHistory.getBookId(),
                bookHistory.getTitle(),
                bookHistory.getAuthor(),
                bookHistory.getPublicationYear(),
                bookHistory.getOperationType().name(),
                bookHistory.getTimestamp(),
                bookHistory.getModifiedBy(),
                bookHistory.getChangeDescription()
        );
    }
}

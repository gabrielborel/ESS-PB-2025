package al.infnet.edu.br.books_management.application.mapper;

import al.infnet.edu.br.books_management.application.dto.BookCreateDTO;
import al.infnet.edu.br.books_management.application.dto.BookResponseDTO;
import al.infnet.edu.br.books_management.application.dto.BookUpdateDTO;
import al.infnet.edu.br.books_management.domain.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Book(dto.getTitle(), dto.getAuthor(), dto.getPublicationYear());
    }

    public BookResponseDTO toResponseDTO(Book book) {
        if (book == null) {
            return null;
        }
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear(),
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }

    public void updateEntityFromDTO(BookUpdateDTO dto, Book book) {
        if (dto == null || book == null) {
            return;
        }
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublicationYear(dto.getPublicationYear());
    }
}


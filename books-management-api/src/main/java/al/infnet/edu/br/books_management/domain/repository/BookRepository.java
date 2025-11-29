package al.infnet.edu.br.books_management.domain.repository;

import al.infnet.edu.br.books_management.domain.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}


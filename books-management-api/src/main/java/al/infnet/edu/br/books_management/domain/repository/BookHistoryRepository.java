package al.infnet.edu.br.books_management.domain.repository;

import al.infnet.edu.br.books_management.domain.model.BookHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookHistoryRepository extends JpaRepository<BookHistory, Long> {

    List<BookHistory> findByBookIdOrderByTimestampDesc(Long bookId);
}

package al.infnet.edu.br.books_management.application.service.impl;

import al.infnet.edu.br.books_management.application.dto.BookHistoryResponseDTO;
import al.infnet.edu.br.books_management.application.mapper.BookHistoryMapper;
import al.infnet.edu.br.books_management.application.service.BookHistoryService;
import al.infnet.edu.br.books_management.domain.repository.BookHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookHistoryServiceImpl implements BookHistoryService {

    private final BookHistoryRepository bookHistoryRepository;
    private final BookHistoryMapper bookHistoryMapper;

    public BookHistoryServiceImpl(BookHistoryRepository bookHistoryRepository, 
                                 BookHistoryMapper bookHistoryMapper) {
        this.bookHistoryRepository = bookHistoryRepository;
        this.bookHistoryMapper = bookHistoryMapper;
    }

    @Override
    public List<BookHistoryResponseDTO> getBookHistory(Long bookId) {
        return bookHistoryRepository.findByBookIdOrderByTimestampDesc(bookId)
                .stream()
                .map(bookHistoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}

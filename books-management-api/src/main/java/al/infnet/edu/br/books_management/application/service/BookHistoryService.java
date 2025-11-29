package al.infnet.edu.br.books_management.application.service;

import al.infnet.edu.br.books_management.application.dto.BookHistoryResponseDTO;

import java.util.List;

public interface BookHistoryService {

    List<BookHistoryResponseDTO> getBookHistory(Long bookId);
}

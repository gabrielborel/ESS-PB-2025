package al.infnet.edu.br.books_management.presentation.controller;

import al.infnet.edu.br.books_management.application.dto.BookHistoryResponseDTO;
import al.infnet.edu.br.books_management.application.service.BookHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookHistoryController {

    private final BookHistoryService bookHistoryService;

    public BookHistoryController(BookHistoryService bookHistoryService) {
        this.bookHistoryService = bookHistoryService;
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<BookHistoryResponseDTO>> getBookHistory(@PathVariable Long id) {
        List<BookHistoryResponseDTO> history = bookHistoryService.getBookHistory(id);
        return ResponseEntity.ok(history);
    }
}

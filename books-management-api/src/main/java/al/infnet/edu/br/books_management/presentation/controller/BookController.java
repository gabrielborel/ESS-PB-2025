package al.infnet.edu.br.books_management.presentation.controller;

import al.infnet.edu.br.books_management.application.dto.BookCreateDTO;
import al.infnet.edu.br.books_management.application.dto.BookResponseDTO;
import al.infnet.edu.br.books_management.application.dto.BookUpdateDTO;
import al.infnet.edu.br.books_management.application.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@Valid @RequestBody BookCreateDTO bookCreateDTO) {
        BookResponseDTO createdBook = bookService.create(bookCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        BookResponseDTO book = bookService.findById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        List<BookResponseDTO> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookUpdateDTO bookUpdateDTO) {
        BookResponseDTO updatedBook = bookService.update(id, bookUpdateDTO);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


package al.infnet.edu.br.books_management.application.service.impl;

import al.infnet.edu.br.books_management.application.dto.BookCreateDTO;
import al.infnet.edu.br.books_management.application.dto.BookResponseDTO;
import al.infnet.edu.br.books_management.application.dto.BookUpdateDTO;
import al.infnet.edu.br.books_management.application.mapper.BookMapper;
import al.infnet.edu.br.books_management.application.service.BookService;
import al.infnet.edu.br.books_management.domain.event.BookCreatedEvent;
import al.infnet.edu.br.books_management.domain.event.BookUpdatedEvent;
import al.infnet.edu.br.books_management.domain.event.BookDeletedEvent;
import al.infnet.edu.br.books_management.domain.exception.ResourceNotFoundException;
import al.infnet.edu.br.books_management.domain.model.Book;
import al.infnet.edu.br.books_management.domain.repository.BookRepository;
import al.infnet.edu.br.books_management.infrastructure.messaging.BookEventPublisher;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookEventPublisher eventPublisher;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper, BookEventPublisher eventPublisher) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public BookResponseDTO create(BookCreateDTO bookCreateDTO) {
        Book book = bookMapper.toEntity(bookCreateDTO);
        Book savedBook = bookRepository.save(book);
        
        eventPublisher.publishBookCreated(new BookCreatedEvent(
            savedBook.getId(),
            savedBook.getTitle(),
            savedBook.getAuthor()
        ));
        
        return bookMapper.toResponseDTO(savedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDTO findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", "id", id));
        return bookMapper.toResponseDTO(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponseDTO update(Long id, BookUpdateDTO bookUpdateDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", "id", id));

        bookMapper.updateEntityFromDTO(bookUpdateDTO, book);
        Book updatedBook = bookRepository.save(book);
        
        eventPublisher.publishBookUpdated(new BookUpdatedEvent(
            updatedBook.getId(),
            updatedBook.getTitle(),
            updatedBook.getAuthor()
        ));
        
        return bookMapper.toResponseDTO(updatedBook);
    }

    @Override
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", "id", id));

        bookRepository.delete(book);
        
        eventPublisher.publishBookDeleted(new BookDeletedEvent(id));
    }

}


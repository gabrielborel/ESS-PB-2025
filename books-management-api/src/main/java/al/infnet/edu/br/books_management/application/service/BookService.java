package al.infnet.edu.br.books_management.application.service;

import al.infnet.edu.br.books_management.application.dto.BookCreateDTO;
import al.infnet.edu.br.books_management.application.dto.BookResponseDTO;
import al.infnet.edu.br.books_management.application.dto.BookUpdateDTO;

import java.util.List;

public interface BookService {

    BookResponseDTO create(BookCreateDTO bookCreateDTO);

    BookResponseDTO findById(Long id);

    List<BookResponseDTO> findAll();

    BookResponseDTO update(Long id, BookUpdateDTO bookUpdateDTO);

    void delete(Long id);
}


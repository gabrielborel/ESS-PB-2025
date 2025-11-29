package al.infnet.edu.br.books_management.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class BookUpdateDTO {

    @NotBlank(message = "Título é obrigatório")
    private String title;

    @NotBlank(message = "Autor é obrigatório")
    private String author;

    @NotNull(message = "Ano é obrigatório")
    @Min(value = 1000, message = "Ano inválido")
    @Max(value = 2100, message = "Ano inválido")
    private Integer publicationYear;

    public BookUpdateDTO() {
    }

    public BookUpdateDTO(String title, String author, Integer publicationYear) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }
}


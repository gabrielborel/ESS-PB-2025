package al.infnet.edu.br.books_management.application.dto;

import java.time.LocalDateTime;

public class BookHistoryResponseDTO {

    private Long id;
    private Long bookId;
    private String title;
    private String author;
    private Integer publicationYear;
    private String operationType;
    private LocalDateTime timestamp;
    private String modifiedBy;
    private String changeDescription;

    public BookHistoryResponseDTO() {
    }

    public BookHistoryResponseDTO(Long id, Long bookId, String title, String author, 
                                 Integer publicationYear, String operationType, 
                                 LocalDateTime timestamp, String modifiedBy, 
                                 String changeDescription) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.operationType = operationType;
        this.timestamp = timestamp;
        this.modifiedBy = modifiedBy;
        this.changeDescription = changeDescription;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
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

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getChangeDescription() {
        return changeDescription;
    }

    public void setChangeDescription(String changeDescription) {
        this.changeDescription = changeDescription;
    }
}

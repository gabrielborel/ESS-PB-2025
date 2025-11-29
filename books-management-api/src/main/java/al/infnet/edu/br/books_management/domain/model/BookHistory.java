package al.infnet.edu.br.books_management.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "book_history")
public class BookHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String author;

    @Column(nullable = false)
    private Integer publicationYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OperationType operationType;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 100)
    private String modifiedBy;

    @Column(columnDefinition = "TEXT")
    private String changeDescription;

    public BookHistory() {
    }

    public BookHistory(Long bookId, String title, String author, Integer publicationYear, 
                      OperationType operationType, String modifiedBy) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.operationType = operationType;
        this.modifiedBy = modifiedBy;
        this.timestamp = LocalDateTime.now();
    }

    public enum OperationType {
        CREATE,
        UPDATE,
        DELETE
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

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookHistory that = (BookHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BookHistory{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", title='" + title + '\'' +
                ", operationType=" + operationType +
                ", timestamp=" + timestamp +
                '}';
    }
}

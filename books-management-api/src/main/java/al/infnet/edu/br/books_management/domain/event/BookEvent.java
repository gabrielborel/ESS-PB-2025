package al.infnet.edu.br.books_management.domain.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class BookEvent implements Serializable {
    private Long bookId;
    private String eventType;
    private LocalDateTime timestamp;

    public BookEvent() {
    }

    public BookEvent(Long bookId, String eventType) {
        this.bookId = bookId;
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

package al.infnet.edu.br.reviews.domain.event;

public class BookDeletedEvent extends BookEvent {

    public BookDeletedEvent() {
        super();
    }

    public BookDeletedEvent(Long bookId) {
        super(bookId, "BOOK_DELETED");
    }
}

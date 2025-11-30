package al.infnet.edu.br.reviews.domain.repository;

import al.infnet.edu.br.reviews.domain.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByBookId(Long bookId);

    Long countByBookId(Long bookId);

    @Aggregation(pipeline = {
        "{ $match: { book_id: ?0 } }",
        "{ $group: { _id: null, avgRating: { $avg: '$rating' } } }"
    })
    Double findAverageRatingByBookId(Long bookId);
}

package al.infnet.edu.br.reviews.application.mapper;

import al.infnet.edu.br.reviews.application.dto.ReviewCreateDTO;
import al.infnet.edu.br.reviews.application.dto.ReviewResponseDTO;
import al.infnet.edu.br.reviews.application.dto.ReviewUpdateDTO;
import al.infnet.edu.br.reviews.domain.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewCreateDTO dto) {
        return new Review(
                dto.bookId(),
                dto.reviewerName(),
                dto.rating(),
                dto.comment()
        );
    }

    public ReviewResponseDTO toResponseDTO(Review review) {
        return new ReviewResponseDTO(
                review.getId(),
                review.getBookId(),
                review.getReviewerName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }

    public void updateEntityFromDTO(ReviewUpdateDTO dto, Review review) {
        review.setReviewerName(dto.reviewerName());
        review.setRating(dto.rating());
        review.setComment(dto.comment());
        review.updateTimestamp();
    }
}

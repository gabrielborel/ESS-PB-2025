package al.infnet.edu.br.reviews.application.service;

import al.infnet.edu.br.reviews.application.dto.ReviewCreateDTO;
import al.infnet.edu.br.reviews.application.dto.ReviewResponseDTO;
import al.infnet.edu.br.reviews.application.dto.ReviewStatsDTO;
import al.infnet.edu.br.reviews.application.dto.ReviewUpdateDTO;

import java.util.List;

public interface ReviewService {
    
    ReviewResponseDTO createReview(ReviewCreateDTO dto);
    
    ReviewResponseDTO getReviewById(String id);
    
    List<ReviewResponseDTO> getAllReviews();
    
    List<ReviewResponseDTO> getReviewsByBookId(Long bookId);
    
    ReviewResponseDTO updateReview(String id, ReviewUpdateDTO dto);
    
    void deleteReview(String id);
    
    void deleteAllByBookId(Long bookId);
    
    ReviewStatsDTO getBookStats(Long bookId);
}

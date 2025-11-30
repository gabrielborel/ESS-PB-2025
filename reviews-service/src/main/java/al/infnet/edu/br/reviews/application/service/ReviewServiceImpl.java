package al.infnet.edu.br.reviews.application.service;

import al.infnet.edu.br.reviews.application.dto.ReviewCreateDTO;
import al.infnet.edu.br.reviews.application.dto.ReviewResponseDTO;
import al.infnet.edu.br.reviews.application.dto.ReviewStatsDTO;
import al.infnet.edu.br.reviews.application.dto.ReviewUpdateDTO;
import al.infnet.edu.br.reviews.application.mapper.ReviewMapper;
import al.infnet.edu.br.reviews.domain.model.Review;
import al.infnet.edu.br.reviews.domain.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    @Transactional
    public ReviewResponseDTO createReview(ReviewCreateDTO dto) {
        Review review = reviewMapper.toEntity(dto);
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toResponseDTO(savedReview);
    }

    @Override
    public ReviewResponseDTO getReviewById(String id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com ID: " + id));
        return reviewMapper.toResponseDTO(review);
    }

    @Override
    public List<ReviewResponseDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewResponseDTO> getReviewsByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId).stream()
                .map(reviewMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewResponseDTO updateReview(String id, ReviewUpdateDTO dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com ID: " + id));
        
        reviewMapper.updateEntityFromDTO(dto, review);
        Review updatedReview = reviewRepository.save(review);
        return reviewMapper.toResponseDTO(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(String id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Avaliação não encontrada com ID: " + id);
        }
        reviewRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllByBookId(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        if (!reviews.isEmpty()) {
            reviewRepository.deleteAll(reviews);
            System.out.println("Deletadas " + reviews.size() + " avaliações do livro " + bookId);
        }
    }

    @Override
    public ReviewStatsDTO getBookStats(Long bookId) {
        Long totalReviews = reviewRepository.countByBookId(bookId);
        Double averageRating = reviewRepository.findAverageRatingByBookId(bookId);
        
        return new ReviewStatsDTO(
                bookId,
                totalReviews,
                averageRating != null ? averageRating : 0.0
        );
    }
}

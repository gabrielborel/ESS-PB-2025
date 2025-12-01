package al.infnet.edu.br.reviews.presentation.controller;

import al.infnet.edu.br.reviews.application.dto.ReviewCreateDTO;
import al.infnet.edu.br.reviews.application.dto.ReviewResponseDTO;
import al.infnet.edu.br.reviews.application.dto.ReviewStatsDTO;
import al.infnet.edu.br.reviews.application.dto.ReviewUpdateDTO;
import al.infnet.edu.br.reviews.application.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewCreateDTO dto) {
        ReviewResponseDTO response = reviewService.createReview(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable String id) {
        ReviewResponseDTO response = reviewService.getReviewById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews() {
        List<ReviewResponseDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByBookId(@PathVariable Long bookId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByBookId(bookId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/book/{bookId}/stats")
    public ResponseEntity<ReviewStatsDTO> getBookStats(@PathVariable Long bookId) {
        ReviewStatsDTO stats = reviewService.getBookStats(bookId);
        return ResponseEntity.ok(stats);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable String id,
            @Valid @RequestBody ReviewUpdateDTO dto) {
        ReviewResponseDTO response = reviewService.updateReview(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}

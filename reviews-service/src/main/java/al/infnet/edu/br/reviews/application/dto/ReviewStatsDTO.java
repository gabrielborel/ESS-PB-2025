package al.infnet.edu.br.reviews.application.dto;

public record ReviewStatsDTO(
        Long bookId,
        Long totalReviews,
        Double averageRating
) {
}

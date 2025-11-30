package al.infnet.edu.br.reviews.application.dto;

import java.time.LocalDateTime;

public record ReviewResponseDTO(
        String id,
        Long bookId,
        String reviewerName,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

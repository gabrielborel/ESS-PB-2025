package al.infnet.edu.br.reviews.application.dto;

import jakarta.validation.constraints.*;

public record ReviewUpdateDTO(
        @NotBlank(message = "O nome do revisor é obrigatório")
        @Size(max = 100, message = "O nome do revisor deve ter no máximo 100 caracteres")
        String reviewerName,

        @NotNull(message = "A nota é obrigatória")
        @Min(value = 1, message = "A nota mínima é 1")
        @Max(value = 5, message = "A nota máxima é 5")
        Integer rating,

        @NotBlank(message = "O comentário é obrigatório")
        @Size(max = 1000, message = "O comentário deve ter no máximo 1000 caracteres")
        String comment
) {
}

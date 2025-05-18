package ir.speedy.computerworkshopproject.dto.Book;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class BookUpdateRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String author;


    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
}

package ir.speedy.computerworkshopproject.dto.Book;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookAddRequest {

    @NotBlank(message = "name cannot be Blank")
    private String name;

    @NotBlank(message = "author cannot be Blank")
    private String author;

    @NotNull(message = "price cannot be Null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

}

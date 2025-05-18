package ir.speedy.computerworkshopproject.dto.Book;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;
@Data
public class BookResponse {
    private UUID id;
    private String name;
    private String author;
    private BigDecimal price;
}

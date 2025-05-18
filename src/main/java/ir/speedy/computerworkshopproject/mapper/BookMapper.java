package ir.speedy.computerworkshopproject.mapper;

import ir.speedy.computerworkshopproject.dto.Book.BookResponse;
import ir.speedy.computerworkshopproject.models.Book;

public class BookMapper {

    public static BookResponse toBookResponseDto(Book sneaker) {
        BookResponse dto = new BookResponse();
        dto.setId(sneaker.getId());
        dto.setName(sneaker.getName());
        dto.setAuthor(sneaker.getAuthor());
        dto.setPrice(sneaker.getPrice());
        return dto;
    }
}

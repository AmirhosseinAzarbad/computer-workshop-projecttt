package ir.speedy.computerworkshopproject.services;

import ir.speedy.computerworkshopproject.Repositories.BookRepository;
import ir.speedy.computerworkshopproject.dto.Book.BookAddRequest;
import ir.speedy.computerworkshopproject.dto.Book.BookResponse;
import ir.speedy.computerworkshopproject.dto.Book.BookUpdateRequest;
import ir.speedy.computerworkshopproject.mapper.BookMapper;
import ir.speedy.computerworkshopproject.models.Book;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepo;
    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Transactional
    public BookResponse addBook (BookAddRequest req){
        Book book = new Book();
        book.setAuthor(req.getAuthor());
        book.setName(req.getName());
        book.setPrice(req.getPrice());
        bookRepo.save(book);
        return BookMapper.toBookResponseDto(book);
    }

    public Iterable<BookResponse> getAllBooks (){
        List<Book> books = bookRepo.findAll();

        return books.stream()
                .map(BookMapper::toBookResponseDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public BookResponse updateBook (UUID id , BookUpdateRequest req){
        Book book = bookRepo.findById(id)
                .orElseThrow(() ->  new EntityNotFoundException("Book not found"));

        if (req.getAuthor() != null){
            book.setAuthor(req.getAuthor());
        }
        if (req.getPrice() != null){
            book.setPrice(req.getPrice());
        }
        if (book.getName() != null){
            book.setName(req.getName());
        }

        bookRepo.save(book);
        return BookMapper.toBookResponseDto(book);
    }


    @Transactional
    public void deleteBook (UUID id){
        Book book = bookRepo.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        bookRepo.delete(book);
    }


}

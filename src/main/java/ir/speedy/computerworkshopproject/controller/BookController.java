package ir.speedy.computerworkshopproject.controller;

import ir.speedy.computerworkshopproject.dto.Book.BookAddRequest;
import ir.speedy.computerworkshopproject.dto.Book.BookResponse;
import ir.speedy.computerworkshopproject.dto.Book.BookUpdateRequest;
import ir.speedy.computerworkshopproject.services.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<BookResponse> addBook(@Valid @RequestBody BookAddRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(request));
    }

    @GetMapping("/show")
    public ResponseEntity<Iterable<BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<BookResponse> updateBook(@PathVariable UUID id, @Valid @RequestBody BookUpdateRequest request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER')")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}

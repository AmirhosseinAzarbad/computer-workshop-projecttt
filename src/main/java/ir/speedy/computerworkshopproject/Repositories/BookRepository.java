package ir.speedy.computerworkshopproject.Repositories;

import ir.speedy.computerworkshopproject.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
}

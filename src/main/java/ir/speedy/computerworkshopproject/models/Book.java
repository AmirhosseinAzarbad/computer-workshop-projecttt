package ir.speedy.computerworkshopproject.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name1",nullable = false)
    private String name;

    @Column(name = "author1",nullable = false)
    private String author;

    @Column(name = "price1",nullable = false)
    private BigDecimal price;

}

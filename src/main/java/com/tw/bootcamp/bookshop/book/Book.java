package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String authorName;
    @Embedded
    private Money price;

    private String image;
    private Long isbnNumber;
    private String description;

    private double rating;
    @Builder.Default
    private long quantity = 5;

    public void updateQuantity(long quantity) {
        this.quantity = quantity;
    }

    public BookResponse toResponse() {
        return BookResponse.builder()
                .id(id)
                .name(name)
                .authorName(authorName)
                .price(price)
                .image(image)
                .quantity(quantity)
                .description(description)
                .rating(rating)
                .reviews(new ArrayList<>())
                .build();
    }

    public BookResponse toResponse(List<Review> reviews) {
        return BookResponse.builder()
                .id(id)
                .name(name)
                .authorName(authorName)
                .price(price)
                .image(image)
                .quantity(quantity)
                .description(description)
                .rating(rating)
                .reviews(reviews)
                .build();
    }
}

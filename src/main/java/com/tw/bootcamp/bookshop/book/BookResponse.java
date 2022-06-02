package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Long id;
    private String name;
    private String authorName;
    private Money price;
    private String image;
    private long quantity;
    private String description;
    private double rating;
    private List<Review> reviews;
}

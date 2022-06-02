package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;

public class BookTestBuilder {
    private final Book.BookBuilder bookBuilder;

    public BookTestBuilder() {
        bookBuilder = Book.builder().name("Harry Potter")
                .authorName("J K Rowling")
                .price(Money.rupees(300))
                .quantity(5);
    }

    public Book build() {
        return bookBuilder.build();
    }

    public BookTestBuilder withPrice(int price) {
        bookBuilder.price(Money.rupees(price));
        return this;
    }

    public BookTestBuilder withName(String name) {
        bookBuilder.name(name);
        return this;
    }


    public BookTestBuilder withImage(String image) {
        bookBuilder.image(image);
        return this;
    }

    public BookTestBuilder withDescription(String description) {
        bookBuilder.description(description);
        return this;
    }

    public BookTestBuilder withRating(double rating) {
        bookBuilder.rating(rating);
        return this;
    }

    public BookTestBuilder withId(long id) {
        bookBuilder.id(id);
        return this;
    }

}

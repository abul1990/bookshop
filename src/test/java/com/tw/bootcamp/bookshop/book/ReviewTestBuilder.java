package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewTestBuilder {
    private final Review.ReviewBuilder reviewBuilder;

    public ReviewTestBuilder() {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        reviewBuilder = Review.builder()
                .id(1L)
                .postedBy("Guest1")
                .message("Good to read")
                .timeStamp(timestamp.getTime());
    }

    public Review build() {
        return reviewBuilder.build();
    }

    public ReviewTestBuilder withBook(Book book) {
        reviewBuilder.book(book);
        return this;
    }

    public static List<Review> createBookReviewListWithOneItem(Book book)
    {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        Review review = new Review(
                1L,
                "Guest1",
                "Good to read",
                timestamp.getTime(),
                book);
        List<Review> reviews = new ArrayList<>();
        reviews.add(review);
        return reviews;
    }

    public static List<Review> createBookReviewListWithTwoItems(Book book)
    {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        Review review1 = new Review(
                1L,
                "Guest1",
                "Good to read",
                timestamp.getTime(),
                book);
        Review review2 = new Review(
                2L,
                "Guest2",
                "Excellent",
                timestamp.getTime(),
                book);
        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);
        return reviews;
    }

    public static List<Review> createBookReviewListWithTwoItemsForTwoDifferentBooks(Book book1, Book book2)
    {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        Review review1 = new Review(
                1L,
                "Guest1",
                "Good to read",
                timestamp.getTime(),
                book1);
        Review review2 = new Review(
                2L,
                "Guest2",
                "Excellent",
                timestamp.getTime(),
                book1);
        Review review3 = new Review(
                3L,
                "Guest3",
                "Good to read",
                timestamp.getTime(),
                book2);
        Review review4 = new Review(
                4L,
                "Guest4",
                "Excellent",
                timestamp.getTime(),
                book2);
        List<Review> reviews = new ArrayList<>();
        reviews.add(review1);
        reviews.add(review2);
        reviews.add(review3);
        reviews.add(review4);
        return reviews;
    }
}

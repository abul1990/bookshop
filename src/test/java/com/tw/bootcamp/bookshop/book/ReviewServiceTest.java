package com.tw.bootcamp.bookshop.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @Test
    void shouldReturnEmptyListWhenNoReviewsAvailableForGivenBook() {
        Book book = new BookTestBuilder().build();
        bookRepository.save(book);

        List<Review> reviewList = reviewService.getReviewsByBookId(book.getId());

        assertEquals(0, reviewList.size());
    }

    @Test
    void shouldReturnOneReviewWhenOneReviewIsAddedForGivenBook() {
        Book book = new BookTestBuilder().build();
        bookRepository.save(book);
        Review review = new ReviewTestBuilder().withBook(book).build();
        reviewRepository.save(review);

        List<Review> reviewList = reviewService.getReviewsByBookId(book.getId());

        assertEquals(1, reviewList.size());
    }

    @Test
    void shouldReturnTwoReviewsWhenTwoReviewsAddedForSameBook() {
        Book book = new BookTestBuilder().build();
        bookRepository.save(book);
        List<Review> reviewListWithTwoItems = ReviewTestBuilder.createBookReviewListWithTwoItems(book);
        reviewRepository.saveAll(reviewListWithTwoItems);

        List<Review> reviewList = reviewService.getReviewsByBookId(book.getId());

        assertEquals(2, reviewList.size());
        assertEquals(book.getId(), reviewList.get(0).getBook().getId());
        assertEquals(book.getId(), reviewList.get(1).getBook().getId());
    }

    @Test
    void shouldReturnReviewsListAddedForGivenBookWhenMultipleReviewsAddedForDifferentBooks() {
        Book oneBook = new BookTestBuilder().build();
        bookRepository.save(oneBook);
        Book anotherBook = new BookTestBuilder().build();
        bookRepository.save(anotherBook);
        List<Review> originalReviewList = ReviewTestBuilder
                .createBookReviewListWithTwoItemsForTwoDifferentBooks(oneBook, anotherBook);
        reviewRepository.saveAll(originalReviewList);

        List<Review> reviewListForOneBook = reviewService.getReviewsByBookId(oneBook.getId());

        assertEquals(4, reviewRepository.findAll().size());
        assertEquals(2, reviewListForOneBook.size());
        assertEquals(oneBook.getId(), reviewListForOneBook.get(0).getBook().getId());
        assertEquals(oneBook.getId(), reviewListForOneBook.get(1).getBook().getId());
    }
}
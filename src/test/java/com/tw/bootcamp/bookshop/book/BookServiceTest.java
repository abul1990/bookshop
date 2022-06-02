package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.error.BookNotAvailableException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Autowired
    private BookRepository bookRepository;

    @InjectMocks
    @Autowired
    private BookService bookService;

    @Mock
    private ReviewService reviewService;

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Nested
    class BookList {
        @Test
        void shouldFetchAllBooks() {
            Book book = new BookTestBuilder().withName("title").build();
            bookRepository.save(book);

            List<Book> books = bookService.fetchAll();

            assertEquals(1, books.size());
            assertEquals("title", books.get(0).getName());
        }

        @Test
        void shouldFetchAllBooksBeSortedByNameAscending() {
            Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire").build();
            Book animalFarm = new BookTestBuilder().withName("Animal Farm").build();
            bookRepository.save(wingsOfFire);
            bookRepository.save(animalFarm);

            List<Book> books = bookService.fetchAll();

            assertEquals(2, books.size());
            assertEquals("Animal Farm", books.get(0).getName());
        }

        @Test
        void shouldFetchAllBooksWithImages() {

            Book book = new BookTestBuilder().withName("title").withImage("image").build();
            bookRepository.save(book);

            List<Book> books = bookService.fetchAll();

            assertEquals(1, books.size());
            assertEquals("image", books.get(0).getImage());

        }
    }

    @Nested
    class BookSearch {
        @Test
        void shouldFetchAllBooksWhenBookInfoMatchWithBookNameOrAuthorName() {
            String bookName = "Harry Potter";
            Book book = new BookTestBuilder().withName(bookName).build();
            bookRepository.save(book);

            List<Book> books = bookService.fetchAllByAuthorNameOrByBookNameByOrderByNameAsc(bookName);

            assertEquals(1, books.size());
            assertEquals(bookName, books.get(0).getName());
        }

        @Test
        void shouldNoBookWhenBookInfoIsNotMatchingWithBookNameOrAuthorName() {
            String bookName = "Harry Potter";
            Book book = new BookTestBuilder().withName(bookName).build();
            bookRepository.save(book);

            String anotherBookName = "Harry Potter Volume 2";
            List<Book> books = bookService.fetchAllByAuthorNameOrByBookNameByOrderByNameAsc(anotherBookName);

            assertEquals(0, books.size());
        }

    }

    @Nested
    class DeleteBook {
        @Test
        void shouldRemoveBookSuccessfullyOnDeletingAvailableBook() throws BookNotAvailableException {
            String bookName = "Harry Potter";
            Book book = new BookTestBuilder().withName(bookName).build();
            book = bookRepository.save(book);

            bookService.removeBook(book);

            assertFalse(bookService.fetchBookById(book.getId()).isPresent());
        }

        @Test
        void shouldThrowBookNotAvailableExceptionOnDeletingUnAvailableBook() throws BookNotAvailableException {
            String bookName = "Harry Potter";
            Book book = new BookTestBuilder().withName(bookName).build();
            book = bookRepository.save(book);
            bookService.removeBook(book);

            Book finalBook = book;
            assertThrows(BookNotAvailableException.class, () -> {
                bookService.removeBook(finalBook);
            });
        }
    }

    @Nested
    class UpdateBook {
        @Test
        void shouldUpdateBookTotalAvailableCountGivenNoOfCopiesPurchased() throws BookNotAvailableException {
            String bookName = "Harry Potter";
            Book book = new BookTestBuilder().withName(bookName).build();
            book = bookRepository.save(book);

            long updatedQuantity = book.getQuantity() - 1;
            book.updateQuantity(updatedQuantity);
            bookService.updateBook(book);

            assertTrue(bookService.fetchBookById(book.getId()).get().getQuantity() == updatedQuantity);
        }
    }

    @Nested
    class ViewBookDetails {
        @Test
        void shouldFetchBookDetailsWhenBookIsAvailable() throws BookNotAvailableException {
            Book book = new BookTestBuilder()
                    .withName("title")
                    .withDescription("description")
                    .withRating(4.6)
                    .build();
            bookRepository.save(book);

            BookResponse bookResponse = bookService.viewBookDetails(book.getId());

            assertEquals("title", bookResponse.getName());
            assertEquals("description", bookResponse.getDescription());
            assertEquals(4.6, bookResponse.getRating());
        }

        @Test
        void shouldFetchBookDetailsWithReviewsWhenBookIsAvailableAndReviewsAdded() throws BookNotAvailableException {
            Book book = new BookTestBuilder()
                    .withName("title")
                    .build();
            bookRepository.save(book);
            List<Review> reviews = ReviewTestBuilder.createBookReviewListWithTwoItems(book);
            when(reviewService.getReviewsByBookId(book.getId())).thenReturn(reviews);

            BookResponse bookResponse = bookService.viewBookDetails(book.getId());

            assertEquals(2, bookResponse.getReviews().size());
        }

        @Test
        void shouldNotDisplayBookDetailsWhenBookIsNotFound() throws BookNotAvailableException {
            Book book = new BookTestBuilder()
                    .withId(1)
                    .withName("title")
                    .build();

            assertThrows(BookNotAvailableException.class, ()->{
                bookService.viewBookDetails(book.getId());
            });
        }
    }
}
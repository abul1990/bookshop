package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.error.BookNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    @Autowired
    private ReviewService reviewService;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> fetchAll() {
        return bookRepository.findAllByOrderByNameAsc();
    }

    public List<Book> fetchAllByAuthorNameOrByBookNameByOrderByNameAsc(String bookInfo) {
        return bookRepository.fetchAllByAuthorNameOrByBookNameByOrderByNameAsc(bookInfo);
    }

    public Optional<Book> fetchBookById(Long bookId) {
        return this.bookRepository.findById(bookId);
    }

    public void removeBook(Book book) throws BookNotAvailableException {
        if(!fetchBookById(book.getId()).isPresent()) {
            throw new BookNotAvailableException("Book not found");
        }
        this.bookRepository.delete(book);
    }

    public void updateBook(Book book) throws BookNotAvailableException {
        if(!fetchBookById(book.getId()).isPresent()) {
            throw new BookNotAvailableException("Book not found");
        }
        this.bookRepository.save(book);
    }

    public void updateBookTotalAvailableCount(Book book, int noOfCopies) throws BookNotAvailableException {
        book.updateQuantity(book.getQuantity() - noOfCopies);
        updateBook(book);
    }

    public BookResponse viewBookDetails(long bookId) throws BookNotAvailableException {
        Optional<Book> book = bookRepository.findById(bookId);
        if(!book.isPresent()){
            throw new BookNotAvailableException("Book not found");
        }

        List<Review> reviews = reviewService.getReviewsByBookId(bookId);
        return book.get().toResponse(reviews);
    }
}

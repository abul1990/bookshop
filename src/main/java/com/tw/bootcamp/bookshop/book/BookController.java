package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.error.BookNotAvailableException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/search")
    @Operation(summary = "Search books", description = "Search books in bookshop", tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List all books",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class))})
    })
    List<BookResponse> search(@RequestParam(name = "bookInfo", defaultValue = "", required = false) String bookInfo) {
        List<Book> books = bookService.fetchAllByAuthorNameOrByBookNameByOrderByNameAsc(bookInfo);
        return books.stream()
                .map(book -> book.toResponse())
                .collect(Collectors.toList());
    }

    @GetMapping("/books")
    @Operation(summary = "List all books", description = "Lists all books in bookshop", tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List all books",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class))})
    })
    List<BookResponse> list() {
        List<Book> books = bookService.fetchAll();
        return books.stream()
                .map(book -> book.toResponse())
                .collect(Collectors.toList());
    }

    @RequestMapping("/books/{id}")
    @Operation(summary = "View book details", description = "View book details of given book id", tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "View book details",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class))})
    })
    ResponseEntity<BookResponse> viewBookDetails(@PathVariable long id) throws BookNotAvailableException {
        return new ResponseEntity<>(bookService.viewBookDetails(id), HttpStatus.OK);
    }

}

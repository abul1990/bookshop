package com.tw.bootcamp.bookshop.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.error.BookNotAvailableException;
import com.tw.bootcamp.bookshop.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@WithMockUser
class  BookControllerTest {
    @MockBean
    UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        Mockito.reset(bookService);
    }

    @Nested
    class BookList {

        @Test
        void shouldListAllBooksWhenPresent() throws Exception {
            List<Book> books = new ArrayList<>();
            Book book = new BookTestBuilder().build();
            books.add(book);
            when(bookService.fetchAll()).thenReturn(books);

            mockMvc.perform(get("/books").with(user("admin").password("admin").roles("USER", "ADMIN")).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
            verify(bookService, times(1)).fetchAll();
        }

        @Test
        void shouldBeEmptyListWhenNoBooksPresent() throws Exception {
            when(bookService.fetchAll()).thenReturn(new ArrayList<>());

            mockMvc.perform(get("/books").with(user("admin").password("admin").roles("USER", "ADMIN")).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
            verify(bookService, times(1)).fetchAll();
        }
    }

    @Nested
    class BookSearch {


        @Test
        void shouldListBooksWhenUserSearchWithAuthorName() throws Exception {
            List<Book> books = new ArrayList<>();
            Book book = new BookTestBuilder().build();
            books.add(book);
            when(bookService.fetchAllByAuthorNameOrByBookNameByOrderByNameAsc(ArgumentMatchers.any(String.class))).thenReturn(books);

            mockMvc.perform(get("/books/search").with(user("admin").password("admin").roles("USER", "ADMIN")).queryParam("bookInfo", "J K Rowling").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
            verify(bookService, times(1)).fetchAllByAuthorNameOrByBookNameByOrderByNameAsc(ArgumentMatchers.any(String.class));
        }

        @Test
        void shouldListBooksWhenUserSearchWithBookName() throws Exception {
            List<Book> books = new ArrayList<>();
            Book book = new BookTestBuilder().build();
            books.add(book);
            when(bookService.fetchAllByAuthorNameOrByBookNameByOrderByNameAsc(ArgumentMatchers.any(String.class))).thenReturn(books);

            mockMvc.perform(get("/books/search").with(user("admin").password("admin").roles("USER", "ADMIN")).queryParam("bookInfo", "Harry Potter").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
            verify(bookService, times(1)).fetchAllByAuthorNameOrByBookNameByOrderByNameAsc(ArgumentMatchers.any(String.class));
        }

        @Test
        void shouldListBooksWhenUserSearchWithBothBookNameAndAuthorName() throws Exception {
            List<Book> books = new ArrayList<>();
            Book book = new BookTestBuilder().build();
            books.add(book);
            when(bookService.fetchAllByAuthorNameOrByBookNameByOrderByNameAsc(ArgumentMatchers.any(String.class))).thenReturn(books);

            mockMvc.perform(get("/books/search").with(user("admin").password("admin").roles("USER", "ADMIN")).queryParam("bookInfo", "J K Rowling").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
            verify(bookService, times(1)).fetchAllByAuthorNameOrByBookNameByOrderByNameAsc(ArgumentMatchers.any(String.class));
        }

        @Test
        void shouldBeEmptyListWhenNoBooksPresent() throws Exception {
            when(bookService.fetchAllByAuthorNameOrByBookNameByOrderByNameAsc(ArgumentMatchers.any(String.class))).thenReturn(new ArrayList<>());

            mockMvc.perform(get("/books/search").with(user("admin").password("admin").roles("USER", "ADMIN")).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
            verify(bookService, times(1)).fetchAllByAuthorNameOrByBookNameByOrderByNameAsc(ArgumentMatchers.any(String.class));
        }
    }

    @Nested
    class ViewBookDetails {
        @Test
        void shouldBeAbleToViewBookDetailsWhenBookIsAvailable() throws Exception {
            Book book = new BookTestBuilder().withId(1000).build();
            BookResponse bookResponse = new BookResponse(
                    book.getId(),
                    book.getName(),
                    book.getAuthorName(),
                    book.getPrice(),
                    book.getImage(),
                    book.getQuantity(),
                    book.getDescription(),
                    book.getRating(),
                    new ArrayList<>()
            );
            when(bookService.viewBookDetails(book.getId())).thenReturn(bookResponse);

            mockMvc.perform(get("/books/" + book.getId()).
                            with(user("admin").
                                    password("admin").
                                    roles("USER", "ADMIN")).
                            contentType(MediaType.APPLICATION_JSON)).
                    andExpect(status().isOk()).
                    andExpect(content().string(objectMapper.writeValueAsString(bookResponse)));
            verify(bookService, times(1)).viewBookDetails(book.getId());
        }

        @Test
        void shouldThrowBookNotAvailableExceptionWhenBookIsNotFound() throws Exception {
            Book book = new BookTestBuilder().withId(1000).build();
            when(bookService.viewBookDetails(book.getId())).thenThrow(new BookNotAvailableException());

            mockMvc.perform(get("/books/" + book.getId()).
                            with(user("admin").
                                    password("admin").
                                    roles("USER", "ADMIN")).
                            contentType(MediaType.APPLICATION_JSON)).
                    andExpect(status().isNotFound());
            verify(bookService, times(1)).viewBookDetails(book.getId());
        }

        @Test
        void shouldBeAbleToViewBookDetailsWithReviewsWhenBookIsAvailable() throws Exception {
            Book book = new BookTestBuilder().withId(1000).build();
            BookResponse bookResponse = new BookResponse(
                    book.getId(),
                    book.getName(),
                    book.getAuthorName(),
                    book.getPrice(),
                    book.getImage(),
                    book.getQuantity(),
                    book.getDescription(),
                    book.getRating(),
                    ReviewTestBuilder.createBookReviewListWithTwoItems(book)
            );
            when(bookService.viewBookDetails(book.getId())).thenReturn(bookResponse);

            mockMvc.perform(get("/books/" + book.getId()).
                            with(user("admin").
                                    password("admin").
                                    roles("USER", "ADMIN")).
                            contentType(MediaType.APPLICATION_JSON)).
                    andExpect(status().isOk()).
                    andExpect(content().string(objectMapper.writeValueAsString(bookResponse))).
                    andExpect(jsonPath("$.reviews.length()").value(2));
            verify(bookService, times(1)).viewBookDetails(book.getId());
        }
    }
}
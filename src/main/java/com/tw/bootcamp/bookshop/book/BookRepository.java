package com.tw.bootcamp.bookshop.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByOrderByNameAsc();

    @Query("FROM Book book WHERE LOWER(book.name) LIKE LOWER(concat('%',:bookInfo,'%')) OR LOWER(book.authorName) LIKE LOWER(concat('%',:bookInfo,'%')) ORDER BY book.name ASC")
    List<Book> fetchAllByAuthorNameOrByBookNameByOrderByNameAsc(String bookInfo);

}

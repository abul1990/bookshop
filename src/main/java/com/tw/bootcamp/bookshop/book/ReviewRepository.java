package com.tw.bootcamp.bookshop.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("FROM Review review WHERE review.book.id = :bookId ORDER BY timeStamp DESC")
    List<Review> fetchAllByBookIdAndOrderByLatestTimeStamp(long bookId);
}

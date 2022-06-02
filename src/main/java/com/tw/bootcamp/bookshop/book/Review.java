package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.user.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String postedBy;
    private String message;
    private long timeStamp;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}

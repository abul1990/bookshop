ALTER TABLE orders
    ADD CONSTRAINT fk_orders_books FOREIGN KEY (book_id) REFERENCES books (id);
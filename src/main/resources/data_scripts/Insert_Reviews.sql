-- INSERT REVIEWS FOR BOOK WITH ISBN - 1416914285 & Name - City of Bones (The Mortal Instruments, #1) --
INSERT INTO REVIEWS(posted_by, message, time_stamp, book_id)
VALUES('Guest1', 'I love this series so much!',
(extract(epoch from now()) * 1000),
(SELECT id from BOOKS WHERE isbn_number=1416914285));

INSERT INTO REVIEWS(posted_by, message, time_stamp, book_id)
VALUES('Guest2', 'A Spectacular YA Fantasy Series!',
(extract(epoch from now()) * 1000),
(SELECT id from BOOKS WHERE isbn_number=1416914285));

INSERT INTO REVIEWS(posted_by, message, time_stamp, book_id)
VALUES('Guest3', 'Not the best book ever but a solid fantasy adventure',
(extract(epoch from now()) * 1000),
(SELECT id from BOOKS WHERE isbn_number=1416914285));

INSERT INTO REVIEWS(posted_by, message, time_stamp, book_id)
VALUES('Guest4', 'Great book!',
(extract(epoch from now()) * 1000),
(SELECT id from BOOKS WHERE isbn_number=1416914285));

INSERT INTO REVIEWS(posted_by, message, time_stamp, book_id)
VALUES('Guest5', 'An Amazing Novel!!',
(extract(epoch from now()) * 1000),
(SELECT id from BOOKS WHERE isbn_number=1416914285));

-- INSERT REVIEWS FOR BOOK WITH ISBN - 316160202 & Name - Eclipse (Twilight, #3) --
INSERT INTO REVIEWS(posted_by, message, time_stamp, book_id)
VALUES('Guest1', 'Eclipse, just like Twilight and New Moon, is a surprisingly good read.',
(extract(epoch from now()) * 1000),
(SELECT id from BOOKS WHERE isbn_number=316160202));

INSERT INTO REVIEWS(posted_by, message, time_stamp, book_id)
VALUES('Guest2', 'BEST BOOK EVER!!!',
(extract(epoch from now()) * 1000),
(SELECT id from BOOKS WHERE isbn_number=316160202));

INSERT INTO REVIEWS(posted_by, message, time_stamp, book_id)
VALUES('Guest3', 'If you want a break from reality then this book is your best escape',
(extract(epoch from now()) * 1000),
(SELECT id from BOOKS WHERE isbn_number=316160202));

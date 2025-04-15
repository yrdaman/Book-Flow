CREATE TABLE book_reviews (
    book_review_id int NOT NULL AUTO_INCREMENT,
    user_book_id int DEFAULT NULL,
    reviewer_id int DEFAULT NULL,
    rating int DEFAULT NULL,
    comment text,
    review_date date DEFAULT NULL,
    PRIMARY KEY (book_review_id),
    KEY user_book_id (user_book_id),
    KEY reviewer_id (reviewer_id),
    CONSTRAINT book_reviews_ibfk_1 FOREIGN KEY (user_book_id) REFERENCES user_books (user_book_id),
    CONSTRAINT book_reviews_ibfk_2 FOREIGN KEY (reviewer_id) REFERENCES users (user_id)
);
CREATE TABLE users (
                         user_id int NOT NULL AUTO_INCREMENT,
                         name varchar(100) DEFAULT NULL,
                         email varchar(100) DEFAULT NULL,
                         password_hash varchar(255) DEFAULT NULL,
                         location varchar(100) DEFAULT NULL,
                         created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (user_id),
                         UNIQUE KEY email (email)
);
CREATE TABLE user_books (
                              user_book_id int NOT NULL AUTO_INCREMENT,
                              user_id int DEFAULT NULL,
                              book_id int DEFAULT NULL,
                              status enum('owned','wishlist') DEFAULT NULL,
                              available tinyint(1) DEFAULT '1',
                              is_digital tinyint(1) DEFAULT '0',
                              share_method varchar(20) DEFAULT NULL,
                              shared_note text,
                              payment_required tinyint(1) DEFAULT '0',
                              payment_note text,
                              PRIMARY KEY (user_book_id),
                              KEY user_id (user_id),
                              KEY book_id (book_id),
                              CONSTRAINT user_books_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (user_id),
                              CONSTRAINT user_books_ibfk_2 FOREIGN KEY (book_id) REFERENCES books (book_id)
);

CREATE TABLE payments (
                            payment_id int NOT NULL AUTO_INCREMENT,
                            borrow_id int DEFAULT NULL,
                            amount decimal(10,2) DEFAULT NULL,
                            payment_method varchar(50) DEFAULT NULL,
                            payment_note text,
                            payment_status varchar(20) DEFAULT 'pending',
                            created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (payment_id),
                            KEY borrow_id (borrow_id),
                            CONSTRAINT payments_ibfk_1 FOREIGN KEY (borrow_id) REFERENCES borrows (borrow_id)
);
CREATE TABLE notifications (
                                 notification_id int NOT NULL AUTO_INCREMENT,
                                 user_id int DEFAULT NULL,
                                 message text,
                                 is_read tinyint(1) DEFAULT '0',
                                 created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (notification_id),
                                 KEY user_id (user_id),
                                 CONSTRAINT notifications_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (user_id)
);
CREATE TABLE notifications (
                                 notification_id int NOT NULL AUTO_INCREMENT,
                                 user_id int DEFAULT NULL,
                                 message text,
                                 is_read tinyint(1) DEFAULT '0',
                                 created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (notification_id),
                                 KEY user_id (user_id),
                                 CONSTRAINT notifications_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (user_id)
);
CREATE TABLE borrows (
                           borrow_id int NOT NULL AUTO_INCREMENT,
                           lender_id int DEFAULT NULL,
                           borrower_id int DEFAULT NULL,
                           user_book_id int DEFAULT NULL,
                           borrow_date date DEFAULT NULL,
                           due_date date DEFAULT NULL,
                           actual_return_date date DEFAULT NULL,
                           status enum('requested','accepted','rejected','returned') DEFAULT 'requested',
                           PRIMARY KEY (borrow_id),
                           KEY lender_id (lender_id),
                           KEY borrower_id (borrower_id),
                           KEY user_book_id (user_book_id),
                           CONSTRAINT borrows_ibfk_1 FOREIGN KEY (lender_id) REFERENCES users (user_id),
                           CONSTRAINT borrows_ibfk_2 FOREIGN KEY (borrower_id) REFERENCES users (user_id),
                           CONSTRAINT borrows_ibfk_3 FOREIGN KEY (user_book_id) REFERENCES user_books (user_book_id)
);
CREATE TABLE books (
                                     book_id int NOT NULL AUTO_INCREMENT,
                                     title varchar(150) DEFAULT NULL,
                                     author varchar(100) DEFAULT NULL,
                                     genre varchar(50) DEFAULT NULL,
                                     isbn varchar(50) DEFAULT NULL,
                                     publication_year int DEFAULT NULL,
                                     PRIMARY KEY (book_id)
            );

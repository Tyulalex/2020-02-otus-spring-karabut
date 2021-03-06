insert into BOOKS (id, `title`) values (BOOK_SEQ_ID.nextval, 'three pigs');
insert into AUTHORS (id, `author_name`) values (AUTHOR_SEQ_ID.nextval, 'David Wiesner');
insert into GENRES (id, `genre_name`) values (GENRE_SEQ_ID.nextval, 'Children''s picture book');
insert into BOOKS_AUTHORS (book_id, author_id) values (BOOK_SEQ_ID.currval, AUTHOR_SEQ_ID.currval);
insert into BOOKS_GENRES (book_id, genre_id) values (BOOK_SEQ_ID.currval, GENRE_SEQ_ID.currval);

insert into BOOKS (id, `title`) values (BOOK_SEQ_ID.nextval, 'EUGENE ONEGIN');
insert into AUTHORS (id, `author_name`) values (AUTHOR_SEQ_ID.nextval, 'Alexander Pushkin');
insert into BOOKS_AUTHORS (book_id, author_id) values (BOOK_SEQ_ID.currval, AUTHOR_SEQ_ID.currval);
insert into AUTHORS (id, `author_name`) values (AUTHOR_SEQ_ID.nextval, 'Anna Maude (Translator)');
insert into BOOKS_AUTHORS (book_id, author_id) values (BOOK_SEQ_ID.currval, AUTHOR_SEQ_ID.currval);
insert into GENRES (id, `genre_name`) values (GENRE_SEQ_ID.nextval, 'Poem');
insert into BOOKS_GENRES (book_id, genre_id) values (BOOK_SEQ_ID.currval, GENRE_SEQ_ID.currval);
insert into GENRES (id, `genre_name`) values (GENRE_SEQ_ID.nextval, 'novel in verses');
insert into BOOKS_GENRES (book_id, genre_id) values (BOOK_SEQ_ID.currval, GENRE_SEQ_ID.currval);
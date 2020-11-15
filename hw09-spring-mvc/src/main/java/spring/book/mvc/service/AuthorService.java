package spring.book.mvc.service;

import spring.book.mvc.model.Author;

public interface AuthorService {

    void saveOrRefresh(Author author);
}

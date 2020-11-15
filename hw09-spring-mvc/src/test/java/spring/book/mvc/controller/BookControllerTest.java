package spring.book.mvc.controller;

import com.google.common.io.CharStreams;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import spring.book.mvc.dto.BookDto;
import spring.book.mvc.service.BookService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import({BookController.class, BookControllerTest.BookControllerConfiguration.class})
class BookControllerTest {

    private static final String BOOKLIST_HTML_FILE = "/controller-responses/bookList.html";
    private static final String VIEW_BOOK = "/controller-responses/viewBook.html";
    private static final String EDIT_BOOK = "/controller-responses/editBook.html";
    private static final String SAVE_NEW_BOOK = "/controller-responses/saveNewBook.html";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;


    private BookDto testBookDto() {
        return new BookDto(1, "three pigs", "David Wiesner", "Children's picture book");
    }

    @Test
    void getBooks() throws Exception {
        String html = this.getFileAsString(BOOKLIST_HTML_FILE);
        List<BookDto> bookDtos = List.of(
                testBookDto(),
                new BookDto(2, "trte", "trtr", "trtr")
        );

        Mockito.when(bookService.getBooks()).thenReturn(bookDtos);
        this.mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().string(html));
    }

    @Test
    void getBook() throws Exception {

        Mockito.when(bookService.findBookById(1)).thenReturn(testBookDto());

        this.mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(getFileAsString(VIEW_BOOK)));

    }

    @Test
    void edit() throws Exception {

        Mockito.when(bookService.findBookById(1)).thenReturn(testBookDto());

        this.mockMvc.perform(get("/books/edit?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().string(getFileAsString(EDIT_BOOK)));
    }

    @Test
    void update() throws Exception {

        Mockito.doNothing().when(bookService).updateBookById(testBookDto());

        this.mockMvc.perform(
                post("/books/edit")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("title", "newTitle")
                        .param("authors", "Three Wigs")
                        .param("genres", "new Genre"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void getSave() throws Exception {

        this.mockMvc.perform(get("/books/save"))
                .andExpect(status().isOk())
                .andExpect(content().string(getFileAsString(SAVE_NEW_BOOK)));
    }

    @Test
    void postSave() throws Exception {

        Mockito.doNothing().when(bookService).saveBook(testBookDto());

        this.mockMvc.perform(post("/books/save"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void deleteBook() throws Exception {
        Mockito.doNothing().when(bookService).deleteBookById(1L);

        this.mockMvc.perform(post("/books/delete/1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }


    private String getFileAsString(String fileName) throws IOException {
        InputStream input = this.getClass().getResourceAsStream(fileName);
        return CharStreams.toString(new InputStreamReader(input));
    }

    @TestConfiguration
    static class BookControllerConfiguration {


        @MockBean
        BookService bookService;

    }
}
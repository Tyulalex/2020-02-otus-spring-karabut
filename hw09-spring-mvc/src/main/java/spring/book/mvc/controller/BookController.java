package spring.book.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.book.mvc.dto.BookDto;
import spring.book.mvc.service.BookService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public String getBooks(Model model) {
        List<BookDto> books = bookService.getBooks();
        model.addAttribute("books", books);
        return "bookList";
    }

    @GetMapping("/")
    public String main() {
        return "redirect:/books";
    }

    @GetMapping("/books/{id}")
    public String getBook(@PathVariable("id") long id, Model model) {
        BookDto book = bookService.findBookById(id);
        model.addAttribute("book", book);
        return "viewBook";
    }

    @GetMapping("/books/edit")
    public String edit(@RequestParam("id") long id, Model model) {
        BookDto bookDto = bookService.findBookById(id);
        model.addAttribute("book", bookDto);
        return "editBook";
    }

    @PostMapping("/books/edit")
    public String update(BookDto bookDto, Model model) {
        bookService.updateBookById(bookDto);
        model.addAttribute("book", bookDto);
        return "redirect:/books";
    }

    @PostMapping("/books/save")
    public String save(BookDto bookDto) {
        bookService.saveBook(bookDto);
        return "redirect:/books";
    }

    @GetMapping("/books/save")
    public String save(Model model) {
        model.addAttribute("book", new BookDto());
        return "saveNewBook";
    }


    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        bookService.deleteBookById(id);
        return "redirect:/";
    }
}

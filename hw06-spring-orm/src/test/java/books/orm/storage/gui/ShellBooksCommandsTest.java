package books.orm.storage.gui;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ShellBooksCommandsTest {

    @Autowired
    private Shell shell;

    @Test
    void getAuthors() {
        val result = shell.evaluate(() -> "get-authors");
        assertThat(result).isEqualTo(
                "1. David Wiesner\n2. Alexander Pushkin\n3. Anna Maude (Translator)");
    }

    @Test
    void getGenres() {
        val result = shell.evaluate(() -> "get-genres");
        assertThat(result).isEqualTo(
                "1. Children's picture book\n2. Poem\n3. novel in verses");
    }

    @Test
    void getBooks() {
        val result = shell.evaluate(() -> "get-books");
        assertThat(result).isEqualTo(
                "1. \"three pigs\". Authors: 1. David Wiesner. Genre: 1. Children's picture book\n" +
                        "2. \"EUGENE ONEGIN\". Authors: 2. Alexander Pushkin, 3. Anna Maude (Translator). Genre: 2. Poem, 3. novel in verses");
    }

    @Test
    void findBookById() {
        Object result = shell.evaluate(() -> "find-book-by-id --id 2");
        String expected = "2. \"EUGENE ONEGIN\". Authors: 2. Alexander Pushkin, 3. Anna Maude (Translator). Genre: 2. Poem, 3. novel in verses";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findBookByIdNotFound() {
        Object result = shell.evaluate(() -> "find-book-by-id --id 20");
        String expected = "No such book with id=20";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void addNewBook() {
        Object resultBefore = shell.evaluate(() -> "find-book-by-id --id 3");
        assertThat(resultBefore).isEqualTo("No such book with id=3");
        String addBook = "add-book --title WarPiece --authors Leo,Henry,Aylmer,Louise --genres Novel,HistoricalNovel";
        shell.evaluate(() -> addBook);
        Object resultAfter = shell.evaluate(() -> "find-book-by-id --id 3");
        assertThat(resultAfter)
                .isEqualTo("3. \"WarPiece\". Authors: 4. Leo, 5. Henry, 6. Aylmer, 7. Louise. Genre: 4. Novel, 5. HistoricalNovel");

        String addAnotherBook = "add-book --title NewBook --authors Leo,Henry,NewAuthor --genres Novel,NewGenre";
        shell.evaluate(() -> addAnotherBook);
        Object resultAfterAddAnotherBook = shell.evaluate(() -> "find-book-by-id --id 4");
        assertThat(resultAfterAddAnotherBook)
                .isEqualTo("4. \"NewBook\". Authors: 4. Leo, 5. Henry, 8. NewAuthor. Genre: 4. Novel, 6. NewGenre");
    }

    @Test
    void deleteBookById() {
        shell.evaluate(() -> "delete-book-by-id --id 2");
        Object result = shell.evaluate(() -> "find-book-by-id --id 2");
        assertThat(result).isEqualTo("No such book with id=2");
    }

    @Test
    void updateBookById() {
        shell.evaluate(() -> "update-book-by-id --id 1 --title newTitle");
        Object result = shell.evaluate(() -> "find-book-by-id --id 1");
        assertThat(result).isEqualTo("1. \"newTitle\". Authors: 1. David Wiesner. Genre: 1. Children's picture book");
    }

    @Test
    void findBookByTitle() {
        Object result = shell.evaluate(() -> "find-books-by-title --title three");
        assertThat(result).isEqualTo("1. \"three pigs\". Authors: 1. David Wiesner. Genre: 1. Children's picture book");
    }

    @Test
    void findCommentByBookId() {
        Object result = shell.evaluate(() -> "find-comments-for-book --bookId 1");
        assertThat(result).isEqualTo("comment: 1. good book\ncomment: 2. bad book");
    }

    @Test
    void findCommentById() {
        Object result = shell.evaluate(() -> "find-comment-by-id --id 1");
        assertThat(result).isEqualTo("comment: 1. good book");
    }

    @Test
    void addCommentToBook() {
        shell.evaluate(() -> "add-comment-to-book --bookId 1 --comment newcomment");
        Object result = shell.evaluate(() -> "find-comment-by-id --id 3");
        assertThat(result).isEqualTo("comment: 3. newcomment");
    }

    @Test
    void deleteComment() {
        Object resultBeforeDelete = shell.evaluate(() -> "find-comment-by-id --id 1");
        assertThat(resultBeforeDelete).isEqualTo("comment: 1. good book");
        shell.evaluate(() -> "delete-comment --id 1");
        Object resultAfterDelete = shell.evaluate(() -> "find-comment-by-id --id 1");
        assertThat(resultAfterDelete).isEqualTo("No such comment with id=1");
    }

    @Test
    void updateComment() {
        Object resultBeforeDelete = shell.evaluate(() -> "find-comment-by-id --id 1");
        assertThat(resultBeforeDelete).isEqualTo("comment: 1. good book");
        shell.evaluate(() -> "update-comment-to-book --id 1 --comment newComment");
        Object resultAfterDelete = shell.evaluate(() -> "find-comment-by-id --id 1");
        assertThat(resultAfterDelete).isEqualTo("comment: 1. newComment");
    }

    @Test
    void deleteABookWithComments() {
        shell.evaluate(() -> "add-comment-to-book --bookId 1 --comment newcomment");
        Object result1 = shell.evaluate(() -> "find-comment-by-id --id 3");
        assertThat(result1).isEqualTo("comment: 3. newcomment");

        shell.evaluate(() -> "delete-book-by-id --id 1");

        Object result2 = shell.evaluate(() -> "find-book-by-id --id 1");
        assertThat(result2).isEqualTo("No such book with id=1");

        Object result3 = shell.evaluate(() -> "find-comment-by-id --id 3");
        assertThat(result3).isEqualTo("No such comment with id=3");
    }
}
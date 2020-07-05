package books.storage.app.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.shell.result.DefaultResultHandler;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ShellBooksCommandsTest {

    @Autowired
    private Shell shell;

    @Autowired
    private DefaultResultHandler resultHandler;

    @Test
    void getBooksCommand() {
        Object result = shell.evaluate(() -> "get-books");
        assertThat(result).isEqualTo("1. \"three pigs\"2. \"EUGENE ONEGIN\"");
    }

    @Test
    void findBookById() {
        Object result = shell.evaluate(() -> "find-book-by-id --id 2");
        String expected = "2. \"EUGENE ONEGIN\". Authors: [2. Alexander Pushkin, 3. Anna Maude (Translator)]. Genre: [2. Poem, 3. novel in verses]";
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
                .isEqualTo("3. \"WarPiece\". Authors: [4. Leo, 5. Henry, 6. Aylmer, 7. Louise]. Genre: [4. Novel, 5. HistoricalNovel]");

        String addAnotherBook = "add-book --title NewBook --authors Leo,Henry,NewAuthor --genres Novel,NewGenre";
        shell.evaluate(() -> addAnotherBook);
        Object resultAfterAddAnotherBook = shell.evaluate(() -> "find-book-by-id --id 4");
        assertThat(resultAfterAddAnotherBook)
                .isEqualTo("4. \"NewBook\". Authors: [4. Leo, 5. Henry, 8. NewAuthor]. Genre: [4. Novel, 6. NewGenre]");
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
        assertThat(result).isEqualTo("1. \"newTitle\". Authors: [1. David Wiesner]. Genre: [1. Children's picture book]");
    }

}
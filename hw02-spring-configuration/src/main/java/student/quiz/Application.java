package student.quiz;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import student.quiz.console.GUIConsole;
import student.quiz.console.GUIConsoleImpl;

@ComponentScan
@Configuration
public class Application {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);
        GUIConsole guiConsole = ctx.getBean("guiConsole", GUIConsoleImpl.class);
        guiConsole.doQuiz();
    }
}

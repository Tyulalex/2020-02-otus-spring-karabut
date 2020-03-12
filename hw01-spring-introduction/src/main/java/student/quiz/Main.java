package student.quiz;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import student.quiz.console.GUIConsole;
import student.quiz.console.GUIConsoleImpl;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        GUIConsole guiConsole = context.getBean("guiConsole", GUIConsoleImpl.class);
        guiConsole.doQuiz();
    }
}

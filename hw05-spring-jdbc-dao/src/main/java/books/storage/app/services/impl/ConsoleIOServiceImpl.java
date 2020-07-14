package books.storage.app.services.impl;

import books.storage.app.services.IOService;
import org.springframework.stereotype.Service;

import java.io.PrintStream;

@Service
public class ConsoleIOServiceImpl implements IOService {

    private final PrintStream out;

    public ConsoleIOServiceImpl() {
        out = System.out;
    }

    @Override
    public void out(String text) {
        out.println(text);
    }
}

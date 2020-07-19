package book.springdata.sql.storage.service.impl;

import book.springdata.sql.storage.service.IOService;
import org.springframework.stereotype.Service;

import java.io.PrintStream;

@Service
public class IOServiceImpl implements IOService {


    private final PrintStream out;

    public IOServiceImpl() {
        out = System.out;
    }

    @Override
    public void out(String text) {
        out.println(text);
    }
}

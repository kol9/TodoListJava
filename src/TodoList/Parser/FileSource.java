package TodoList.Parser;

import java.io.BufferedReader;
import java.io.IOException;


public class FileSource implements JsonSource, AutoCloseable {
    private final BufferedReader reader;
    private char last = '\0';

    public FileSource(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public boolean hasNext() {
        try {
            int ch = reader.read();
            if (ch != -1) {
                last = (char) ch;
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    @Override
    public char next() {
        return last;
    }

    @Override
    public JsonException error(String message) {
        return new JsonException(message);
    }

    @Override
    public void close() throws Exception {
        reader.close();
    }
}

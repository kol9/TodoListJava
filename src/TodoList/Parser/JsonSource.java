package TodoList.Parser;


public interface JsonSource {
    boolean hasNext();

    char next();

    JsonException error(final String message);
}

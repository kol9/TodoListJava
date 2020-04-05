package TodoList.Parser;

import TodoList.TodoList;
import TodoList.Note;

import java.util.ArrayList;
import java.util.List;

public class TodoJson {
    public static TodoList parse(JsonSource source) {
        return new TodoJson.JsonParser(source).parseJson();
    }

    private static class JsonParser extends BaseParser {
        protected JsonParser(JsonSource source) {
            super(source);
            nextChar();
        }

        private TodoList parseJson() {
            final TodoList result = parseElement();
            if (test('\0')) {
                return result;
            }
            throw error("End of JSON expected");
        }

        private TodoList parseElement() {
            skipWhitespace();
            final TodoList result = parseValue();
            skipWhitespace();
            return result;
        }

        private TodoList parseValue() {
            expect('{');
            return parseList();
        }

        private TodoList parseList() {
            skipWhitespace();
            if (test('"')) {
                expect("items\"");
                skipWhitespace();
                expect(':');
                skipWhitespace();
                expect('[');
                List<Note> notes = parseNoteList();
                return new TodoList(notes);
            } else if (test('}')) {
                return new TodoList(new ArrayList<>());
            } else {
                throw new JsonException("Error parsing");
            }
        }

        private List<Note> parseNoteList() {
            skipWhitespace();
            if (test(']')) {
                skipWhitespace();
                expect('}');
                return new ArrayList<>();
            }
            List<Note> array = new ArrayList<>();
            array.add(parseNote());
            while (test(',')) {
                array.add(parseNote());
            }
            skipWhitespace();
            expect(']');
            skipWhitespace();
            expect('}');
            skipWhitespace();
            return array;
        }

        private Note parseNote() {
            String text;
            boolean done = false;
            skipWhitespace();
            expect('{');
            skipWhitespace();
            expect("\"text\"");
            skipWhitespace();
            expect(':');
            skipWhitespace();
            expect("\"");
            text = parseString();
            skipWhitespace();
            expect(',');
            skipWhitespace();
            expect("\"done\"");
            skipWhitespace();
            expect(":");
            skipWhitespace();
            if (test('t')) {
                expect("rue");
                done = true;
            } else if (test('f')) {
                expect("alse");
                done = false;
            }
            skipWhitespace();
            expect('}');
            return new Note(text, done);
        }


        private String parseString() {
            StringBuilder sb = new StringBuilder();
            while (!test('"')) {
                if (test('\\')) {
                    if (!escaped(sb, '\"', '\"') && !escaped(sb, '\\', '\\') &&
                            !escaped(sb, '/', '/') && !escaped(sb, '\b', 'b') &&
                            !escaped(sb, '\f', 'f') && !escaped(sb, '\n', 'n') &&
                            !escaped(sb, '\r', 'r')) {
                        escaped(sb, '\t', 't');
                    }
                } else {
                    sb.append(ch);
                    nextChar();
                }
            }
            return sb.toString();
        }

        private boolean escaped(final StringBuilder sb, final char character, final char expected) {
            final boolean consumed = test(expected);
            if (consumed) {
                sb.append(character);
            }
            return consumed;
        }


        private void skipWhitespace() {
            while (test(' ') || test('\r') || test('\n') || test('\t')) {
                // skip
            }
        }
    }
}

package TodoList;

public class Note implements Encodable, Printable {
    public Note(String text, boolean done) {
        this.text = text;
        this.done = done;
    }

    String text;
    boolean done;

    public void setState(boolean state) {
        done = state;
    }

    @Override
    public String encode() {
        return "    {\n      \"text\": \"" +
                text +
                "\",\n      \"done\": " +
                done +
                "\n    }";
    }

    @Override
    public void print() {
        System.out.println(text + "    | " + (done ? "DONE |" : "TODO |"));
    }
}

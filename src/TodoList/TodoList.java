package TodoList;

import java.util.List;

public class TodoList implements Encodable, Printable {
    public TodoList(List<Note> items) {
        this.items = items;
    }

    List<Note> items;

    public void addNote(Note note) {
        items.add(note);
        System.out.println("Note has been successfully added");
    }

    public void removeNote(int ind) {
        try {
            items.remove(ind - 1);
            System.out.println("Note has been successfully removed");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please, input correct index of note");
        }
    }

    public void setState(int ind, boolean state) {
        try {
            items.get(ind - 1).setState(state);
            System.out.println("Note has been successfully changed");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please, input correct index of note");
        }
    }


    public void filter() {
        System.out.println("-----------NOT_DONE_TASKS-----------");
        for (int i = 0; i < items.size(); ++i) {
            if (!items.get(i).done) {
                System.out.print((i + 1) + ") ");
                items.get(i).print();
            }
        }
    }

    @Override
    public String encode() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n  \"items\": [\n");
        for (int i = 0; i < items.size() - 1; ++i) {
            stringBuilder.append(items.get(i).encode()).append(",\n");
        }
        stringBuilder.append(items.get(items.size() - 1).encode());
        stringBuilder.append("\n  ]\n}");
        return stringBuilder.toString();
    }

    @Override
    public void print() {
        System.out.println("-----------TODO_LIST-----------");
        for (int i = 0; i < items.size(); ++i) {
            System.out.print((i + 1) + ") ");
            items.get(i).print();
        }
    }
}

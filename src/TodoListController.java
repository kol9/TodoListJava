import TodoList.Parser.FileSource;
import TodoList.Parser.JsonException;
import TodoList.Parser.TodoJson;
import TodoList.TodoList;
import TodoList.Note;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class TodoListController {
    private TodoList items;

    public TodoListController() {
        File jsonFile = new File("todo-list.json");
        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile, StandardCharsets.UTF_8))) {
            try {
                items = TodoJson.parse(new FileSource(reader));
            } catch (JsonException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveContext() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("todo-list.json", StandardCharsets.UTF_8))) {
            writer.write(items.encode());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String commandList() {
        return "Command list:\n" +
                "write 'add' for create new note.\n" +
                "write 'remove' for delete an existing note.\n" +
                "write 'read' for see all existing notes.\n" +
                "write 'mark' for change note's type.\n" +
                "write 'filter' for see all not done notes.\n" +
                "write 'close' for save all changes.\n";
    }

    void runLoop() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean flag = false;
            System.out.println(commandList());
            while (!flag) {
                saveContext();
                String nxt = scanner.next();
                switch (nxt) {
                    case "add":
                        System.out.print("Input note: ");
                        scanner.nextLine();
                        nxt = scanner.nextLine();
                        items.addNote(new Note(nxt, false));
                        continue;
                    case "remove":
                        System.out.print("Input index: ");
                        nxt = scanner.next();
                        try {
                            items.removeNote(Integer.parseInt(nxt));
                        } catch (NumberFormatException e) {
                            System.out.println("Wrong id");
                            continue;
                        }
                        continue;
                    case "read":
                        items.print();
                        continue;
                    case "mark":
                        System.out.print("Input index: ");
                        nxt = scanner.next();
                        try {
                            int ind = Integer.parseInt(nxt);

                            System.out.println("Input 'Done' or 'Todo' for change status of this note");
                            nxt = scanner.next();
                            switch (nxt) {
                                case "Done":
                                    items.setState(ind, true);
                                    break;
                                case "Todo":
                                    items.setState(ind, false);
                                    break;
                                default:
                                    System.out.println("Wrong status");
                                    continue;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Wrong id");
                            continue;
                        }
                        break;
                    case "filter":
                        items.filter();
                        break;
                    case "close":
                        flag = true;
                        break;
                    default:
                        System.out.println("Wrong command");
                        System.out.println(commandList());
                        break;
                }
            }
        }
    }
}

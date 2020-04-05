import java.io.*;

public class Main {
    public static void main(String[] args) {
        TodoListController controller;
        controller = new TodoListController();
        try {
            controller.runLoop();
        } catch (IOException e) {
            controller.saveContext();
            e.printStackTrace();
        } finally {
            controller.saveContext();
        }

    }
}


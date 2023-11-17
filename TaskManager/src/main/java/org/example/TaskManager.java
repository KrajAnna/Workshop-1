package org.example;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    private static final String[] OPTIONS = new String[]{"add", "remove", "list", "exit"};
    private static final String FILE = "tasks.csv";
    static Scanner sc = new Scanner(System.in);

    static String[][] tasks;

    public static void main(String[] args) throws IOException {

        tasks = readFile(FILE);
        displayOptions(OPTIONS);
        while (sc.hasNextLine()) {
            String input = sc.nextLine();

            switch (input) {
                case "add":
                    addTask();
                    break;
                case "remove":
                    removeTask(tasks, numberVerification());
                    break;
                case "list":
                    printTasks(tasks);
                    break;
                case "exit":
                    saveTask(FILE, tasks);
                    System.out.println(ConsoleColors.RED + "Bye, bye");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please select a correct option.");
            }
            displayOptions(OPTIONS);
        }

    }

    private static String[][] readFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            //System.out.println("File does not exist");
            System.exit(0);
        }
        List<String> content = Files.readAllLines(path);
        String[][] tab = new String[content.size()][content.get(0).split(",").length];
        for (int i = 0; i < content.size(); i++) {
            String[] line = content.get(i).split(",");
            for (int j = 0; j < line.length; j++) {
                tab[i][j] = line[j];
            }
        }
        return tab;
    }


    private static void displayOptions(String[] options) {
        System.out.print(ConsoleColors.BLUE);
        System.out.println("Please select an option:");
        System.out.print(ConsoleColors.RESET);
        for (String opt : options) {
            System.out.println(opt);
        }
    }

    private static void addTask() {
        System.out.println("Please add task description:");
        String description = sc.nextLine();
        System.out.println("Please add task dua date:");
        String duaDate = sc.nextLine();
        System.out.println("Is your task important? true/false");
        String importance = sc.nextLine();

        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = duaDate;
        tasks[tasks.length - 1][2] = importance;

    }

    private static void printTasks(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tasks[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void removeTask(String[][] tab, int index) {
        try {
            if (index < tab.length) {
                tasks = ArrayUtils.remove(tab, index);
                System.out.println("Value was successfully deleted");
            }
            else {
                System.out.println("element does not exist in the tab");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Element does not exist in the tab");
        }

    }

    public static int numberVerification() {
        System.out.println("Please select number to remove.");
        String number = sc.nextLine();

        while (!isNumberGreaterEqualZero(number)) {
            System.out.println("Incorrect argument. Please give number > or equal 0");
            number = sc.nextLine();
        }
        return Integer.parseInt(number);
    }

    public static boolean isNumberGreaterEqualZero(String input) {
        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }

    private static void saveTask(String fileName, String[][] tab) {
        Path path = Paths.get(fileName);

        String[] lines = new String[tasks.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
            //System.out.println(lines[i]);
        }
        try {
            Files.write(path, List.of(lines));
        } catch (IOException e) {
            System.out.println("There was an error during saving: " + e.getMessage());
        }
    }

}

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

public class Main {
    static FileService fs = new FileService("./cats.json");

    public static void main(String[] args) {
        run();
    }

    public static void run() {
//        var cats = Cat.makeCats(10);  // create cats without json
        var cats = fs.readFile();
        printCats(sortCatsByAverageLevel(cats));
        getAction(cats);
    }

    public static void printCats(List<Cat> cats) {
        System.out.println("+-----+-----------+---------+----------+------------+---------+---------------+");
        System.out.println("|  #  | Name      | Age     | Health   | Mood       | Satiety | Average Level |");
        System.out.println("+-----+-----------+---------+----------+------------+---------+---------------+");
        for (int i = 0; i < cats.size(); i++) {
            System.out.printf("|  %-2s | %-9s |    %-4s |    %-5s |     %-6s |    %-4s |       %-7s |%n",
                    i + 1, cats.get(i).getName(), cats.get(i).getAge(), cats.get(i).getHealthLevel(),
                    cats.get(i).getMoodLevel(), cats.get(i).getSatietyLevel(), cats.get(i).getAverage());
        }
        System.out.println("+-----+-----------+---------+----------+------------+---------+---------------+\n");
    }

    public static List<Cat> sortCatsByName(List<Cat> cats) {
        return cats.stream()
                .sorted(comparing(Cat::getName))
                .toList();
    }

    public static List<Cat> sortCatsByAge(List<Cat> cats) {
        return cats.stream()
                .sorted(comparingInt(Cat::getAge))
                .toList();
    }
    public static List<Cat> sortCatsByHealthLevel(List<Cat> cats) {
        return cats.stream()
                .sorted(comparingInt(Cat::getHealthLevel))
                .toList();
    }
    public static List<Cat> sortCatsByMoodLevel(List<Cat> cats) {
        return cats.stream()
                .sorted(comparingInt(Cat::getMoodLevel))
                .toList();
    }
    public static List<Cat> sortCatsBySatietyLevel(List<Cat> cats) {
        return cats.stream()
                .sorted(comparingInt(Cat::getSatietyLevel))
                .toList();
    }

    public static List<Cat> sortCatsByAverageLevel(List<Cat> cats) {
        return cats.stream()
                .sorted(comparing(Cat::getAverage, reverseOrder()))
                .toList();
    }

    public static void addNewCat(List<Cat> cats) {
        Cat cat = new Cat(inputName(), inputAge());
        cat.setSatietyLevel(new Random().nextInt(20, 81));
        cat.setMoodLevel(new Random().nextInt(20, 81));
        cat.setHealthLevel(new Random().nextInt(20, 81));
//        cat.setAverage((cat.getSatietyLevel() + cat.getMoodLevel() + cat.getHealthLevel()) / 3);
        cat.refreshAverage();
        cat.setActionToday(false);
        cats.add(cat);
    }

    public static int inputAge() {
        String input;
        while (true) {
            System.out.print("Enter new cat's age(1..18): ");
            try {
                input = new Scanner(System.in).nextLine();
                checkInputAge(input);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(input);
        return Integer.parseInt(input);
    }

    public static void checkInputAge(String str) throws Exception {
        int input = Integer.parseInt(str);
        if (input < 1 || input > 18) throw new Exception("Incorrect age input");
    }

    public static String inputName() {
        String input;
        while (true) {
            System.out.print("Enter new cat's name: ");
            Scanner scan = new Scanner(System.in);
            try {
                input = checkInputName(scan, "[a-zA-Z]+");
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(input);
        return input;
    }

    public static String checkInputName(Scanner scan, String pattern) throws Exception {
        if (!scan.hasNext(pattern)) throw new Exception("Incorrect name input");
        else return scan.next();
    }

    public static int chooseAction() {
        String input;
        printActions();
        while (true) {
            System.out.print("Enter number of action: ");
            try {
                input = new Scanner(System.in).nextLine();
                return checkAction(input);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void printActions() {
        System.out.println("""
                Choose Action:
                1  - Feed the cat
                2  - Play with cat
                3  - Cure the cat
                4  - Get new cat
                5  - Next day
                6  - Choose sort
                7  - Exit
                """);
    }

    public static int checkAction(String str) throws Exception {
        int input = Integer.parseInt(str);
        if (input < 1 || input > 6)
            throw new Exception("Incorrect action");
        return input;
    }

    public static void getAction(List<Cat> cats) {
        while (true) {
            fs.writeFile(cats);
            switch (chooseAction()) {
                case 1:
                    chooseCat(cats).feed();
                    printCats(sortCatsByAverageLevel(cats));
                    break;
                case 2:
                    chooseCat(cats).play();
                    printCats(sortCatsByAverageLevel(cats));
                    break;
                case 3:
                    chooseCat(cats).cure();
                    printCats(sortCatsByAverageLevel(cats));
                    break;
                case 4:
                    addNewCat(cats);
                    printCats(sortCatsByAverageLevel(cats));
                    break;
                case 5:
                    getNextDay(cats);
                    printCats(sortCatsByAverageLevel(cats));
                    break;
                case 6:
                    getSort(cats);
                case 7:
                    return;
            }
        }
    }

    public static Cat chooseCat(List<Cat> cats) {
        String input;
        printCatChoice(cats);
        while (true) {
            System.out.printf("Enter cat number(1..%s): ", cats.size());
            try {
                input = new Scanner(System.in).nextLine();
                int choice = checkChoice(input, cats);
                return cats.get(choice - 1);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void printCatChoice(List<Cat> cats) {
        System.out.println("Choose cat: ");
        for (int i = 0; i < cats.size(); i++) {
            System.out.printf("%s - %s%n", i + 1, cats.get(i).getName());
        }
    }

    public static int checkChoice(String str, List<Cat> cats) throws Exception {
        int input = Integer.parseInt(str);
        if (input < 1 || input > cats.size())
            throw new Exception("Incorrect choice of cat");
        return input;
    }

    public static List<Cat> getNextDay(List<Cat> cats) {
        cats.forEach(Cat::changeParams);
        cats.forEach(Cat::resetActionToday);
        return cats;
    }

    public static int chooseSort() {
        String input;
        printSorts();
        while (true) {
            System.out.print("Enter number of sort(1..6): ");
            try {
                input = new Scanner(System.in).nextLine();
                return checkSort(input);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void printSorts() {
        System.out.println("""
                Choose sort:
                1  - by name
                2  - by age
                3  - by health
                4  - by mood
                5  - by satiety
                6  - by average
                """);
    }

    public static int checkSort(String str) throws Exception {
        int input = Integer.parseInt(str);
        if (input < 1 || input > 6)
            throw new Exception("Incorrect number of sort");
        return input;
    }

    public static void getSort(List<Cat> cats) {
        while (true) {
            switch (chooseSort()) {
                case 1:
                    sortCatsByName(cats);
                    printCats(sortCatsByName(cats));
                    break;
                case 2:
                    sortCatsByAge(cats);
                    printCats(sortCatsByAge(cats));
                    break;
                case 3:
                    sortCatsByHealthLevel(cats);
                    printCats(sortCatsByHealthLevel(cats));
                    break;
                case 4:
                    sortCatsByMoodLevel(cats);
                    printCats(sortCatsByMoodLevel(cats));
                    break;
                case 5:
                    sortCatsBySatietyLevel(cats);
                    printCats(sortCatsBySatietyLevel(cats));
                    break;
                case 6:
                    sortCatsByAverageLevel(cats);
                    printCats(sortCatsByAverageLevel(cats));
                    break;
            }
        }
    }
}
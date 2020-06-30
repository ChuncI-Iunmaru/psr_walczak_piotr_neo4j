package psr.lab7;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ConsoleUtils {
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    static char getMenuOption() {
        System.out.println();
        List<String> menuOptions = new ArrayList<>(Arrays.asList(
                "[d]odaj książkę",
                "[e]dytuj książkę",
                "[u]suń książkę",
                "pokaz [k]sięgozbiór",
                "za[r]ejstruj kartę",
                "w[y]rejestruj kartę",
                "wszyscy [c]zytelnicy",
                "czytelnik po [n]umerze karty",
                "[w]ypożycz książkę",
                "[o]ddaj książkę",
                "[p]obierz zapytaniem",
                "[s]tatystyki wypożyczeń",
                "[z]akoncz"));
        int i = 1;
        for (String s : menuOptions) {
            System.out.println(i + ". " + s);
            i++;
        }
        while (true) {
            try {
                System.out.print("Podaj operację: ");
                return scanner.nextLine().toLowerCase().charAt(0);
            } catch (StringIndexOutOfBoundsException e) {
                scanner.nextLine();
                System.out.println("Podano nieprawidłową operację.");
            }
        }
    }

    static String getFormattedDate(String setValue) {
        System.out.println("Podaj date wydania w formacie DD-MM-YYYY");
        if (!setValue.isEmpty())
            System.out.println("Obecna wartość: " + setValue + ". Pozostaw puste by nie zmieniać.");
        while (true) {
            try {
                String line = scanner.nextLine();
                if (!setValue.isEmpty() && line.isEmpty()) return setValue;
                LocalDate date = LocalDate.parse(line, format);
                return format.format(date);
            } catch (DateTimeParseException e) {
                System.out.println("Podaj prawidłową datę!");
            }
        }
    }

    static String getFormattedDate() {
        return getFormattedDate("");
    }

    static String getText(int minLength) {
        String tmp = "";
        do {
            tmp = scanner.nextLine();
            if (tmp.length() < minLength) System.out.println("Podaj minimum " + minLength + " znakow!");
        } while (tmp.length() < minLength);
        return tmp;
    }

    static long getId() {
        long num = -1;
        while (num < 0) {
            try {
                num = scanner.nextLong();
                scanner.nextLine();
                if (num < 0) System.out.println("Podaj prawidłową wartość => 0");
            } catch (InputMismatchException e) {
                scanner.next();
                System.out.println("Podaj prawidłową wartość!");
            }
        }
        return num;
    }
}

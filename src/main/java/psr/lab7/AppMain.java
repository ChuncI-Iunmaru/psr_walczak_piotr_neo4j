package psr.lab7;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import psr.lab7.entity.Book;
import psr.lab7.entity.Reader;
import psr.lab7.service.BookService;
import psr.lab7.service.ReaderService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AppMain {

    private static void addBook(BookService service) {
        System.out.println("Podaj tytuł książki:");
        String title = ConsoleUtils.getText(1);
        String date = ConsoleUtils.getFormattedDate();
        System.out.println("Podaj wydawnictwo:");
        String publisher = ConsoleUtils.getText(1);
        service.createOrUpdate(new Book(title, date, publisher));
    }

    private static Book findBook(BookService service){
        System.out.println("Podaj id książki: ");
        Long id = ConsoleUtils.getId();
        Book result = service.getById(id);
        if (result==null) {
            System.out.println("Nie znaleziono książki o takim id!");
        }
        return result;
    }

    private static void deleteBook(BookService service) {
        Book result = findBook(service);
        if  (result != null) service.delete(result.getId());
    }

    private static void updateBook(BookService service) {
        Book toUpdate = findBook(service);
        if (toUpdate != null) {
            System.out.println("Podaj nowy tytuł książki. Obecna wartość: " + toUpdate.getTitle()+". Pozostaw puste by nie zmieniać.");
            String title = ConsoleUtils.getText(0);
            toUpdate.setTitle(title.isEmpty() ? toUpdate.getTitle() : title);
            String date = ConsoleUtils.getFormattedDate(toUpdate.getReleaseDate());
            toUpdate.setReleaseDate(date);
            System.out.println("Podaj nowe wydawnictwo. Obecna wartość: " + toUpdate.getPublisher()+". Pozostaw puste by nie zmieniać.");
            String publisher = ConsoleUtils.getText(0);
            toUpdate.setPublisher(publisher.isEmpty() ? toUpdate.getPublisher() : publisher);
            service.createOrUpdate(toUpdate);
        }
    }

    private static void printAllBooks(BookService service) {
        for (Book b:service.getAll()) {
            System.out.println(b);
        }
    }

    private static Reader findReader(ReaderService service){
        System.out.println("Podaj numer karty czytelnika: ");
        Long id = ConsoleUtils.getId();
        Reader result = service.getById(id);
        if (result==null) {
            System.out.println("Nie znaleziono karty czytelnika o takim numerze!");
        }
        return result;
    }

    private static void addReader(ReaderService service) {
        System.out.println("Podaj imię:");
        String name = ConsoleUtils.getText(1);
        System.out.println("Podaj nazwisko:");
        String surname = ConsoleUtils.getText(1);
        service.createOrUpdate(new Reader(name, surname));
    }

    private static void deleteReader(ReaderService service) {
        Reader result = findReader(service);
        if (result != null) service.delete(result.getId());
    }

    private static void printAllReaders(ReaderService service) {
        for (Reader r: service.getAll()) {
            System.out.println(r);
            System.out.println("Wypożyczone książki: ");
            for (Book b: r.getBooks()) {
                System.out.println("\t" + b);
            }
        }
    }

    private static void borrowBook(ReaderService readerService, BookService bookService) {
        Reader reader = findReader(readerService);
        if (reader == null) return;
        System.out.println(String.format("%s %s wybiera książkę: ", reader.getName(), reader.getSurname()));
        Book book = findBook(bookService);
        if (book == null) return;
        System.out.println(String.format("%s %s wypożycza książkę pt. '%s'", reader.getName(), reader.getSurname(), book.getTitle()));
        if (readerService.borrowBook(reader, book)){
            System.out.println("Udało się wypożyczyć egzemplarz szukanej książki.");
        } else System.out.println("Wypożyczenie nie powiodło się.");
    }

    private static void findWithQuery(ReaderService readerService) {
        // Full query: MATCH (a:Reader)-[:BORROWED]-(:Book) WHERE a.name=$imie and a.surname=$nazwisko RETURN a
        StringBuilder query = new StringBuilder("MATCH (a:Reader)");
        HashMap<String, Object> params = new HashMap<>();
        System.out.println("Czy uwzględniać tylko aktywne karty? [y\\n]");
        if (ConsoleUtils.getText(1).toLowerCase().charAt(0) == 'y') query.append("-[:BORROWED]-(:Book)");
        System.out.println("Podaj imię. '*' by wyszukać wszystkie.");
        String name = ConsoleUtils.getText(1);
        if (name.compareTo("*") != 0){
            query.append(" WHERE a.name=$imie");
            params.put("imie", name);
        }
        System.out.println("Podaj nazwisko. '*' by wyszukać wszystkie.");
        String surname = ConsoleUtils.getText(1);
        if (surname.compareTo("*") !=0) {
            if (name.compareTo("*") != 0) query.append(" and a.surname=$nazwisko");
            else query.append(" WHERE a.surname=$nazwisko");
            params.put("nazwisko", surname);
        }
        query.append(" RETURN a");
        for (Map<String, Object> map: readerService.getByQuery(query.toString(), params)) {
            System.out.println(map.get("a"));
        }
    }

    private static void returnBook(ReaderService readerService, BookService bookService) {
        Reader reader = findReader(readerService);
        if (reader == null) return;
        System.out.println(String.format("%s %s wybiera książkę do zwrotu: ", reader.getName(), reader.getSurname()));
        Book book = findBook(bookService);
        if (book == null) return;
        System.out.println(String.format("%s %s zwraca książkę pt. '%s'", reader.getName(), reader.getSurname(), book.getTitle()));
        if (readerService.returnBook(reader, book)){
            System.out.println("Udało się zwrócić wybrany egzemplarz.");
        } else System.out.println("Zwrot nie powiódł się.");
    }

    private static void getStats(BookService bookService){
        System.out.println("Przetwarzanie danych - ranking książek według popularności u czytelników");
        String query = "MATCH (b:Book)-[:BORROWED]-(r:Reader) WITH b, COUNT(r) as readers RETURN b, readers";
        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Integer> booksToReaders = new HashMap<>();
        for (Map<String, Object> map: bookService.getByQuery(query, params)) {
            booksToReaders.put(map.get("b").toString(), Integer.parseInt(map.get("readers").toString()));
        }
        Map<String, Integer> sortedMap = booksToReaders.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        System.out.println(String.format("%10s | %-100s", "Czytelnicy", "Książka"));
        System.out.println(String.format("%113s", "-").replace(" ", "-"));
        for (String s: sortedMap.keySet()) {
            System.out.println(String.format("%10s | %-100s", sortedMap.get(s), s));
        }
    }

    private static void findAndPrintOne(ReaderService readerService){
        Reader reader = findReader(readerService);
        if (reader == null) return;
        System.out.println(reader);
        System.out.println("Wypożyczone książki: ");
        for (Book b: reader.getBooks()) {
            System.out.println("\t" + b);
        }
    }

    public static void main(String[] args) {
        Configuration configuration = new Configuration.Builder().uri("bolt://localhost:7687").credentials("neo4j", "admin").build();
        SessionFactory sessionFactory = new SessionFactory(configuration, "psr.lab7");

        Session session = sessionFactory.openSession();

        //session.purgeDatabase();
        BookService bookService = new BookService(session);
        ReaderService readerService = new ReaderService(session);

        System.out.println("Aplikacja na PSR lab 7 - Neo4j");
        System.out.println("Piotr Walczak gr. 1ID22B");
        while (true){
            switch (ConsoleUtils.getMenuOption()) {
                case 'd':
                    addBook(bookService);
                    break;
                case 'e':
                    updateBook(bookService);
                    break;
                case 'u':
                    deleteBook(bookService);
                    break;
                case 'k':
                    printAllBooks(bookService);
                    break;
                case 'r':
                    addReader(readerService);
                    break;
                case 'y':
                    deleteReader(readerService);
                    break;
                case 'c':
                    printAllReaders(readerService);
                    break;
                case 'n':
                    findAndPrintOne(readerService);
                    break;
                case 'w':
                    borrowBook(readerService, bookService);
                    break;
                case 'o':
                    returnBook(readerService, bookService);
                    break;
                case 'p':
                    findWithQuery(readerService);
                    break;
                case 's':
                    getStats(bookService);
                    break;
                case 'z':
                    sessionFactory.close();
                    return;
                default:
                    System.out.println("Podano nieznaną operację. Spróbuj ponownie.");
            }
        }
    }
}

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

class Book {
    int id;
    String title;
    String author;
    String publisher;
    int publishYear;
    boolean isBorrowed;
    String borrowerName;
    LocalDate borrowDate;
    int borrowPeriod;

    Book(int id, String title, String author, String publisher, int publishYear) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishYear = publishYear;
        this.isBorrowed = false;
        this.borrowerName = "";
        this.borrowDate = null;
        this.borrowPeriod = 0;
    }
}

public class Library {
    static ArrayList<Book> books = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static int bookIdCounter = 1;
    static final int FINE_PER_DAY = 2;

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\nWelcome to Library Management Application");
            System.out.println("1. Add Book");
            System.out.println("2. Show All Books");
            System.out.println("3. Show Available Books");
            System.out.println("4. Borrowed Book");
            System.out.println("5. Return Book");
            System.out.println("6. Search Books");
            System.out.println("7. Exit");
            System.out.print("\nEnter Your Choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    showAllBooks();
                    break;
                case 3:
                    showAvailableBooks();
                    break;
                case 4:
                    borrowBook();
                    break;
                case 5:
                    returnBook();
                    break;
                case 6:
                    searchBooks();
                    break;
                case 7:
                    System.out.println("Exiting the application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 7);
    }

    static void addBook() {
        System.out.print("Enter the title of the book: ");
        String title = scanner.nextLine();
        System.out.print("Enter the author of the book: ");
        String author = scanner.nextLine();
        System.out.print("Enter the publisher of the book: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter the publish year of the book: ");
        int publishYear = scanner.nextInt();

        books.add(new Book(bookIdCounter++, title, author, publisher, publishYear));
        System.out.println("Book added successfully!");
    }

    static void showAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }

        System.out.println("\nList of All Books:\n");
        System.out.printf("%-5s %-20s %-20s %-15s %-15s %-10s %-20s %-15s %-10s%n",
                "ID", "TITLE", "AUTHOR", "PUBLISHER", "PUBLISH YEAR", "STATUS", "BORROWER", "BORROW DATE", "PERIOD");
        for (Book book : books) {
            System.out.printf("%-5d %-20s %-20s %-15s %-15d %-10s %-20s %-15s %-10s%n",
                    book.id, book.title, book.author, book.publisher, book.publishYear,
                    book.isBorrowed ? "Borrowed" : "Available",
                    book.borrowerName.isEmpty() ? "-" : book.borrowerName,
                    book.borrowDate == null ? "-" : book.borrowDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    book.borrowPeriod > 0 ? book.borrowPeriod + " days" : "-");
        }
    }

    static void showAvailableBooks() {
        boolean available = false;

        System.out.println("\nList of Available Books:");
        System.out.printf("%-5s %-20s %-20s %-15s %-15s%n", "ID", "TITLE", "AUTHOR", "PUBLISHER", "PUBLISH YEAR");
        for (Book book : books) {
            if (!book.isBorrowed) {
                System.out.printf("%-5d %-20s %-20s %-15s %-15d%n",
                        book.id, book.title, book.author, book.publisher, book.publishYear);
                available = true;
            }
        }

        if (!available) {
            System.out.println("No books are currently available.");
        }
    }

    static void borrowBook() {
        System.out.print("Enter the ID of the book to borrow: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        for (Book book : books) {
            if (book.id == id && !book.isBorrowed) {
                System.out.print("Enter your name: ");
                String name = scanner.nextLine();
                System.out.print("Enter borrow date (dd/MM/yyyy): ");
                String borrowDateString = scanner.nextLine();
                System.out.print("Enter period (in days): ");
                int period = scanner.nextInt();
                scanner.nextLine();

                LocalDate borrowDate = LocalDate.parse(borrowDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                book.isBorrowed = true;
                book.borrowerName = name;
                book.borrowDate = borrowDate;
                book.borrowPeriod = period;
                System.out.println("The book is  borrowed : " + book.title);
                return;
            }
        }
        System.out.println("Sorry, the book is either not available or already borrowed.");
    }

    static void returnBook() {
        System.out.print("Enter the ID of the book to return: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        for (Book book : books) {
            if (book.id == id && book.isBorrowed) {
                System.out.print("Enter return date (dd/MM/yyyy): ");
                String returnDateString = scanner.nextLine();

                LocalDate returnDate = LocalDate.parse(returnDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                long daysBetween = ChronoUnit.DAYS.between(book.borrowDate, returnDate);

                if (daysBetween > book.borrowPeriod) {
                    long overdueDays = daysBetween - book.borrowPeriod;
                    int fine = (int) (overdueDays * FINE_PER_DAY);
                    System.out.println("Late return! Fine: ₹" + fine);
                } else {
                    System.out.println("Returned on time. No fine.");
                }

                book.isBorrowed = false;
                book.borrowerName = "";
                book.borrowDate = null;
                book.borrowPeriod = 0;
                System.out.println("Book is returned : " + book.title);
                return;
            }
        }
        System.out.println("The book is either not borrowed or does not exist.");
    }

    static void searchBooks() {
        System.out.print("Enter keyword to search: ");
        String keyword = scanner.nextLine().toLowerCase();

        System.out.println("Search Results:");
        System.out.printf("%-5s %-20s %-20s %-15s %-15s %-10s %-20s %-15s %-10s%n",
                "ID", "TITLE", "AUTHOR", "PUBLISHER", "PUBLISH YEAR", "STATUS", "BORROWER", "BORROW DATE", "PERIOD");
        for (Book book : books) {
            if (book.title.toLowerCase().contains(keyword) || 
                book.author.toLowerCase().contains(keyword) || 
                book.publisher.toLowerCase().contains(keyword) || 
                Integer.toString(book.publishYear).contains(keyword)) {
                System.out.printf("%-5d %-20s %-20s %-15s %-15d %-10s %-20s %-15s %-10s%n",
                        book.id, book.title, book.author, book.publisher, book.publishYear,
                        book.isBorrowed ? "Borrowed" : "Available",
                        book.borrowerName.isEmpty() ? "-" : book.borrowerName,
                        book.borrowDate == null ? "-" : book.borrowDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        book.borrowPeriod > 0 ? book.borrowPeriod + " days" : "-");
            }
        }
    }
}

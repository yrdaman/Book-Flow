// Project: BookFlow
import dao.UserDao;
import dao.BookDao;
import dao.UserBookDao;
import dao.BorrowDao;
import dao.PaymentDao;
import dao.ReviewDao;
import dao.NotificationDao;
import models.*;

import java.io.Console;
import java.util.Scanner;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserDao userDao = new UserDao();
        BookDao bookDao = new BookDao();
        UserBookDao userBookDao = new UserBookDao();
        BorrowDao borrowDao = new BorrowDao();
        PaymentDao paymentDao = new PaymentDao();
        ReviewDao reviewDao = new ReviewDao();
        NotificationDao notificationDao = new NotificationDao();

        boolean running = true;

        while (running) {
            System.out.println(
                    """
                            ▀█████████▄   ▄██████▄   ▄██████▄     ▄█   ▄█▄\s
                              ███    ███ ███    ███ ███    ███   ███ ▄███▀\s
                              ███    ███ ███    ███ ███    ███   ███▐██▀  \s
                             ▄███▄▄▄██▀  ███    ███ ███    ███  ▄█████▀   \s
                            ▀▀███▀▀▀██▄  ███    ███ ███    ███ ▀▀█████▄   \s
                              ███    ██▄ ███    ███ ███    ███   ███▐██▄  \s
                              ███    ███ ███    ███ ███    ███   ███ ▀███▄\s
                            ▄█████████▀   ▀██████▀   ▀██████▀    ███   ▀█▀\s
                                                                 ▀        \s
                               ▄████████  ▄█        ▄██████▄   ▄█     █▄  \s
                              ███    ███ ███       ███    ███ ███     ███ \s
                              ███    █▀  ███       ███    ███ ███     ███ \s
                             ▄███▄▄▄     ███       ███    ███ ███     ███ \s
                            ▀▀███▀▀▀     ███       ███    ███ ███     ███ \s
                              ███        ███       ███    ███ ███     ███ \s
                              ███        ███▌    ▄ ███    ███ ███ ▄█▄ ███ \s
                              ██▀        █████▄▄██  ▀██████▀   ▀███▀███▀  \s
                              ▀          ▀                               \s""");
            System.out.println("Welcome to BookFlow! 🌊🌊🌊");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();
                    System.out.print("Enter location: ");
                    String location = sc.nextLine();

                    String hashedPassword = hashPassword(password);
                    User newUser = new User(name, email, hashedPassword, location);
                    userDao.registerUser(newUser);
                    break;

                case 2:
                    System.out.print("Enter email: ");
                    String loginEmail = sc.nextLine();
                    Console console = System.console();
                    String loginPassword;

                    if (console != null) {
                        char[] passwordChars = console.readPassword("Enter password: ");
                        loginPassword = new String(passwordChars); // convert char[] to String
                    } else {
                        // Fallback for IDEs like IntelliJ/Eclipse that don't support Console
                        System.out.println("⚠️ Running in IDE. Password will be visible.");
                        System.out.print("Enter password (visible): ");
                        loginPassword = sc.nextLine();
                    }

                    String loginHashedPassword = hashPassword(loginPassword);
                    int userId = userDao.loginUser(loginEmail, loginHashedPassword);

                    if (userId != -1) {
                        boolean loggedIn = true;
                        notificationDao.showUnread(userId);
                        notificationDao.cleanOldNotifications(userId);
                        while (loggedIn) {
                            System.out.println("————————————————————————————————————————————————");
                            System.out.println("\n Welcome to ░B░O░O░K░ ░F░L░OW░!!"+"    (^_^)" );
                            System.out.println("————————————————————————————————————————————————");
                            System.out.println("1.\uD83D\uDCDA My Shelf (Add / View / Remove)");
                            System.out.println("2.\uD83D\uDD0E Browse Books to Borrow");
                            System.out.println("3.\uD83D\uDCE4 Requests (Sent / Received)");
                            System.out.println("4.\uD83D\uDD01 Return + Review Book");
                            System.out.println("5.\uD83D\uDCB3 My Transactions");
                            System.out.println("6.\uD83D\uDD14 Notifications");
                            System.out.println("7.\uD83D\uDD12 Logout");
                            System.out.println("————————————————————————————————————————————————");
                            System.out.println("Choose: ");
                            int loggedInChoice = sc.nextInt();
                            sc.nextLine();

                            switch (loggedInChoice) {
                                case 1:
                                    boolean loogs=true;
                                    while (loogs) {
                                        System.out.println("1. Add Book");
                                        System.out.println("2. View My Shelf");
                                        System.out.println("3. Remove Book");
                                        System.out.println("4. Back to Bookflow");
                                        int shelfChoice = sc.nextInt();
                                        sc.nextLine();


                                        switch (shelfChoice) {
                                            case 1:
                                                System.out.print("Title: ");
                                                String title = sc.nextLine();
                                                System.out.print("Author: ");
                                                String author = sc.nextLine();
                                                System.out.print("Genre: ");
                                                String genre = sc.nextLine();
                                                System.out.print("ISBN: ");
                                                String isbn = sc.nextLine();
                                                System.out.print("Publication Year: ");
                                                int pubYear = sc.nextInt();
                                                sc.nextLine();
                                                System.out.print("Is Digital? (true/false): ");
                                                boolean isDigital = sc.nextBoolean();
                                                sc.nextLine();
                                                System.out.print("Share Method (email/whatsapp/manual): ");
                                                String shareMethod = sc.nextLine();
                                                System.out.print("Optional Note: ");
                                                String sharedNote = sc.nextLine();

                                                //  Add payment option from lender
                                                System.out.print("Is Payment Required? for flow of Your Book(true/false): ");
                                                boolean paymentRequired = sc.nextBoolean();
                                                sc.nextLine();
                                                System.out.print("Payment type (e.g., ₹50 or buymeacoffee.com/you): ");
                                                String paymentNote = sc.nextLine();

                                                Book book = new Book(title, author, genre, isbn, pubYear);
                                                int bookId = bookDao.addOrFindBook(book);

                                                // Update UserBook constructor if needed to match new fields
                                                UserBook userBook = new UserBook(userId, bookId, "owned", isDigital, shareMethod, sharedNote);
                                                userBook.setPaymentRequired(paymentRequired);
                                                userBook.setPaymentNote(paymentNote);


                                                userBookDao.addUserBook(userBook);
                                                // After userBookDao.addUserBook(userBook);
                                                List<Integer> allUserIds = userDao.getAllUserIdsExcept(userId);
                                                for (int uid : allUserIds) {
                                                    String noteMsg = "📢 " + title + " by " + author + " just got added! Want to borrow?";
                                                    Notification notification = new Notification(uid, noteMsg);
                                                    notificationDao.createNotification(notification);
                                                }
                                                break;

                                            case 2:
                                                userBookDao.showMyShelf(userId);
                                                break;

                                            case 3:
                                                System.out.print("Enter book ID to remove: ");
                                                int removebookId = sc.nextInt();
                                                sc.nextLine();
                                                userBookDao.removeBookFromShelf(userId, removebookId);
                                                System.out.println("Book removed from shelf.");
                                                break;

                                            case 4:
                                                loogs = false;
                                                break;

                                            default:
                                                System.out.println("❌ Invalid option. Please try again.");

                                        }

                                    }break;

                                case 2:
                                    boolean inBrowse = true;
                                    while (inBrowse) {
                                        System.out.println("——————————————————————————————");
                                        userBookDao.showAvailableBooks();
                                        System.out.println("——————————————————————————————");
                                        System.out.println("1. 📥 Borrow a Book");
                                        System.out.println("2. 📚 View My Borrowed Books");
                                        System.out.println("3. 🔙 Back to BookFlow Menu");
                                        System.out.print("Choose: ");
                                        int browseChoice = sc.nextInt();
                                        sc.nextLine();

                                        switch (browseChoice) {
                                            case 1:
                                                System.out.print("Enter UserBook ID to borrow: ");
                                                int userBookId = sc.nextInt();
                                                sc.nextLine();

                                                // 💡 Here we need to know who is the LENDER
                                                int lenderId = userBookDao.getLenderIdByUserBookId(userBookId);
                                                if (lenderId == -1) {
                                                    System.out.println("⚠️ Invalid UserBook ID.");
                                                    break;
                                                }

                                                java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
                                                java.sql.Date dueDate = new java.sql.Date(System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000)); // 7 days

                                                Borrow borrow = new Borrow(0, lenderId, userId, userBookId, today, dueDate, "pending");
                                                borrow.setLenderId(lenderId);
                                                borrow.setBorrowerId(userId);
                                                borrow.setUserBookId(userBookId);
                                                borrow.setBorrowDate(today);
                                                borrow.setDueDate(dueDate);
                                                borrow.setStatus("requested");

                                                int requestId = borrowDao.requestBorrow(borrow);
                                                if (requestId != -1) {
                                                    System.out.println("✅ Borrow request created successfully! Request ID: " + requestId);
                                                } else {
                                                    System.out.println("❌ Failed to create borrow request.");
                                                }
                                                break;

                                            case 2:
                                                borrowDao.showBorrowedBooks(userId);
                                                break;

                                            case 3:
                                                inBrowse = false;
                                                break;

                                            default:
                                                System.out.println("❌ Invalid option. Please choose again.");
                                        }
                                    }
                                    break;



                                case 3:
                                    boolean looogs = true;
                                    while (looogs) {
                                        System.out.println("\n📬 Request Management Menu:");
                                        System.out.println("1. View My Requests");
                                        System.out.println("2. Accept/Reject Received Requests");
                                        System.out.println("3. Back to Flow");
                                        System.out.print("Enter your choice: ");
                                        int reqChoice = sc.nextInt();
                                        sc.nextLine(); // Consume newline

                                        switch (reqChoice) {
                                            case 1:
                                                borrowDao.showMyRequests(userId);
                                                break;

                                            case 2:
                                                System.out.print("Enter Borrow ID to accept/reject: ");
                                                int borrowId = sc.nextInt();
                                                sc.nextLine(); // Consume newline

                                                System.out.print("Accept or Reject? (accept/reject): ");
                                                String action = sc.nextLine().trim().toLowerCase();

                                                switch (action) {
                                                    case "accept":
                                                        borrowDao.acceptRequest(borrowId);
                                                        break;
                                                    case "reject":
                                                        borrowDao.rejectRequest(borrowId);
                                                        break;
                                                    default:
                                                        System.out.println("❌ Invalid action. Please enter 'accept' or 'reject'.");
                                                        break;
                                                }
                                                break;

                                            case 3:
                                                looogs = false;
                                                System.out.println("🔙 Returning to main menu...");
                                                break;

                                            default:
                                                System.out.println("❌ Invalid option. Please try again.");
                                        }
                                    }
                                    break;

                                case 4:
                                    System.out.print("📖 Enter Borrow ID to return: ");
                                    int returnBorrowId = sc.nextInt();
                                    sc.nextLine(); // Consume newline

                                    int userBookIdToReturn = borrowDao.getUserBookIdByBorrowId(returnBorrowId);
                                    if (userBookIdToReturn == -1) {
                                        System.out.println("❌ Invalid Borrow ID. Please check and try again.");
                                        break;
                                    }

// Collect review
                                    System.out.print("📝 Share your review: ");
                                    String reviewText = sc.nextLine();

                                    int rating = 0;
                                    while (true) {
                                        System.out.print("⭐ Rate the book (1 to 5): ");
                                        rating = sc.nextInt();
                                        sc.nextLine(); // Consume newline

                                        if (rating >= 1 && rating <= 5) break;
                                        System.out.println("❌ Invalid rating. Please enter a number between 1 and 5.");
                                    }
                                    // Save review and update borrow status
                                    Review review = new Review(userBookIdToReturn, userId, rating, reviewText);
                                    reviewDao.addReview(review);
                                    borrowDao.returnBook(returnBorrowId);

                                    System.out.println("✅ Book returned successfully and your review has been recorded. Thank you!");
                                    break;


                                case 5:
                                    System.out.println("1. View Transaction History");
                                    System.out.println("2. View Pending Payments");
                                    System.out.println("3. Mark Payment as Paid");
                                    int transOpt = sc.nextInt(); sc.nextLine();
                                    if (transOpt == 1) {
                                        paymentDao.showTransactionsForUser(userId);
                                    } else if (transOpt == 2) {
                                        paymentDao.showPendingPaymentsForUser(userId);
                                    } else if (transOpt == 3) {
                                        System.out.print("Enter Borrow ID to mark payment as paid: ");
                                        int paymentBorrowId = sc.nextInt(); sc.nextLine();
                                        paymentDao.markPaymentPaid(paymentBorrowId);
                                    }
                                    break;

                                case 6:
                                    System.out.println("1. View Unread Notifications");
                                    System.out.println("2. View All Notifications");
                                    int noti = sc.nextInt(); sc.nextLine();
                                    if (noti == 1) {
                                        notificationDao.showUnread(userId);
                                    } else {
                                        notificationDao.showAll(userId);
                                    }
                                    break;


                                case 7:
                                    loggedIn = false;
                                    System.out.println("🔒 Logged out.");
                                    break;

                                default:
                                    System.out.println("❌ Invalid option.");
                            }
                        }
                    }
                    break;

                case 3:
                    running = false;
                    System.out.println("👋 lets flow the books again!");
                    break;

                default:
                    System.out.println("❌ Invalid option.");
            }
        }

        sc.close();
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found.", e);
        }
    }

    public static String truncate(String str, int max) {
        return UserBookDao.truncate(str, max);
    }
}

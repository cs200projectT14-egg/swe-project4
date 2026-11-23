import java.nio.file.Path;
import java.util.Scanner;
/*
* Console class allows for highlighting and ease of menu navigation
*/
class Console {
/**
  * Highlights shortcut on the terminal.
  * @param string the label
  * @return string of the menu selection.
   */
  public static String highlight(String string) {
    if (string == null || string.isEmpty()) {
      return string;
    }
    return "\u001b[32;1m" + string.charAt(0) + "\u001b[0m" + string.substring(1);
  }
}
/*
* Loads ChocAn Database, logs in authorized users, and saves updates on shutdown.
*/
public class Terminal {
 // Program entry point, uses terminal to navigate
  public static void main(String[] args) {
    System.out.println("Welcome to the ChocAn terminal.");

    Path databasePath = Path.of("chocan.db");
    Database database = new Database(databasePath);
    Scanner scanner = new Scanner(System.in);
    // While superloop allows for any user to navigate the menu with their ID
    // members not authorized
    // Switch statement moves user to respective menu needed.
    while (true) {
      String id = null;
      while (id == null || id.isEmpty()) {
        System.out.print("Enter your id (operator, provider, or manager), or \"exit\": ");
        id = scanner.next();
      }

      if (id.trim().equals("exit")) {
        System.err.println("Exiting");
        break;
      }

      if (database.people.get(id) == null) {
        System.err.println("Verify number failed; invalid ID");
        continue;
      }

      char role = id.charAt(0);
      switch (role) {
        // select operator
        case 'o':
          System.out.println("Hello operator!");
          Operator operator = (Operator) database.people.get(id);
          operator.actionLoop(scanner, database);
          break;
        // select manager
        case 'm':
          System.out.println("Hello manager!");
          Manager manager = (Manager) database.people.get(id);
          manager.actionLoop(scanner, database);
          break;
        // select provider
        case 'p':
          System.out.println("Hello provider!");
          Provider provider = (Provider) database.people.get(id);
          provider.actionLoop(scanner, database);
          break;
        // select member, recognized but not authorized
        case 'c':
          System.out.println("Hello member! Unfortunately, members can't login.");
          break;
        //should not be able to access, exit
        default:
          System.err.println("Invalid role! This should be unreachable.");
          System.exit(1);
          break;
      }
    }

    System.out.println("Shutting down...");
    database.save();
    scanner.close();
  }
}

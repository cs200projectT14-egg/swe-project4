import java.nio.file.Path;
import java.util.Scanner;

class Console {
  public static String highlight(String string) {
    if (string == null || string.isEmpty()) {
      return string;
    }
    return "\u001b[32;1m" + string.charAt(0) + "\u001b[0m" + string.substring(1);
  }
}

public class Terminal {
  public static void main(String[] args) {
    System.out.println("Welcome to the ChocAn terminal.");

    Path databasePath = Path.of("chocan.db");
    Database database = new Database(databasePath);
    Scanner scanner = new Scanner(System.in);

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
        case 'o':
          System.out.println("Hello operator!");
          Operator operator = (Operator) database.people.get(id);
          operator.actionLoop(scanner, database);
          break;
        case 'm':
          System.out.println("Hello manager!");
          Manager manager = (Manager) database.people.get(id);
          manager.actionLoop(scanner, database);
          break;
        case 'p':
          System.out.println("Hello provider!");
          Provider provider = (Provider) database.people.get(id);
          provider.actionLoop(scanner, database);
          break;
        case 'c':
          System.out.println("Hello member! Unfortunately, members can't login.");
          // Member member = (Member) database.people.get(id);
          // member.actionLoop(scanner, database);
          break;
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

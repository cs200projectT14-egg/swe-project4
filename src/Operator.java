import java.util.Scanner;

/*
* Operator class has operator access to manage ChocAn records:
* add, edit, and delete members and providers from the database.
 */
class Operator extends Person<Operator> {
  /**
  * Constructs an Operator from separate values.
  * @param id the operator's ID number.
  * @param name the operator's name.
  * @param address the operator's address.
  * @param status status value passed in (not stored by this constructor).
  */
  Operator(String id, String name, String address, String status) {
    this.id = id;
    this.name = name;
    this.address = address;
  }

  /**
  * Constructs an Operator from an id|name|address record in the database.
  * @param serialization one line of the database file, with values split by "|".
  */
  Operator(String serialization) {
    // Split the line into its columns: id, name, address
    String[] columns = serialization.split("\\|");

    this.id = columns[0];
    this.name = columns[1];
    this.address = columns[2];
  }

  // @Override
  // Operator deserialize(String serialization) {
  //     return new Operator(serialization);
  // }

  /**
  * Creates an Operator record formatted string.
  * @return id|name|address format for record keeping
  */
  @Override
  String serialize() {
    return String.join("|", id, name, address);
  }

  /** Runs ChocAn menu for Operators.
  * Each action is picked by typing its highlighted first letters, then the member or provider
  * record (or ID, for deleting) on the same line.
  * @param scanner terminal input to navigate.
  * @param database ChocAn database to add, edit, or delete people in.
  */
  void actionLoop(Scanner scanner, Database database) {
    // Keeps showing the menu until the operator logs out
    while (true) {
      System.out.println("What would you like to do, operator?");
      // Menu options, with the first letter of each word highlighted as the shortcut
      System.out.println(
          "You may perform the following actions:\n"
              + Console.highlight("add a ")
              + Console.highlight("member\n")
              + Console.highlight("edit a ")
              + Console.highlight("member\n")
              + Console.highlight("delete a ")
              + Console.highlight("member\n")
              + Console.highlight("add a ")
              + Console.highlight("provider\n")
              + Console.highlight("edit a ")
              + Console.highlight("provider\n")
              + Console.highlight("delete a ")
              + Console.highlight("provider\n")
              + Console.highlight("logout\n"));

      // Ask until the user types something
      String action = null;
      while (action == null || action.isEmpty()) {
        System.out.print("Select an action: ");
        action = scanner.next();
      }

      // Switch statement moves to the action the operator chose
      switch (action) {
        // Add Member, rest of the line is the new member's record
        case "am" -> {
          String serialization = scanner.nextLine().trim();
          if (serialization == null || serialization.isEmpty()) {
            System.err.println("Please provide a serialization!");
            continue;
          }

          database.add(new Member(serialization));
        }
        // Edit Member, rest of the line is the updated record
        case "em" -> {
          String serialization = scanner.nextLine().trim();
          if (serialization == null || serialization.isEmpty()) {
            System.err.println("Please provide a serialization!");
            continue;
          }

          database.edit(new Member(serialization));
        }
        // Delete Member, rest of the line is the member's ID
        case "dm" -> {
          String id = scanner.nextLine().trim();
          if (id == null || id.isEmpty()) {
            System.err.println("Please provide an ID!");
            continue;
          }

          database.delete(id);
        }
        // Add Provider, rest of the line is the new provider's record
        case "ap" -> {
          String serialization = scanner.nextLine().trim();
          if (serialization == null || serialization.isEmpty()) {
            System.err.println("Please provide a serialization!");
            continue;
          }

          database.add(new Provider(serialization));
        }
        // Edit Provider, rest of the line is the updated record
        case "ep" -> {
          String serialization = scanner.nextLine().trim();
          if (serialization == null || serialization.isEmpty()) {
            System.err.println("Please provide a serialization!");
            continue;
          }

          database.edit(new Provider(serialization));
        }
        // Delete Provider, rest of the line is the provider's ID
        case "dp" -> {
          String id = scanner.nextLine().trim();
          if (id == null || id.isEmpty()) {
            System.err.println("Please provide an ID!");
            continue;
          }

          database.delete(id);
        }
        // Logout, return to the login prompt
        case "l" -> {
          return;
        }
        // Anything else is not a menu option
        default -> {
          System.err.println("Invalid action!");
          continue;
        }
      }
    }
  }
}

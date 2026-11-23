import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Pattern;
/*
* Provider class has provider access to provider directory, record service to member,
* and verify without servicing
 */
class Provider extends Person<Provider> {
  Provider(String id, String name, String address, String status) {
    this.id = id;
    this.name = name;
    this.address = address;
  }
  /*
  * Constructs Provider from id|name|address record in database.
  */
  Provider(String serialization) {
    String[] columns = serialization.split("\\|");

    this.id = columns[0];
    this.name = columns[1];
    this.address = columns[2];
  }
  /**
  * Creates a Provider record formatted string.
  * @return id|name|address format for record keeping
  */
  @Override
  String serialize() {
    return String.join("|", id, name, address);
  }
/** Runs ChocAn menu for Providers.
* @param scanner terminal input to navigate.
* @param database ChocAn database to search client ID.
*/
  void actionLoop(Scanner scanner, Database database) {
    while (true) {
      System.out.println("What would you like to do, provider?");
      System.out.println(
          "You may perform the following actions:\n"
              + Console.highlight("open ")
              + Console.highlight("provider directory\n")
              + Console.highlight("verify ")
              + Console.highlight("member\n")
              + Console.highlight("report ")
              + Console.highlight("service\n")
              + Console.highlight("logout\n"));

      String action = null;
      while (action == null || action.isEmpty()) {
        System.out.print("Select an action: ");
        action = scanner.next();
      }

      switch (action) {
        // Open Provider Directory
        case "op" -> {
          try {
            Path filePath = Path.of("providerDirectory.db");
            String content = Files.readString(filePath);
            System.out.println(content);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        // Verify Member, and print their status(invalid, valid, not in good standing)
        case "vm" -> {
          String clientId = scanner.nextLine().trim();
          if (clientId == null || clientId.isEmpty()) {
            System.err.println("Please provide a Member ID!");
            continue;
          }

          Person<?> person = database.people.get(clientId);
          if (person == null) {
            System.err.println("Invalid Number!");
            continue;
          } else if (person instanceof Member member && member.status.equals("active")) {
            System.out.println("Member is valid");
          } else if (person instanceof Member member) {
            System.err.println("Member is not in good standing. (" + member.status + ")");
          } else {
            System.err.println("Not a Member!");
          }
        }
        // Report service, service a member by verifying their ID then entering a service code.
        case "rs" -> {
          String clientId = scanner.nextLine().trim();
          if (clientId == null || clientId.isEmpty()) {
            System.err.println("Please provide a Member ID!");
            continue;
          }
          // Search for member
          Person<?> person = database.people.get(clientId);
          if (person == null) {
            System.err.println("Invalid Number!");
            continue;
          } else if (person instanceof Member member && member.status.equals("active")) {
            System.out.println("Validated");
          } else if (person instanceof Member member) {
            System.err.println("Did you verify member first?");
            continue;
          } else {
            System.err.println("Not a Member!");
            continue;
          }
          
          String inDate = null;
          while (inDate == null || inDate.isEmpty()) {
            System.out.print("Enter Date of Service (MM-DD-YYYY): ");
            inDate = scanner.next();
          }
          
          String serviceCode = null;
          while (serviceCode == null || serviceCode.isEmpty()) {
            System.out.print("Enter Service Code: ");
            serviceCode = scanner.next();
          }

          switch (serviceCode) {
            case "598470" -> {
              System.out.print("Dietician, ");
            }
            case "883948" -> {
              System.out.print("Aerobics Exercise Session, ");
            }
            default -> {
              System.err.println("That is not a code!");
              continue;
            }
          }
          
          String response = null;
          while (response == null || response.isEmpty()) {
            System.out.print("Correct? (y/n): ");
            response = scanner.next();
          }

          if (!response.equals("y")) {
            System.out.println("Aborting");
            continue;
          }
          
          Pattern oldDelimiter = scanner.delimiter();
          scanner.useDelimiter("\n");
          String comments = null;
          while (comments == null || comments.isEmpty()) {
            System.out.print("Enter Comments: ");
            comments = scanner.next().trim();
          }
          scanner.useDelimiter(oldDelimiter);
          //add new service record to existing member file
          Path clientPath = Path.of("clients/" + database.people.get(clientId).name + ".txt");
          LocalDateTime now = LocalDateTime.now();
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
          String clientContent =
              now.format(formatter)
                  + "\n"
                  + inDate
                  + "\n"
                  + id
                  + "\n"
                  + clientId
                  + "\n"
                  + serviceCode
                  + "\n"
                  + comments;

          try {
            Files.createFile(clientPath);
          } catch (FileAlreadyExistsException e) {
            clientContent = "\n|\n" + clientContent;
          } catch (IOException e) {
            System.err.println("Failed to create file: " + e.getMessage());
          }
          try {
            Files.writeString(clientPath, clientContent, StandardOpenOption.APPEND);
          } catch (IOException e) {
            System.err.println("Failed to write");
          }
        }
        //logout
        case "l" -> {
          return;
        }
        //Should not be able to reach, error
        default -> {
          System.err.println("Invalid action!");
          continue;
        }
      }
    }
  }
}

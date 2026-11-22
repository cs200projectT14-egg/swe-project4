import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Scanner;

class Manager extends Person<Manager> {
  Manager(String id, String name, String address, String status) {
    this.id = id;
    this.name = name;
    this.address = address;
  }

  Manager(String serialization) {
    String[] columns = serialization.split("\\|");

    this.id = columns[0];
    this.name = columns[1];
    this.address = columns[2];
  }

  @Override
  String serialize() {
    return String.join("|", id, name, address);
  }

  void actionLoop(Scanner scanner, Database database) {
    while (true) {
      System.out.println("What would you like to do, manager?");
      System.out.println(
          "You may perform the following actions:\n"
              + Console.highlight("do ")
              + Console.highlight("accounting procedures\n")
              + Console.highlight("logout\n"));

      String action = null;
      while (action == null || action.isEmpty()) {
        System.out.print("Select an action: ");
        action = scanner.next();
      }

      switch (action) {
        case "da" -> {
          reports(scanner, database);
        }
        case "l" -> {
          return;
        }
        default -> {
          System.err.println("Invalid action!");
          continue;
        }
      }
    }
  }

  void reports(Scanner scanner, Database database) {
    HashMap<String, String> providers = new HashMap<>();
    HashMap<String, Integer> providerTotal = new HashMap<>();
    HashMap<String, Double> providerCost = new HashMap<>();

    database.people.forEach((id, person) -> {
      if (person instanceof Member) {
        Path clientPath = Path.of("clients/" + person.name + ".txt");
        if (Files.exists(clientPath)) {
          Path clientReportPath = Path.of("reports/" + person.name + ".txt");
          String reportContent = person.name + "\n" + person.id + "\n" + person.address + "\n\n";

          try {
            String content = Files.readString(clientPath);
            String[] meetings = content.split("\\|");
            for (String meeting : meetings) {
              String[] data = meeting.split("\n");
              if (!providers.containsKey(database.people.get(data[2]).name)) {
                providers.put(
                    database.people.get(data[2]).name,
                    database.people.get(data[2]).name
                        + "\n"
                        + data[2]
                        + "\n"
                        + database.people.get(data[2]).address
                        + "\n\n");
              }
              double amt = 0;

              reportContent += data[1] + "\n" + database.people.get(data[2]).name + "\n";
              switch (data[4]) {
                case "598470" -> {
                  reportContent += "Dietician\n\n";
                  amt = 20.00;
                }
                case "883948" -> {
                  reportContent += "Aerobics Exercise Session\n\n";
                  amt = 10.00;
                }
                default -> {
                  System.err.println("What am i even doing with my life anymore");
                }
              }
              providerCost.merge(database.people.get(data[2]).name, amt, Double::sum);
              providerTotal.merge(database.people.get(data[2]).name, 1, Integer::sum);
              providers.put(
                  database.people.get(data[2]).name,
                  providers.get(database.people.get(data[2]).name)
                      + data[1]
                      + "\n"
                      + data[0]
                      + "\n"
                      + database.people.get(data[3]).name
                      + "\n"
                      + data[3]
                      + "\n"
                      + data[4]
                      + "\n"
                      + amt
                      + "\n\n");
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
          try {
            Files.writeString(clientReportPath, reportContent, StandardOpenOption.CREATE);
          } catch (IOException e) {
            System.err.println("failed to write");
          }
        }
      }
    });
    String summaryContent = "";
    int providerTotalNum = 0;
    int consultationTotal = 0;
    double overallFee = 0.0;
    for (String provider : providers.keySet()) {
      summaryContent +=
          provider
              + "\n"
              + providerTotal.get(provider)
              + "\n"
              + providerCost.get(provider)
              + "\n\n";
      providerTotalNum += 1;
      consultationTotal += providerTotal.get(provider);
      overallFee += providerCost.get(provider);

      Path providerPath = Path.of("reports/" + provider + ".txt");
      try {
        Files.writeString(
            providerPath,
            providers.get(provider)
                + providerTotal.get(provider)
                + "\n"
                + providerCost.get(provider)
                + "\n",
            StandardOpenOption.CREATE);
      } catch (IOException e) {
        System.err.println("failed to write");
      }

      Path summaryReport = Path.of("reports/summaryReport.txt");
      summaryContent += providerTotalNum + "\n" + consultationTotal + "\n" + overallFee + "\n";
      try {
        Files.writeString(summaryReport, summaryContent, StandardOpenOption.CREATE);
      } catch (IOException e) {
        System.err.println("failed to write");
      }
    }
  }
}

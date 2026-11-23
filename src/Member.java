import java.util.Scanner;

class Member extends Person<Member> {
  String status;

  Member(String id, String name, String address, String status) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.status = status;
  }

  Member(String serialization) {
    String[] columns = serialization.split("\\|");

    this.id = columns[0];
    this.name = columns[1];
    this.address = columns[2];
    this.status = columns[3];
  }

  @Override
  String serialize() {
    return String.join("|", id, name, address, status);
  }

  void actionLoop(Scanner scanner, Database database) {
    while (true) {
      System.err.println("You are " + this.serialize());

      System.out.println("What would you like to do, member?");
      // Q: What may a member do?
      // A: We've concluded they aren't allowed to login.

      String action = null;
      while (action == null || action.isEmpty()) {
        System.out.print("Select an action: ");
        action = scanner.next();
      }
    }
  }

  void setStatus(String status) {
    this.status = status;
  }

  String getMemberInfo() {
    StringBuilder sb = new StringBuilder("Member info: ");

    sb.append(" ");
    sb.append(id);

    sb.append(" ");
    sb.append(name);

    sb.append(", ");
    sb.append(address);

    sb.append(" (");
    sb.append(status);
    sb.append(")");

    return sb.toString();
  }
}

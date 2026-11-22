import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Stream;

public class Database {
  HashMap<String, Person<?>> people;
  Path path;

  Database(Path path) {
    this.path = path;

    if (!Files.exists(path)) {
      try {
        Files.createFile(path);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    HashMap<String, Person<?>> people = new HashMap<>();
    try (Stream<String> lines = Files.lines(this.path)) {
      lines.forEach(line -> {
        Person<?> person = Person.deserialize(line);
        people.put(person.id, person);
      });
    } catch (IOException e) {
      System.err.println("Error reading file '" + this.path + "': " + e.getMessage());
      System.exit(1);
    }

    this.people = people;
  }

  void save() {
    try (FileOutputStream fos = new FileOutputStream(this.path.toString());
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        PrintWriter writer = new PrintWriter(osw); ) {
      for (Person<?> person : this.people.values()) {
        String serialized = person.serialize();
        writer.println(serialized);
      }

      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  void add(Person<?> person) {
    this.people.put(person.id, person);
    System.out.println("Added person " + person.serialize() + ".");
  }

  void edit(Person<?> person) {
    Person<?> oldPerson = this.people.replace(person.id, person);
    Person<?> newPerson = this.people.get(person.id);

    System.out.println(
        "Edited person from " + oldPerson.serialize() + " to " + newPerson.serialize() + ".");
  }

  void delete(String id) {
    if (this.people.remove(id) instanceof Person<?> person) {
      System.out.println("Removed person " + person.serialize() + ".");
      return;
    }

    System.err.println("Member with id " + id + " was not in the database.");
  }
}

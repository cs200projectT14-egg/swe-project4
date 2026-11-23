import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Stream;

/*
* Database class holds every ChocAn person (members, providers, operators, managers) in memory,
* loads them from a text file when the program starts, and saves them back to the file on shutdown.
* Each person is stored by their ID.
 */
public class Database {
  // All people in the system, keyed by their ID number
  HashMap<String, Person<?>> people;
  // Location of the file that stores the records between runs
  Path path;

  /**
  * Creates the database and loads existing records from the file.
  * If the file does not exist yet, an empty one is created first.
  * Each line of the file is one person in id|name|address format.
  * @param path location of the database file (for example chocan.db).
  */
  Database(Path path) {
    this.path = path;

    // Create a database if one doesn't exist
    if (!Files.exists(path)) {
      try {
        Files.createFile(path);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Temporary map to fill while reading the file
    HashMap<String, Person<?>> people = new HashMap<>();
    // Read the file line by line and turn each line into a Person object
    try (Stream<String> lines = Files.lines(this.path)) {
      lines.forEach(line -> {
        Person<?> person = Person.deserialize(line);
        people.put(person.id, person);
      });
    } catch (IOException e) {
      // The program cannot run without its data, so print the error and stop
      System.err.println("Error reading file '" + this.path + "': " + e.getMessage());
      System.exit(1);
    }

    // Save the finished map as the database's people
    this.people = people;
  }

  /**
  * Writes every person in memory back to the database file.
  * The old file contents are replaced, one person per line.
  */
  void save() {
    // try-with-resources closes the file streams automatically when finished
    try (FileOutputStream fos = new FileOutputStream(this.path.toString());
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        PrintWriter writer = new PrintWriter(osw); ) {
      // Write each person as one id|name|address line
      for (Person<?> person : this.people.values()) {
        String serialized = person.serialize();
        writer.println(serialized);
      }

      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
  * Adds a new person to the database and prints a confirmation.
  * If the ID already exists, the old record is replaced.
  * @param person the member, provider, or other person to add.
  */
  void add(Person<?> person) {
    this.people.put(person.id, person);
    System.out.println("Added person " + person.serialize() + ".");
  }

  /**
  * Replaces an existing person's record with updated information and prints the change.
  * The person is matched by ID.
  * @param person the updated record, with the same ID as the person being edited.
  */
  void edit(Person<?> person) {
    // replace returns the old record, so it can be shown in the confirmation
    Person<?> oldPerson = this.people.replace(person.id, person);
    Person<?> newPerson = this.people.get(person.id);

    System.out.println(
        "Edited person from " + oldPerson.serialize() + " to " + newPerson.serialize() + ".");
  }

  /**
  * Removes a person from the database by ID.
  * Prints a confirmation, or an error if no one has that ID.
  * @param id the ID of the person to remove.
  */
  void delete(String id) {
    // remove returns the removed person, or null if the ID was not found
    if (this.people.remove(id) instanceof Person<?> person) {
      System.out.println("Removed person " + person.serialize() + ".");
      return;
    }

    System.err.println("Member with id " + id + " was not in the database.");
  }
}

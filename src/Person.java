/**
 * Base class representing a system user (Member, Operator, Manager, or Provider).
 * Uses the Curiously Recurring Template Pattern (CRTP) to maintain type safety
 * across subclasses.
 *
 * @param <T> The specific subclass type extending Person.
 */
abstract class Person<T extends Person<T>> {
  String id;
  String name;
  String address;

  /**
   * Factory method that reconstructs the appropriate Person subclass from a serialized string.
   * Determines the subclass based on the first character prefix of the string:
   * - 'c': Member (Client)
   * - 'o': Operator
   * - 'm': Manager
   * - 'p': Provider
   *
   * @param serialization A pipe-delimited string starting with a valid role prefix.
   * @return A newly instantiated subclass of Person.
   * @throws IllegalArgumentException If the leading character does not match a known role.
   */
  static Person<?> deserialize(String serialization) {
    return switch (serialization.charAt(0)) {
      case 'c' -> new Member(serialization);
      case 'o' -> new Operator(serialization);
      case 'm' -> new Manager(serialization);
      case 'p' -> new Provider(serialization);
      default -> throw new IllegalArgumentException("Invalid person type");
    };
  }

  /**
   * Converts the user's fields into a pipe-delimited string for file storage.
   *
   * @return A serialized string representation of the person.
   */
  abstract String serialize();
}

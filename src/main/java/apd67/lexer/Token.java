package apd67.lexer;

/** Token class */
public class Token extends java_cup.runtime.Symbol {
  /** The type of the token */
  int type;
  /**
   * The object associated with this token. as follows; <br>
   * PRIMITIVE TYPES:
   * <li>int -> "int"
   * <li>bool -> "bool" <br>
   *     INTEGERS:
   * <li>12 -> 12 : int <br>
   *     IDENTIFIER:
   * <li>pot -> "pot" <br>
   *     STRING / CHARACTER
   * <li>"pot" -> "pot"
   * <li>'y' -> "y" <br>
   *     KEYWORDS:
   * <li>if -> "if"
   */
  Object attribute;
  /** The line on which the token was identified */
  int line;
  /** The column on which the token was identified */
  int column;

  /**
   * @param type The type of the token to be created
   * @param line The line the token was identified on
   * @param column The column the token was identified on
   */
  Token(int type, int line, int column, Object attr) {
    super(type, column, line, attr);
    this.type = type;
    attribute = attr;
    this.line = line;
    this.column = column;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  public String getAttribute() {
    String out;
    switch (type) {
      case apd67.parser.sym.CHAR:
        out = "character " + attribute;
        break;

      case apd67.parser.sym.ID:
        out = "id " + attribute;
        break;

      case apd67.parser.sym.INT:
        out = "integer " + attribute;
        break;

      case apd67.parser.sym.MIN_INT:
        out = "integer 9223372036854775808";
        break;

      case apd67.parser.sym.STRING:
        out = "string " + attribute;
        break;

      default:
        out = "" + attribute;
        break;
    }
    return out;
  }

  /**
   * String of the format {line}:{column} {attribute} NOTE: Attributes for integers, strings, ids
   * and characters prepend the full integer, string, id, character to the {attribute} string
   * representation.
   */
  public String toString() {
    return "" + (line) + ":" + (column) + " " + getAttribute() + "";
  }
}

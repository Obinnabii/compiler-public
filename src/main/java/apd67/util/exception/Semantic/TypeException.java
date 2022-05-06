package apd67.util.exception;

/** The exception to be thrown during Typechecking. */
public class TypeException extends SemanticError {
  public TypeException(String message, int line, int column) {
    super(message, line, column);
  }
}

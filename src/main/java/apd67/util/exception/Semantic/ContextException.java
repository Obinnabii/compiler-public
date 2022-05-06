package apd67.util.exception;

/** The exception to be thrown when illegal actions occur with the context. */
public class ContextException extends SemanticError {
  public ContextException(String message, int line, int column) {
    super(message, line, column);
  }
}

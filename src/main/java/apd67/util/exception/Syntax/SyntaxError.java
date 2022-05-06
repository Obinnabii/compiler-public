package apd67.util.exception;

/** The exception to be thrown during Typechecking. */
public class SyntaxError extends XiError {
  public SyntaxError(String message, int line, int column) {
    super(message, line, column);
    this.errorType = "Syntax";
  }
}

package apd67.util.exception;

/** The exception to be thrown when the Lexer runs into an error. */
public class LexicalError extends XiError {

  public LexicalError(String message, int line, int column) {
    super(message, line, column);
    this.errorType = "Lexical";
  }
}

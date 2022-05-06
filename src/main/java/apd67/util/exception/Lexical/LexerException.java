package apd67.util.exception;

/** The exception to be thrown when the Lexer runs into an error. */
public class LexerException extends LexicalError {

  public LexerException(String message, int line, int column) {
    super(message, line, column);
  }
}

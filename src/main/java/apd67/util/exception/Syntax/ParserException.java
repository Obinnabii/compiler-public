package apd67.util.exception;

import apd67.lexer.Token;

/** The exception to be thrown when the parser runs into an error. */
public class ParserException extends SyntaxError {

  /** The token on which the error occurred */
  public Token token;

  public Token getToken() {
    return token;
  }

  /**
   * Instantiate a new Parser Exception.
   *
   * @param message A message describing the error
   * @param token
   */
  public ParserException(String message, Token token) {
    super(message, token.getLine(), token.getColumn());
    this.token = token;
  }
}

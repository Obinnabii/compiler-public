package apd67.util.exception;

/** The exception to be thrown during Typechecking. */
public class SemanticError extends XiError {
  public SemanticError(String message, int line, int column) {
    super(message, line, column);
    this.errorType = "Semantic";
  }
}

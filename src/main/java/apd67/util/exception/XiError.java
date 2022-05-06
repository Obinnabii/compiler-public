package apd67.util.exception;

import java.io.IOException;

/** The exception to be thrown when the Lexer runs into an error. */
public abstract class XiError extends IOException {
  /** The line on which the error occured */
  public int line;

  /** The column on which the error occured */
  public int column;

  /** The name of the file in which this error occurred */
  public String filename = "<filename>";

  /** The type of error */
  protected String errorType = "Xi Language";

  /**
   * The error message formatted to meet CS4120 PA1 standards
   *
   * @return
   */
  public String getFormattedMessage() {
    String location = "" + line + ":" + column + " error:";
    return location + this.getMessage();
  }

  /** @return The error message formatted to meet CS4120 Typecheker standards */
  public String getTypecheckerFormattedMessage() {
    String location = ":" + line + ":" + column + ": ";
    return errorType + " error beginning at " + filename + location + this.getMessage();
  }

  /**
   * Set the filename to {@code filename} of this error only if it has not been set yet.
   *
   * @param filename
   * @return
   */
  public XiError optionalySetFilename(String filename) {
    this.filename = filename;
    return this;
  }

  public XiError setFilename(String filename) {
    this.filename = filename;
    return this;
  }

  /**
   * Instantiate a new Lexer Exception.
   *
   * @param message A message describing the error
   * @param line
   * @param column
   */
  public XiError(String message, int line, int column) {
    super(message);
    this.line = line;
    this.column = column;
  }
}

package apd67.util.ir;

import apd67.util.polyglot.SerialVersionUID;

/** Exception thrown when the compiler is confused. */
public class InternalCompilerError extends RuntimeException {
  private static final long serialVersionUID = SerialVersionUID.generate();

  public InternalCompilerError(String msg) {
    super(msg);
  }

  public InternalCompilerError(Throwable cause) {
    this(cause.getMessage(), cause);
  }

  public InternalCompilerError(String msg, Throwable cause) {
    super(msg, cause);
  }
}

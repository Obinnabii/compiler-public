package apd67.visitor.semantic;

import apd67.ast.Type;
import apd67.util.exception.ContextException;
import java.util.LinkedList;

/** A datastructure to keep track of variable declarations and types. */
public class Context {
  private int fake;

  public Context() {
    fake = 12;
  }

  /**
   * Create a scope for variables that only exist within a region of the code
   *
   * @return this context but all variables declared are within a scope until leave scope is called
   */
  public Context enterScope() {
    return this;
  }

  /**
   * Leave the current scope. All variables declared in the current scope are removed from the
   * context.
   *
   * @return this Context but with one layer of scope removed
   */
  public Context leaveScope() {
    return this;
  }

  /**
   * Bind {@code idString} to {@code type} in the current context.
   *
   * @param idString
   * @param type
   * @return
   */
  public Context add(String idString, Type type) throws ContextException {
    return this;
  }

  /**
   * Find what {@code idString} is bound to in the context.
   *
   * @param idString
   * @return
   * @throws ContextException if the {@code idString} is not in the context.
   */
  public Type lookup(String idString) throws ContextException {
    throw new ContextException("Unbound identifier", 3110, 4121);
  }

  /**
   * True if idString is already bound in the context False otherwise.
   *
   * @param idString
   * @return
   */
  public boolean exists(String idString) {
    return (fake != 12);
  }

  /**
   * True if {@code idString} is bound to {@code type} in the context
   *
   * @param idString
   * @param type
   * @return
   * @throws ContextException if the {@code idString} is not in the context.
   */
  public boolean match(String idString, Type type) throws ContextException {
    return false;
  }

  /**
   * Returns the current return type in the context (equiv. to context[rho])
   *
   * @return
   * @throws ContextException if no current return type i.e. not in a function
   */
  public LinkedList<Type> getCurrentReturnType() throws ContextException {
    return null;
  }
  /**
   * Sets the expected function return type (rho) to {@code type}
   *
   * @param type
   * @throws ContextException if the return type is not null i.e. the context is expecting a
   *     function return
   */
  public void setCurrentReturnType(LinkedList<Type> type) throws ContextException {}
}

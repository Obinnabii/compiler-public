package apd67.visitor.semantic;

import apd67.ast.Type;
import apd67.util.exception.ContextException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 * A default implementation of typing contexts utilizing a stack of hash tables. Each time a new
 * scope is entered, a new, empty hash table is pushed onto the stack, and when the scope is exited,
 * the most recent hash table is popped from the stack.
 */
public class ContextDefault extends Context {

  private Stack<HashMap<String, Type>> scopes = new Stack<HashMap<String, Type>>();

  private LinkedList<Type> returnType;

  private int scopeLevel;

  public ContextDefault() {
    scopeLevel = 1;
    scopes.push(new HashMap<String, Type>());
  }

  public Context enterScope() {
    scopeLevel++;
    scopes.push(new HashMap<String, Type>());
    return this;
  }

  public Context leaveScope() {
    scopes.pop();
    if (--scopeLevel == 1) {
      returnType = null;
    }
    return this;
  }

  public Context add(String idString, Type type) throws ContextException {
    for (HashMap<String, Type> scope : scopes) {
      Type binding = scope.get(idString);
      if (binding != null)
        throw new ContextException("error: " + idString + " already bound in context", 0, 0);
    }
    scopes.peek().put(idString, type);
    return this;
  }

  public Type lookup(String idString) throws ContextException {
    for (HashMap<String, Type> scope : scopes) {
      Type binding = scope.get(idString);
      if (binding != null) return binding;
    }
    // TODO : investigate how we get the line and column numbers
    throw new ContextException("error: name " + idString + " cannot be resolved", 0, 0);
  }

  public boolean exists(String idString) {
    for (HashMap<String, Type> scope : scopes) {
      if (scope.get(idString) != null) return true;
    }
    return false;
  }

  public boolean match(String idString, Type type) throws ContextException {
    for (HashMap<String, Type> scope : scopes) {
      Type binding = scope.get(idString);
      if (binding != null) return (binding.equals(type));
    }
    // TODO : investigate how we get the line and column numbers
    throw new ContextException("error: name " + idString + " cannot be resolved", 0, 0);
  }

  public LinkedList<Type> getCurrentReturnType() throws ContextException {
    if (returnType != null) return returnType;
    // TODO : investigate how we get the line and column numbers
    throw new ContextException("error: unexpected return", 0, 0);
  }

  public void setCurrentReturnType(LinkedList<Type> type) throws ContextException {
    if (returnType == null) {
      returnType = type;
    }
    // TODO : investigate how we get the line and column numbers
    else throw new ContextException("error: unexpected function scope", 0, 0);
  }
}

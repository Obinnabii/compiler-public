package apd67.ast;

import java.util.LinkedList;

public abstract class ParseTree extends Node {
  protected String name = "Main";

  /** A list of all the use statements declared in this parse tree. Empty if this is an interface */
  public LinkedList<Use> getUses() {
    return new LinkedList<>();
  }
  /** A list of all the methods declared/defined in this parse tree */
  public abstract LinkedList<MethodDeclaration> getMethodDeclarations();

  public ParseTree setName(String filename) {
    name = filename;
    return this;
  }
}

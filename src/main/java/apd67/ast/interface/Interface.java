package apd67.ast;

import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import java.util.LinkedList;

public class Interface extends ParseTree {
  public LinkedList<MethodDeclaration> methodDeclarations = new LinkedList<MethodDeclaration>();

  /**
   * Start an interface with it's first method declaration being a new methodDeclaration with the
   * specified id, params and types.
   *
   * @param id
   * @param params
   * @param types
   */
  public Interface(Identifier id, LinkedList<Declaration> params, LinkedList<Type> types) {
    this.methodDeclarations.add(new MethodDeclaration(id, params, types));
    setLineAndCol(id);
  }

  /**
   * Create an interface from a list of method declarations (to be used to make a new Interface from
   * an existing one)
   *
   * @param methodDeclarations
   */
  public Interface(LinkedList<MethodDeclaration> methodDeclarations) {
    this.methodDeclarations = methodDeclarations;
    setLineAndCol(methodDeclarations.peek());
  }

  /**
   * Add a method declaration to the list of declaration represented in this interface object
   *
   * @param methodDeclaration
   */
  public void appendMethodDeclaration(MethodDeclaration methodDeclaration) {
    methodDeclarations.add(methodDeclaration);
  }

  public LinkedList<MethodDeclaration> getMethodDeclarations() {
    return methodDeclarations;
  }

  @Override
  public Interface visitChildren(Visitor v) throws SemanticError {
    boolean isDifferent = false;

    LinkedList<MethodDeclaration> new_methodDeclarations = new LinkedList<>();
    for (MethodDeclaration d : methodDeclarations) {
      MethodDeclaration d_new = (MethodDeclaration) d.accept(v);
      new_methodDeclarations.addLast(d_new);
      isDifferent = isDifferent || d_new != d;
    }

    if (isDifferent) {
      return new Interface(new_methodDeclarations);
    } else {
      return this;
    }
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();
    cw.startUnifiedList();
    for (MethodDeclaration methodDeclaration : methodDeclarations) {
      methodDeclaration.prettyPrint(cw);
    }
    cw.endList();
    cw.endList();
    cw.close();
  }

  public String toString() {
    return "interface";
  }
}

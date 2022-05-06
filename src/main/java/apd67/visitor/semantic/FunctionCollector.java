package apd67.visitor.semantic;

import apd67.ast.*;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import java.util.LinkedList;

/**
 * A datastructure used in the first type-checking pass to collect function definitions and other
 * top-level statements
 */
public class FunctionCollector extends Visitor {
  public Context context = new ContextDefault();
  private Context fileContext = new ContextDefault();
  private String filename;

  public FunctionCollector(String filename) {
    this.filename = filename;
    loadLength();
  }

  public FunctionCollector() {
    this.filename = "";
    loadLength();
  }

  private void loadLength() {
    LinkedList<Declaration> params = new LinkedList<>();
    params.add(new Declaration(new Identifier("x"), new ArrayType(new IntType(), null)));
    LinkedList<Type> returnTypes = new LinkedList<>();
    returnTypes.add(new IntType());

    try {
      context.add("length", new MethodTypeAndParams(params, returnTypes));
    } catch (ContextException ce) {
      // do nothing, can't fail here
    }
  }

  public TypeChecker toTypeChecker() {
    return new TypeChecker(context);
  }

  public void collectFunctions(ParseTree pt, String filename) throws XiError {
    enterFile(filename);
    addMethods(pt.getMethodDeclarations());
  }

  private void enterFile(String filename) {
    this.filename = filename;
    // fileContext = new ContextDefault();
  }

  private void addMethods(LinkedList<MethodDeclaration> mds) throws XiError {
    for (MethodDeclaration md : mds) {
      addMethod(md);
    }
  }

  private void addMethod(MethodDeclaration md) throws XiError {
    MethodTypeAndParams mtp = md.getTypeAndParams();
    String name = md.getName();

    if (alreadyDefinedInFile(md)) {
      throw new TypeException(doubleDeclMessage(md), md.getLine(), md.getColumn());
    }

    if (context.exists(name)) {
      if (context.match(name, mtp)) return;
      else throw new TypeException(mismatchedDecl(md), md.getLine(), md.getColumn());
    }

    context.add(name, mtp);
  }

  private boolean alreadyDefinedInFile(MethodDeclaration md) throws ContextException {
    if (!fileContext.exists(md.getName())) {
      fileContext.add(md.getName(), md.getTypeAndParams());
      return false;
    }
    return true;
  }

  private String doubleDeclMessage(MethodDeclaration md) throws TypeException {
    return "Error: method " + md.getName() + " is declared twice in " + filename;
  }

  private String mismatchedDecl(MethodDeclaration md) {
    return "Error: method "
        + md.getName()
        + " signature in "
        + filename
        + " does not match the expected signature";
  }
}

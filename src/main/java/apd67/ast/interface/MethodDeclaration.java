package apd67.ast;

import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.util.exception.SemanticError;
import apd67.visitor.Visitor;
import java.util.LinkedList;

// Set line and col
public class MethodDeclaration extends Node {
  private Identifier id;
  private MethodTypeAndParams typeAndParams;

  public MethodDeclaration(Identifier id, LinkedList<Declaration> params, LinkedList<Type> types) {
    this.id = id;
    typeAndParams = new MethodTypeAndParams(params, types);
    setLineAndCol(id);
  }

  public MethodDeclaration(Identifier id, MethodTypeAndParams typeAndParams) {
    this.id = id;
    this.typeAndParams = typeAndParams;
    setLineAndCol(id);
  }

  public MethodTypeAndParams getTypeAndParams() {
    return typeAndParams;
  }

  public String getName() {
    return id.getName();
  }

  @Override
  public MethodDeclaration visitChildren(Visitor v) throws SemanticError {
    Identifier new_id = (Identifier) id.accept(v);

    boolean isDifferent = new_id != id;

    MethodTypeAndParams new_typeAndParams = (MethodTypeAndParams) typeAndParams.accept(v);
    isDifferent = isDifferent || new_typeAndParams != typeAndParams;

    if (isDifferent) {
      return new MethodDeclaration(new_id, new_typeAndParams);
    } else {
      return this;
    }
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();
    id.prettyPrint(cw);

    // Params
    typeAndParams.prettyPrint(cw);

    cw.endList();
  }
}

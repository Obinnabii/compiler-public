package apd67.ast;

import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.visitor.Visitor;

public class BoolType extends Type {
  String value = "bool";

  public BoolType() {
    super();
    ABIRepresentation = "b";
  }

  @Override
  public BoolType visitChildren(Visitor v) {
    return this;
  }

  public String toString() {
    return value;
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.printAtom(value);
  }
}

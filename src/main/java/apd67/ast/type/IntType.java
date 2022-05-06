package apd67.ast;

import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.visitor.Visitor;

public class IntType extends Type {
  String value = "int";

  public IntType() {
    super();
    ABIRepresentation = "i";
  }

  @Override
  public IntType visitChildren(Visitor v) {
    return this;
  }

  public String toString() {
    return value;
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.printAtom(value);
  }
}

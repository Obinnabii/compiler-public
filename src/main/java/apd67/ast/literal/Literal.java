package apd67.ast;

import apd67.util.cs4120.CodeWriterSExpPrinter;

public abstract class Literal extends Expr {

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.printAtom(this.toString());
  }
}

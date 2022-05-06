package apd67.ast;

import apd67.ir.*;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;

public class UscoreDecl extends Node implements Variable {
  public UscoreDecl() {}

  public String toString() {
    return "_";
  }

  @Override
  public Node typeCheck(TypeChecker tc) throws SemanticError {
    return this;
  }

  @Override
  public Node irTranslate(IRTranslator v) throws SemanticError {
    iRTranslation = new IRTemp("_dummy");
    return this;
  }

  @Override
  public Node visitChildren(Visitor v) throws SemanticError {
    return this;
  }

  @Override
  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.printAtom(this.toString());
  }

  @Override
  public boolean isUscore() {
    return true;
  }
}

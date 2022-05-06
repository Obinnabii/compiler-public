package apd67.ast;

import apd67.ir.IRConst;
import apd67.util.cs4120.*;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;

public class Bool extends Literal {
  private Boolean value;

  public Bool(Boolean b) {
    value = b;
    setType(new BoolType());
  }

  public Boolean getValue() {
    return value;
  }

  @Override
  public Bool visitChildren(Visitor v) throws SemanticError {
    return this;
  }

  @Override
  public Node irTranslate(IRTranslator v) throws SemanticError {
    iRTranslation = (value) ? new IRConst(1) : new IRConst(0);
    return this;
  }

  public String toString() {
    return Boolean.toString(value);
  }
}

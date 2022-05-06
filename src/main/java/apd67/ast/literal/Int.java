package apd67.ast;

import apd67.ir.IRConst;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;

public class Int extends Literal {
  Long value;

  public Int(long n) {
    value = n;
    setType(new IntType());
  }

  @Override
  public Int visitChildren(Visitor v) throws SemanticError {
    return this;
  }

  @Override
  public Node irTranslate(IRTranslator v) throws SemanticError {
    iRTranslation = new IRConst(value);
    return this;
  }

  public String toString() {
    return value == Long.MIN_VALUE ? value.toString().substring(1) : value.toString();
  }
}

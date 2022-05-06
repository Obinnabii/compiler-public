package apd67.ast;

import apd67.ir.IRConst;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;

public class Char extends Literal {
  String value;

  public Char(String s) {
    value = s;
    setType(new IntType());
  }

  @Override
  public Char visitChildren(Visitor v) throws SemanticError {
    return this;
  }

  @Override
  public Node irTranslate(IRTranslator v) throws TypeException {
    if (value.length() == 1) {
      iRTranslation = new IRConst((long) value.charAt(0));
    } else {
      char c;
      switch (value.charAt(1)) {
        case 'n':
          c = '\n';
          break;
        case '\'':
          c = '\'';
          break;
        case '\"':
          c = '\"';
          break;
        case 'f':
          c = '\f';
          break;
        case 't':
          c = '\t';
          break;
        case 'r':
          c = '\r';
          break;
        case 'b':
          c = '\b';
          break;
        default:
          c = value.charAt(1);
          break;
      }
      iRTranslation = new IRConst((long) c);
    }
    return this;
  }

  public String toString() {
    return "\'" + value + "\'";
  }
}

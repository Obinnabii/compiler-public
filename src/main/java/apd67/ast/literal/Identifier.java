package apd67.ast;

import apd67.ir.IRTemp;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;

public class Identifier extends Literal implements Variable {
  String name;

  public Identifier(String id) {
    name = id;
  }

  @Override
  public Identifier typeCheck(TypeChecker tc) throws SemanticError {
    Identifier id;
    try {
      id = (Identifier) this.clone();
    } catch (CloneNotSupportedException cnse) {
      cnse.printStackTrace();
      throw new Error(cnse.toString());
    }

    try {
      id.setType(tc.context.lookup(name));
    } catch (ContextException ce) {
      throw new TypeException("Unbound identifier: " + name, line, col);
    }
    return id;
  }

  @Override
  public Node irTranslate(IRTranslator v) throws TypeException {
    iRTranslation = new IRTemp(name);
    return this;
  }

  @Override
  public Identifier visitChildren(Visitor v) throws SemanticError {
    return this;
  }

  public String toString() {
    return name;
  }

  public String getName() {
    return name;
  }
}

package apd67.ast;

import apd67.ir.*;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;

// Set line and col

public class While extends Stmt {
  private Expr guard;
  private Stmt body;

  public While(Expr guard, Stmt body) {
    this.guard = guard;
    this.body = body;
  }

  @Override
  public While irTranslate(IRTranslator v) throws SemanticError {
    IRLabel beglbl = v.makeGenericLabel();
    IRLabel looplbl = v.makeGenericLabel();
    IRLabel endlbl = v.makeGenericLabel();
    IRName begName = new IRName(beglbl.name());
    IRName loopName = new IRName(looplbl.name());
    IRName endName = new IRName(endlbl.name());
    IRStmt guardTranslation = v.CTranslate(guard, loopName, endName);

    iRTranslation =
        new IRSeq(
            beglbl,
            guardTranslation,
            looplbl,
            (IRStmt) body.getIRTranslation(),
            new IRJump(begName),
            endlbl);

    return this;
  }

  @Override
  public While typeCheck(TypeChecker tc) throws SemanticError {
    While w;
    try {
      w = (While) this.clone();
    } catch (CloneNotSupportedException cnse) {
      throw new Error(cnse.toString());
    }

    if (!(guard.getType().expectedType() instanceof BoolType)) {
      throw new TypeException("Guard of while loop must be type bool", line, col);
    }

    // unit
    w.setType(new StatementType(true));

    return w;
  }

  @Override
  public While visitChildren(Visitor v) throws SemanticError {
    Expr new_guard = (Expr) guard.accept(v);
    if (v instanceof TypeChecker) {
      ((TypeChecker) v).context.enterScope();
    }
    Stmt new_body = (Stmt) body.accept(v);
    if (v instanceof TypeChecker) {
      ((TypeChecker) v).context.leaveScope();
    }

    if (new_guard != guard || new_body != body) {
      return (While) new While(new_guard, new_body).setLineAndCol(this);
    } else {
      return this;
    }
  }

  public String toString() {
    return "while (" + guard + ") " + body;
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();
    cw.printAtom("while");

    guard.prettyPrint(cw);
    body.prettyPrint(cw);

    cw.endList();
  }
}

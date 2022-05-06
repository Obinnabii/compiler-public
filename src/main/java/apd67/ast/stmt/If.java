package apd67.ast;

import apd67.ir.*;
import apd67.ir.IRJump;
import apd67.ir.IRLabel;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;
import java.util.Optional;

public class If extends Stmt {
  private Expr guard;
  private Stmt truthy;
  private Optional<Stmt> falsy;

  public If(Expr guard, Stmt truthy, Stmt falsy) {
    this.guard = guard;
    this.truthy = truthy;
    this.falsy = Optional.ofNullable(falsy);
  }

  @Override
  public If irTranslate(IRTranslator v) throws SemanticError {
    IRLabel truelbl = v.makeTrueLabel();
    IRLabel falselbl = v.makeFalseLabel();
    IRLabel endlbl = v.makeGenericLabel();
    IRName trueName = new IRName(truelbl.name());
    IRName falseName = new IRName(falselbl.name());
    IRName endName = new IRName(endlbl.name());
    IRStmt guardTranslation = v.CTranslate(guard, trueName, falseName);

    iRTranslation =
        falsy.isPresent()
            ? ((StatementType) truthy.getType()).isUnit()
                ? new IRSeq(
                    guardTranslation,
                    truelbl,
                    (IRStmt) truthy.getIRTranslation(),
                    new IRJump(endName),
                    falselbl,
                    (IRStmt) falsy.get().getIRTranslation(),
                    endlbl)
                : new IRSeq(
                    guardTranslation,
                    truelbl,
                    (IRStmt) truthy.getIRTranslation(),
                    falselbl,
                    (IRStmt) falsy.get().getIRTranslation())
            : new IRSeq(guardTranslation, truelbl, (IRStmt) truthy.getIRTranslation(), falselbl);

    return this;
  }

  @Override
  public If typeCheck(TypeChecker tc) throws SemanticError {
    If ifNode;
    try {
      ifNode = (If) this.clone();
    } catch (CloneNotSupportedException cnse) {
      throw new Error(cnse.toString());
    }

    if (!(guard.getType().expectedType() instanceof BoolType)) {
      throw new TypeException("Guard of if must be bool", line, col);
    }

    if (falsy.isPresent()) {
      boolean truthyIsUnit = ((StatementType) truthy.getType()).isUnit();
      boolean falsyIsUnit = ((StatementType) falsy.get().getType()).isUnit();
      // only void if neither truthy nor falsy branch are unit
      ifNode.setType(new StatementType(truthyIsUnit || falsyIsUnit));
    } else {
      // no else condition, unit
      ifNode.setType(new StatementType(true));
    }

    return ifNode;
  }

  @Override
  public If visitChildren(Visitor v) throws SemanticError {
    Expr new_guard = (Expr) guard.accept(v);

    if (v instanceof TypeChecker) {
      ((TypeChecker) v).context.enterScope();
    }
    Stmt new_truthy = (Stmt) truthy.accept(v);
    if (v instanceof TypeChecker) {
      ((TypeChecker) v).context.leaveScope();
    }

    if (falsy.isPresent()) {
      if (v instanceof TypeChecker) {
        ((TypeChecker) v).context.enterScope();
      }
      Stmt new_falsy = (Stmt) falsy.get().accept(v);
      if (v instanceof TypeChecker) {
        ((TypeChecker) v).context.leaveScope();
      }

      if (new_guard != guard || new_truthy != truthy || new_falsy != falsy.get()) {
        return (If) new If(new_guard, new_truthy, new_falsy).setLineAndCol(this);
      }
    }

    if (new_guard != guard || new_truthy != truthy) {
      return (If) new If(new_guard, new_truthy, null).setLineAndCol(this);
    } else {
      return this;
    }
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();
    cw.printAtom("if");

    guard.prettyPrint(cw);
    truthy.prettyPrint(cw);
    if (falsy.isPresent()) {
      falsy.get().prettyPrint(cw);
    }

    cw.endList();
  }
}

package apd67.ast;

import apd67.ir.*;
import apd67.ir.IRSeq;
import apd67.ir.IRStmt;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;
import java.util.LinkedList;
import java.util.ListIterator;

public class Block extends Stmt {
  LinkedList<Stmt> stmts;

  public Block(LinkedList<Stmt> stmts) {
    this.stmts = stmts;
  }

  @Override
  public Block typeCheck(TypeChecker tc) throws SemanticError {
    Block blk;
    try {
      blk = (Block) this.clone();
    } catch (CloneNotSupportedException cnse) {
      cnse.printStackTrace();
      throw new Error(cnse.toString());
    }

    if (stmts.size() == 0) {
      // empty block has type unit
      blk.setType(new StatementType(true));
      return blk;
    }

    ListIterator<Stmt> it = stmts.listIterator();
    while (it.nextIndex() < stmts.size() - 1) {
      Stmt s = it.next();
      if (!(s.getType() instanceof StatementType && ((StatementType) s.getType()).isUnit())) {
        throw new TypeException(
            "Blocks may only contain statements and can only end in returns", line, col);
      }
    }

    // R of block is R of last statement
    blk.setType(new StatementType(((StatementType) stmts.getLast().getType()).isUnit()));

    return blk;
  }

  @Override
  public Block irTranslate(IRTranslator v) throws SemanticError {
    LinkedList<IRStmt> irStmts = new LinkedList<IRStmt>();

    for (Stmt stmt : stmts) {
      IRNode stmtIR = stmt.getIRTranslation();
      if (stmtIR instanceof IRTemp) stmtIR = new IRExp((IRExpr) stmtIR);
      irStmts.add((IRStmt) stmtIR);
    }

    iRTranslation = new IRSeq(irStmts);
    return this;
  }

  @Override
  public Block visitChildren(Visitor v) throws SemanticError {
    boolean isDifferent = false;

    LinkedList<Stmt> new_stmts = new LinkedList<>();
    for (Stmt s : stmts) {
      Stmt s_new = (Stmt) s.accept(v);
      new_stmts.addLast(s_new);
      isDifferent |= s_new != s;
    }

    if (isDifferent) {
      return (Block) new Block(new_stmts).setLineAndCol(this);
    } else {
      return this;
    }
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();

    for (Stmt stmt : stmts) {
      stmt.prettyPrint(cw);
    }

    cw.endList();
  }
}

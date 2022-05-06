package apd67.ast;

import apd67.ir.IRBinOp;
import apd67.ir.IRCall;
import apd67.ir.IRConst;
import apd67.ir.IRESeq;
import apd67.ir.IRExpr;
import apd67.ir.IRMem;
import apd67.ir.IRMove;
import apd67.ir.IRName;
import apd67.ir.IRSeq;
import apd67.ir.IRStmt;
import apd67.ir.IRTemp;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;
import java.util.LinkedList;

public class ArrayLit extends Literal {
  LinkedList<Expr> values;
  private boolean isEmpty = false;
  private static int uniq = 0;

  /** Set Line and Column when you change this class! */
  public ArrayLit(LinkedList<Expr> values) {
    this.values = values;
  }

  public ArrayLit(LinkedList<Expr> values, boolean isEmpty) {
    this.values = values;
  }

  @Override
  public ArrayLit typeCheck(TypeChecker tc) throws SemanticError {
    Type t;

    if (values.size() == 0) {
      this.isEmpty = true;
      t = new IntType(); // doesn't matter, won't be checked
      // throw new Error("Never implemented empty arrays :(");
    } else {
      t = values.getFirst().getType().expectedType();

      for (Expr e : values) {
        if (!(t.expectedEquals(e.getType().expectedType()))) {
          throw new TypeException("All elements in an array must be of the same type", line, col);
        }
      }
    }

    try {
      ArrayLit al = (ArrayLit) this.clone();
      al.setType(new ArrayType(t, null, isEmpty));
      return al;
    } catch (CloneNotSupportedException cnse) {
      cnse.printStackTrace();
      throw new Error(cnse.toString());
    }
  }

  @Override
  public ArrayLit visitChildren(Visitor v) throws SemanticError {
    boolean isDifferent = false;

    LinkedList<Expr> new_values = new LinkedList<>();
    for (Expr e : values) {
      Expr e_new = (Expr) e.accept(v);
      new_values.addLast(e_new);
      isDifferent = isDifferent || e_new != e;
    }

    if (isDifferent) {
      return (ArrayLit) new ArrayLit(new_values).setLineAndCol(this);
    } else {
      return this;
    }
  }
  // gives the list of IR stmts associated with placing
  // each expression in memory
  private LinkedList<IRStmt> genStmtLst(IRTemp tm, long n) {
    LinkedList<IRStmt> res = new LinkedList<IRStmt>();
    int offset = 8;
    res.add(
        new IRMove(
            tm,
            new IRCall(
                new IRName("_xi_alloc"),
                new IRBinOp(
                    IRBinOp.OpType.ADD,
                    new IRBinOp(IRBinOp.OpType.MUL, new IRConst(n), new IRConst(8)),
                    new IRConst(8)))));
    for (Expr e : values) {
      res.add(
          new IRMove(
              new IRMem(new IRBinOp(IRBinOp.OpType.ADD, tm, new IRConst(offset))),
              (IRExpr) e.getIRTranslation()));
      offset += 8;
    }
    res.add(new IRMove(new IRMem(tm), new IRConst(n)));
    return res;
  }

  @Override
  public Node irTranslate(IRTranslator v) throws TypeException {
    long n = values.size();
    IRTemp tm = new IRTemp("_m_lit_" + uniq++);
    LinkedList<IRStmt> memInit = genStmtLst(tm, n);
    iRTranslation =
        new IRESeq(new IRSeq(memInit), new IRBinOp(IRBinOp.OpType.ADD, tm, new IRConst(8)));
    return this;
  }

  public boolean isEmpty() {
    return isEmpty;
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();

    for (Expr value : values) {
      value.prettyPrint(cw);
    }

    cw.endList();
  }
}

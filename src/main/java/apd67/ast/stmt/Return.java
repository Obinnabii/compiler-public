package apd67.ast;

import apd67.ir.*;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;
import java.util.LinkedList;
import java.util.ListIterator;

public class Return extends Stmt {
  private LinkedList<Expr> exprs;

  public Return(LinkedList<Expr> exprs) {
    this.exprs = exprs;
  }

  public Return() {
    this(new LinkedList<Expr>());
  }

  @Override
  public Return typeCheck(TypeChecker tc) throws SemanticError {
    Return r;
    try {
      r = (Return) this.clone();
    } catch (CloneNotSupportedException cnse) {
      throw new Error(cnse.toString());
    }

    LinkedList<Type> currentReturnType;
    try {
      currentReturnType = tc.context.getCurrentReturnType();
    } catch (ContextException ce) {
      throw new TypeException(ce.toString(), line, col);
    }

    if (currentReturnType.size() != exprs.size()) {
      String msg =
          "Return wrong number of args (expected "
              + currentReturnType.size()
              + ", found "
              + exprs.size()
              + ")";
      throw new TypeException(msg, line, col);
    }

    ListIterator<Expr> itExpr = exprs.listIterator();
    ListIterator<Type> itType = currentReturnType.listIterator();

    // check every type return matches what expected
    while (itExpr.hasNext()) {
      Type t1 = itExpr.next().getType().expectedType();
      Type t2 = itType.next().expectedType();

      if (!t1.expectedEquals(t2)) {
        throw new TypeException("Expected " + t1 + " but found " + t2, line, col);
      }
    }

    // void type
    r.setType(new StatementType(false));

    return r;
  }

  @Override
  public Return irTranslate(IRTranslator v) throws SemanticError {
    LinkedList<IRExpr> irExprs = new LinkedList<IRExpr>();

    for (Expr expr : exprs) {
      irExprs.add((IRExpr) expr.getIRTranslation());
    }

    iRTranslation = new IRReturn(irExprs);
    return this;
  }

  @Override
  public Return visitChildren(Visitor v) throws SemanticError {
    boolean isDifferent = false;

    LinkedList<Expr> new_exprs = new LinkedList<>();
    for (Expr s : exprs) {
      Expr s_new = (Expr) s.accept(v);
      new_exprs.addLast(s_new);
      isDifferent |= s_new != s;
    }

    if (isDifferent) {
      return (Return) new Return(new_exprs).setLineAndCol(this);
    } else {
      return this;
    }
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();
    cw.printAtom("return");

    for (Expr expr : exprs) {
      expr.prettyPrint(cw);
    }

    cw.endList();
  }
}

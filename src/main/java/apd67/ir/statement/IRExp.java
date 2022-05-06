package apd67.ir;

import apd67.ir.visit.AggregateVisitor;
import apd67.ir.visit.CheckCanonicalIRVisitor;
import apd67.ir.visit.IRVisitor;
import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import apd67.util.cs4120.SExpPrinter;

/**
 * An intermediate representation for evaluating an expression for side effects, discarding the
 * result EXP(e)
 */
public class IRExp extends IRStmt {
  private IRExpr expr;

  /** @param expr the expression to be evaluated and result discarded */
  public IRExp(IRExpr expr) {
    this.expr = expr;
  }

  public IRExpr expr() {
    return expr;
  }

  @Override
  public String label() {
    return "EXP";
  }

  @Override
  public IRNode visitChildren(IRVisitor v) {
    IRExpr expr = (IRExpr) v.visit(this, this.expr);

    if (expr != this.expr) return v.nodeFactory().IRExp(expr);

    return this;
  }

  @Override
  public TranslationResult lower(LowerVisitor v) {
    TranslationResult tr = expr.getTranslation();
    return new TranslationResult(tr.getStmts());
  }

  @Override
  public <T> T aggregateChildren(AggregateVisitor<T> v) {
    T result = v.unit();
    result = v.bind(result, v.visit(expr));
    return result;
  }

  @Override
  public CheckCanonicalIRVisitor checkCanonicalEnter(CheckCanonicalIRVisitor v) {
    return v.enterExp();
  }

  @Override
  public void printSExp(SExpPrinter p) {
    p.startList();
    p.printAtom("EXP");
    expr.printSExp(p);
    p.endList();
  }
}

package apd67.ir;

import apd67.ir.visit.AggregateVisitor;
import apd67.ir.visit.CheckCanonicalIRVisitor;
import apd67.ir.visit.IRVisitor;
import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import apd67.util.cs4120.SExpPrinter;
import java.util.LinkedList;

/**
 * An intermediate representation for an expression evaluated under side effects ESEQ(stmt, expr)
 */
public class IRESeq extends IRExpr_c {
  private IRStmt stmt;
  private IRExpr expr;

  /**
   * @param stmt IR statement to be evaluated for side effects
   * @param expr IR expression to be evaluated after {@code stmt}
   */
  public IRESeq(IRStmt stmt, IRExpr expr) {
    this.stmt = stmt;
    this.expr = expr;
  }

  public IRStmt stmt() {
    return stmt;
  }

  public IRExpr expr() {
    return expr;
  }

  @Override
  public String label() {
    return "ESEQ";
  }

  @Override
  public IRNode visitChildren(IRVisitor v) {
    IRStmt stmt = (IRStmt) v.visit(this, this.stmt);
    IRExpr expr = (IRExpr) v.visit(this, this.expr);

    if (expr != this.expr || stmt != this.stmt) return v.nodeFactory().IRESeq(stmt, expr);

    return this;
  }

  @Override
  public TranslationResult lower(LowerVisitor v) {
    TranslationResult trS = stmt.getTranslation();
    TranslationResult trE = expr.getTranslation();

    LinkedList<IRStmt> stmts1 = trS.getStmts();
    LinkedList<IRStmt> stmts2 = trE.getStmts();
    IRExpr exprPrime = trE.getExpr();

    stmts1.addAll(stmts2);
    return new TranslationResult(stmts1, exprPrime);
  }

  @Override
  public <T> T aggregateChildren(AggregateVisitor<T> v) {
    T result = v.unit();
    result = v.bind(result, v.visit(stmt));
    result = v.bind(result, v.visit(expr));
    return result;
  }

  @Override
  public boolean isCanonical(CheckCanonicalIRVisitor v) {
    return false;
  }

  @Override
  public void printSExp(SExpPrinter p) {
    p.startList();
    p.printAtom("ESEQ");
    stmt.printSExp(p);
    expr.printSExp(p);
    p.endList();
  }
}

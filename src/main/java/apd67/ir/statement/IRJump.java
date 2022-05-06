package apd67.ir;

import apd67.ir.visit.AggregateVisitor;
import apd67.ir.visit.IRVisitor;
import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import apd67.util.Pair;
import apd67.util.cs4120.SExpPrinter;
import java.util.LinkedList;

/** An intermediate representation for a transfer of control */
public class IRJump extends IRStmt {
  private IRExpr target;

  /** @param expr the destination of the jump */
  public IRJump(IRExpr expr) {
    target = expr;
  }

  public IRExpr target() {
    return target;
  }

  @Override
  public String label() {
    return "JUMP";
  }

  @Override
  public IRNode visitChildren(IRVisitor v) {
    IRExpr expr = (IRExpr) v.visit(this, target);

    if (expr != target) return v.nodeFactory().IRJump(expr);

    return this;
  }

  @Override
  public TranslationResult lower(LowerVisitor v) {
    TranslationResult tr = target.getTranslation();
    LinkedList<IRStmt> stmts = tr.getStmts();
    IRStmt jump = new IRJump(tr.getExpr());
    stmts.add(jump);
    return new TranslationResult(stmts);
  }

  @Override
  public <T> T aggregateChildren(AggregateVisitor<T> v) {
    T result = v.unit();
    result = v.bind(result, v.visit(target));
    return result;
  }

  @Override
  public void printSExp(SExpPrinter p) {
    p.startList();
    p.printAtom("JUMP");
    target.printSExp(p);
    p.endList();
  }

  @Override
  public boolean propagateCopy(Pair<IRTemp, IRTemp> copyPair) {
    boolean changed = false;
    IRTemp copiedVar = copyPair.first();
    IRTemp original = copyPair.second();

    if (target instanceof IRTemp) {
      if (target.equals(copiedVar)) {
        changed = true;
        target = original;
      }
    } else {
      changed = changed || target.propagateCopy(copyPair);
    }

    return changed;
  }
}

package apd67.ir;

import apd67.ir.visit.AggregateVisitor;
import apd67.ir.visit.CheckCanonicalIRVisitor;
import apd67.ir.visit.IRVisitor;
import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import apd67.util.Pair;
import apd67.util.cs4120.SExpPrinter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * An intermediate representation for a conditional transfer of control CJUMP(expr, trueLabel,
 * falseLabel)
 */
public class IRCJump extends IRStmt {
  private IRExpr cond;
  private String trueLabel, falseLabel;

  /**
   * Construct a CJUMP instruction with fall-through on false.
   *
   * @param cond the condition for the jump
   * @param trueLabel the destination of the jump if {@code expr} evaluates to true
   */
  public IRCJump(IRExpr cond, String trueLabel) {
    this(cond, trueLabel, null);
  }

  /**
   * @param cond the condition for the jump
   * @param trueLabel the destination of the jump if {@code expr} evaluates to true
   * @param falseLabel the destination of the jump if {@code expr} evaluates to false
   */
  public IRCJump(IRExpr cond, String trueLabel, String falseLabel) {
    this.cond = cond;
    this.trueLabel = trueLabel;
    this.falseLabel = falseLabel;
  }

  public IRExpr cond() {
    return cond;
  }

  public String trueLabel() {
    return trueLabel;
  }

  public String falseLabel() {
    return falseLabel;
  }

  public boolean hasFalseLabel() {
    return falseLabel != null;
  }

  @Override
  public String label() {
    return "CJUMP";
  }

  @Override
  public IRNode visitChildren(IRVisitor v) {
    IRExpr expr = (IRExpr) v.visit(this, this.cond);

    if (expr != this.cond) return v.nodeFactory().IRCJump(expr, trueLabel, falseLabel);

    return this;
  }

  @Override
  public TranslationResult lower(LowerVisitor v) {
    TranslationResult tr = cond.getTranslation();
    IRCJump cjump;
    try {
      cjump = (IRCJump) this.clone();
    } catch (CloneNotSupportedException cnse) {
      throw new Error("Java clone() weirdness");
    }
    cjump.cond = tr.getExpr();
    LinkedList<IRStmt> stmts = tr.getStmts();
    stmts.addLast(cjump);
    return new TranslationResult(stmts);
  }

  @Override
  public <T> T aggregateChildren(AggregateVisitor<T> v) {
    T result = v.unit();
    result = v.bind(result, v.visit(cond));
    return result;
  }

  @Override
  public boolean isCanonical(CheckCanonicalIRVisitor v) {
    return !hasFalseLabel();
  }

  @Override
  public void printSExp(SExpPrinter p) {
    p.startList();
    p.printAtom("CJUMP");
    cond.printSExp(p);
    p.printAtom(trueLabel);
    if (hasFalseLabel()) p.printAtom(falseLabel);
    p.endList();
  }

  @Override
  public void replaceAvailableExprs(
      HashMap<IRExpr, IRTemp> cache,
      HashSet<IRExpr> inexprs,
      HashSet<IRExpr> outexprs,
      ArrayList<IRStmt> newBody) {
    cond = cond.replaceAvailableExprs(cache, inexprs, outexprs, newBody);
  }

  @Override
  public boolean propagateCopy(Pair<IRTemp, IRTemp> copyPair) {
    boolean changed = false;
    IRTemp copiedVar = copyPair.first();
    IRTemp original = copyPair.second();

    if (cond instanceof IRTemp) {
      if (cond.equals(copiedVar)) {
        changed = true;
        cond = original;
      }
    } else {
      changed = changed || cond.propagateCopy(copyPair);
    }

    return changed;
  }
}

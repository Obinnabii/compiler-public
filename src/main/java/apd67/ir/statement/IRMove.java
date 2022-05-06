package apd67.ir;

import apd67.ir.visit.AggregateVisitor;
import apd67.ir.visit.IRVisitor;
import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import apd67.util.Pair;
import apd67.util.cs4120.SExpPrinter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/** An intermediate representation for a move statement MOVE(target, expr) */
public class IRMove extends IRStmt {
  private IRExpr target;
  private IRExpr src;

  /**
   * @param target the destination of this move
   * @param src the expression whose value is to be moved
   */
  public IRMove(IRExpr target, IRExpr src) {
    this.target = target;
    this.src = src;
  }

  public IRExpr target() {
    return target;
  }

  public IRExpr source() {
    return src;
  }

  public void setSource(IRExpr source) {
    src = source;
  }

  @Override
  public String label() {
    return "MOVE";
  }

  @Override
  public IRNode visitChildren(IRVisitor v) {
    IRExpr target = (IRExpr) v.visit(this, this.target);
    IRExpr expr = (IRExpr) v.visit(this, src);

    if (target != this.target || expr != src) return v.nodeFactory().IRMove(target, expr);

    return this;
  }

  @Override
  public TranslationResult lower(LowerVisitor v) {
    TranslationResult trSrc = src.getTranslation();

    IRExpr eSrc = trSrc.getExpr();
    LinkedList<IRStmt> stmtsSrc = trSrc.getStmts();

    if (target instanceof IRTemp) {
      stmtsSrc.add(new IRMove(target, eSrc));
      return new TranslationResult(stmtsSrc);
    } else if (!(target instanceof IRMem)) {
      throw new Error("Move w/ target not a mem or temp");
    }

    IRMem target = (IRMem) this.target;
    TranslationResult trExp = target.expr().getTranslation();
    IRExpr eExp = trExp.getExpr();
    LinkedList<IRStmt> stmtsExp = trExp.getStmts();

    boolean commutable = false; // add function to compute this

    if (commutable) {
      stmtsExp.addAll(stmtsSrc);
      stmtsExp.add(new IRMove(target.getTranslation().getExpr(), eSrc));
      return new TranslationResult(stmtsExp);
    } else {
      IRTemp t = new IRTemp(v.nextTempName());
      stmtsExp.add(new IRMove(t, eExp));

      stmtsExp.addAll(stmtsSrc);

      stmtsExp.add(new IRMove(new IRMem(t), eSrc));
      return new TranslationResult(stmtsExp);
    }
  }

  @Override
  public <T> T aggregateChildren(AggregateVisitor<T> v) {
    T result = v.unit();
    result = v.bind(result, v.visit(target));
    result = v.bind(result, v.visit(src));
    return result;
  }

  @Override
  public void printSExp(SExpPrinter p) {
    p.startList();
    p.printAtom("MOVE");
    // if (target == null) System.out.println("target is null");
    target.printSExp(p);
    src.printSExp(p);
    p.endList();
  }

  @Override
  public void replaceAvailableExprs(
      HashMap<IRExpr, IRTemp> cache,
      HashSet<IRExpr> inexprs,
      HashSet<IRExpr> outexprs,
      ArrayList<IRStmt> newBody) {
    if (target instanceof IRMem) {
      IRMem mem = (IRMem) target;
      IRExpr newExpr = mem.expr().replaceAvailableExprs(cache, inexprs, outexprs, newBody);
      IRMem newTarget = new IRMem(newExpr);
    } else {
      target = target.replaceAvailableExprs(cache, inexprs, outexprs, newBody);
    }
    src = src.replaceAvailableExprs(cache, inexprs, outexprs, newBody);
  }

  @Override
  public boolean propagateCopy(Pair<IRTemp, IRTemp> copyPair) {
    boolean changed = false;
    IRTemp copiedVar = copyPair.first();
    IRTemp original = copyPair.second();

    if (src instanceof IRTemp) {
      if (src.equals(copiedVar)) {
        changed = true;
        src = original;
      }
    } else {
      changed = changed || src.propagateCopy(copyPair);
    }

    changed = changed || target.propagateCopy(copyPair);

    return changed;
  }
}

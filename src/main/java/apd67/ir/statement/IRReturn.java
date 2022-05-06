package apd67.ir;

import apd67.ir.visit.AggregateVisitor;
import apd67.ir.visit.IRVisitor;
import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import apd67.util.Pair;
import apd67.util.cs4120.SExpPrinter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/** RETURN statement */
public class IRReturn extends IRStmt {
  protected List<IRExpr> rets;

  public IRReturn() {
    this(new ArrayList<>());
  }

  /** @param rets values to return */
  public IRReturn(IRExpr... rets) {
    this(Arrays.asList(rets));
  }

  /** @param rets values to return */
  public IRReturn(List<IRExpr> rets) {
    this.rets = rets;
  }

  public List<IRExpr> rets() {
    return rets;
  }

  @Override
  public String label() {
    return "RETURN";
  }

  @Override
  public IRNode visitChildren(IRVisitor v) {
    boolean modified = false;

    List<IRExpr> results = new ArrayList<>(rets.size());

    for (IRExpr ret : rets) {
      IRExpr newExpr = (IRExpr) v.visit(this, ret);
      if (newExpr != ret) modified = true;
      results.add(newExpr);
    }

    if (modified) return v.nodeFactory().IRReturn(results);

    return this;
  }

  @Override
  public TranslationResult lower(LowerVisitor v) {
    LinkedList<IRStmt> stmtsLow = new LinkedList<>();
    LinkedList<IRExpr> tempRets = new LinkedList<>();

    for (IRExpr e : rets) {
      TranslationResult tr = e.getTranslation();

      IRTemp t = new IRTemp(v.nextTempName());
      tempRets.add(t);

      stmtsLow.addAll(tr.getStmts());
      stmtsLow.add(new IRMove(t, tr.getExpr()));
    }

    stmtsLow.add(new IRReturn(tempRets));
    return new TranslationResult(stmtsLow);
  }

  @Override
  public <T> T aggregateChildren(AggregateVisitor<T> v) {
    T result = v.unit();
    for (IRExpr ret : rets) result = v.bind(result, v.visit(ret));
    return result;
  }

  @Override
  public void printSExp(SExpPrinter p) {
    p.startList();
    p.printAtom("RETURN");
    for (IRExpr ret : rets) ret.printSExp(p);
    p.endList();
  }

  @Override
  public void replaceAvailableExprs(
      HashMap<IRExpr, IRTemp> cache,
      HashSet<IRExpr> inexprs,
      HashSet<IRExpr> outexprs,
      ArrayList<IRStmt> newBody) {
    ArrayList<IRExpr> newRets = new ArrayList<>(rets.size());
    for (IRExpr ret : rets) {
      newRets.add(ret.replaceAvailableExprs(cache, inexprs, outexprs, newBody));
    }
    rets = newRets;
  }

  @Override
  public boolean propagateCopy(Pair<IRTemp, IRTemp> copyPair) {
    boolean changed = false;
    IRTemp copiedVar = copyPair.first();
    IRTemp original = copyPair.second();
    ArrayList<IRExpr> res = new ArrayList();

    for (IRExpr expr : rets) {
      if (expr instanceof IRTemp) {
        if (expr.equals(copiedVar)) {
          changed = true;
          expr = original;
        }
      } else {
        changed = changed || expr.propagateCopy(copyPair);
      }
      res.add(expr);
    }
    rets = res;

    return changed;
  }
}

package apd67.ir;

import apd67.ir.visit.AggregateVisitor;
import apd67.ir.visit.IRVisitor;
import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import apd67.util.Pair;
import apd67.util.cs4120.SExpPrinter;
import apd67.util.ir.InternalCompilerError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/** An intermediate representation for a memory location MEM(e) */
public class IRMem extends IRExpr_c {
  public enum MemType {
    NORMAL,
    IMMUTABLE;

    @Override
    public String toString() {
      switch (this) {
        case NORMAL:
          return "MEM";
        case IMMUTABLE:
          return "MEM_I";
      }
      throw new InternalCompilerError("Unknown mem type!");
    }
  };

  private IRExpr expr;
  private MemType memType;

  /** @param expr the address of this memory location */
  public IRMem(IRExpr expr) {
    this(expr, MemType.NORMAL);
  }

  public IRMem(IRExpr expr, MemType memType) {
    this.expr = expr;
    this.memType = memType;
  }

  public IRExpr expr() {
    return expr;
  }

  public MemType memType() {
    return memType;
  }

  @Override
  public String label() {
    return memType.toString();
  }

  @Override
  public IRNode visitChildren(IRVisitor v) {
    IRExpr expr = (IRExpr) v.visit(this, this.expr);

    if (expr != this.expr) return v.nodeFactory().IRMem(expr);

    return this;
  }

  @Override
  public TranslationResult lower(LowerVisitor v) {
    TranslationResult tr = expr.getTranslation();
    IRMem mem;
    try {
      mem = (IRMem) this.clone();
    } catch (CloneNotSupportedException cnse) {
      throw new Error("Java clone() weirdness");
    }
    mem.expr = tr.getExpr();
    IRExpr new_expr = mem;
    LinkedList<IRStmt> new_stmts = tr.getStmts();
    return new TranslationResult(new_stmts, new_expr);
  }

  @Override
  public <T> T aggregateChildren(AggregateVisitor<T> v) {
    T result = v.unit();
    result = v.bind(result, v.visit(expr));
    return result;
  }

  @Override
  public void printSExp(SExpPrinter p) {
    p.startList();
    p.printAtom(memType.toString());
    expr.printSExp(p);
    p.endList();
  }

  @Override
  public HashSet<IRExpr> subExprs() {
    if (subExprs == null) {
      subExprs = new HashSet<IRExpr>();
      subExprs.add(this);
      subExprs.addAll(expr.subExprs());
    }
    return subExprs;
  }

  @Override
  public boolean hasMem() {
    return true;
  }

  @Override
  public boolean contains(IRTemp x) {
    return expr.contains(x);
  }

  @Override
  public IRExpr replaceAvailableExprs(
      HashMap<IRExpr, IRTemp> cache,
      HashSet<IRExpr> inexprs,
      HashSet<IRExpr> outexprs,
      ArrayList<IRStmt> newBody) {
    IRTemp t = cache.get(this);
    boolean inIn = inexprs.contains(this);
    if (inIn) {
      // [e] where cached: [e] -> t
      if (t == null) throw new Error("Impossible: avail expr should already be cached if in");
      return t;
    } else if (outexprs.contains(this)) { // cache does not have [e]
      if (t != null) { // cache does have [e], so it was previously killed
        newBody.add(new IRMove(t, this));

        IRMem repl = new IRMem(expr.replaceAvailableExprs(cache, inexprs, outexprs, newBody));
        if (!this.equals(repl)) return repl;
        return t;
      } else { // cache does not have [e]
        IRTemp tnew = new IRTemp();
        cache.put(this, tnew);
        newBody.add(new IRMove(tnew, this));

        IRMem repl = new IRMem(expr.replaceAvailableExprs(cache, inexprs, outexprs, newBody));
        if (!this.equals(repl)) return repl;
        return tnew;
      }

      // IRTemp tsub = new IRTemp();
      // cache.put(expr, tsub);
      // newBody.add(new IRMove(tsub, expr));
      // expr.replaceAvailableExprs(cache, inexprs, outexprs, newBody);
    } else {
      IRMem repl = new IRMem(expr.replaceAvailableExprs(cache, inexprs, outexprs, newBody));
      if (!this.equals(repl)) return repl;
      return this;
    }
  }

  @Override
  public boolean propagateCopy(Pair<IRTemp, IRTemp> copyPair) {
    boolean changed = false;
    IRTemp copiedVar = copyPair.first();
    IRTemp original = copyPair.second();

    if (expr instanceof IRTemp) {
      if (expr.equals(copiedVar)) {
        changed = true;
        expr = original;
      }
    } else {
      changed = changed || expr.propagateCopy(copyPair);
    }

    return changed;
  }
}

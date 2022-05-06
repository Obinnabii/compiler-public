package apd67.ir.cfg.dfa;

import apd67.ir.*;
import apd67.ir.IRStmt;
import apd67.ir.IRTemp;
import apd67.ir.cfg.CFG;
import apd67.ir.cfg.IRBlock;
import java.util.HashSet;
import java.util.List;

public class AE extends DFA<IRExpr> {

  public AE(CFG cfg) {
    super(cfg);

    // assign top element
    HashSet<IRExpr> allExprs = new HashSet<>();
    for (IRBlock b : blocks) {
      for (int i = 0; i < b.cfgLength(); i++) {
        allExprs.addAll(exprs(b.getStmt(i)));
      }
    }
    inNew.set(0, allExprs);
  }

  @Override
  protected HashSet<IRExpr> in(IRStmt stmt, List<Integer> preds) {
    // intersection all preds
    HashSet<IRExpr> intersect = new HashSet();
    if (preds.size() > 0) intersect.addAll(outNew.get(preds.get(0)));

    for (Integer i : preds) {
      intersect.retainAll(outNew.get(i));
    }

    return intersect;
  }

  @Override
  protected HashSet<IRExpr> out(IRStmt stmt, List<Integer> succs) {
    HashSet<IRExpr> inn = inNew.get(stmt.getIndex());
    HashSet<IRExpr> e = exprs(stmt);
    e.addAll(inn); // union exprs[n] U in(n)
    return kill(stmt, e); // set diff (in[n] U exprs(n)) - kill(n)
  }

  public HashSet<IRExpr> exprs(IRStmt n) {
    if (n instanceof IRCallStmt) { // f(e)
      HashSet<IRExpr> exprs = new HashSet<>();

      for (IRExpr e : ((IRCallStmt) n).args()) {
        exprs.addAll(e.subExprs());
      }

      return exprs;
    } else if (n instanceof IRMove) {
      // assignment
      IRMove m = (IRMove) n;

      if (m.target() instanceof IRMem) { // [e1] <- e2
        HashSet<IRExpr> res = m.target().subExprs();
        res.addAll(m.source().subExprs());
        return res;
      } else if (m.target() instanceof IRTemp) { // x <- e
        return m.source().subExprs();
      } else {
        throw new Error("impossible, source must be temp or mem");
      }
    } else if (n instanceof IRCJump) { // if e
      return ((IRCJump) n).cond().subExprs();
    } else {
      return new HashSet<IRExpr>();
    }
  }

  // remove elements from inUexpr according to kill rules (implemented
  // like this instead of standard algo for efficiency reasons)
  public HashSet<IRExpr> kill(IRStmt n, HashSet<IRExpr> inUexpr) {
    HashSet<IRExpr> res = new HashSet();
    if (n instanceof IRCallStmt) { // f(e)
      for (IRExpr e : inUexpr) {
        if (!e.hasMem()) res.add(e);
      }
    } else if (n instanceof IRMove) {
      // assignment
      IRMove m = (IRMove) n;
      if (m.target() instanceof IRMem) { // [e1] <- e2
        for (IRExpr e : inUexpr) {
          if (!e.hasMem()) res.add(e);
        }
      } else if (m.target() instanceof IRTemp) { // x <- e
        IRTemp x = (IRTemp) m.target();
        for (IRExpr e : inUexpr) {
          if (!e.contains(x)) res.add(e);
        }
      } else {
        throw new Error("impossible, source must be temp or mem");
      }
    } else {
      res.addAll(inUexpr);
    }
    return res;
  }

  @Override
  protected String stringify(HashSet<IRExpr> set) {
    StringBuilder sb = new StringBuilder();
    sb.append("{");

    for (IRExpr exp : set) {
      sb.append(exp.toString() + ", ");
    }

    sb.append("}");
    return sb.toString();
  }
}

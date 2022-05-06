package apd67.ir.cfg.dfa;

import apd67.assembly.Tiling;
import apd67.ir.*;
import apd67.ir.IRCallStmt;
import apd67.ir.IRMove;
import apd67.ir.IRReturn;
import apd67.ir.IRStmt;
import apd67.ir.IRTemp;
import apd67.ir.cfg.CFG;
import apd67.ir.cfg.IRBlock;
import apd67.util.Pair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class LiveVar extends DFA<IRTemp> {
  public LiveVar(CFG cfg) {
    super(cfg);
  }

  @Override
  protected HashSet<IRTemp> in(IRStmt stmt, List<Integer> preds) {
    // add logic
    // use =
    // result = use() U (outOld.get() - def()
    HashSet<IRTemp> u = use(stmt);
    HashSet<IRTemp> o = new HashSet<IRTemp>(outNew.get(stmt.getIndex())); // out(stmt, preds);
    HashSet<IRTemp> d = def(stmt);
    // int length = o.size();
    o.removeAll(d); // set diff (outOld.get() - def())
    // if (o.size() != length) {
    //   System.out.println("out had " + length + " items, now has " + o.size());
    // }
    u.addAll(o); // union use[n] U (out[n] - def[n])
    return u;
  }

  @Override
  protected HashSet<IRTemp> out(IRStmt stmt, List<Integer> succs) {
    // union all preds
    HashSet<IRTemp> union = new HashSet();

    for (Integer i : succs) {
      union.addAll(inNew.get(i));
    }

    return union;
  }

  private static HashSet<IRTemp> vars(List<IRExpr> exprs) {
    HashSet res = new HashSet<IRTemp>();
    for (IRExpr e : exprs) {
      res.addAll(vars(e));
    }
    return res;
  }

  private static HashSet<IRTemp> vars(IRExpr e) {
    if (e instanceof IRTemp) {
      HashSet res = new HashSet<IRTemp>();

      res.add((IRTemp) e);
      return res;
    }
    if (e instanceof IRBinOp) {
      IRBinOp binop = (IRBinOp) e;
      HashSet<IRTemp> left = vars(binop.left());
      left.addAll(vars(binop.right()));
      return left;
    } else if (e instanceof IRCall) {
      return new HashSet<IRTemp>();
    } else if (e instanceof IRMem) {
      return vars(((IRMem) e).expr());
    } else if (e instanceof IRName) {
      return new HashSet<IRTemp>();
    } else if (e instanceof IRConst) {
      return new HashSet<IRTemp>();
    } else if (e instanceof IRESeq) {
      throw new Error("Impossible, ESeq's removed thru lowering");
    } else {
      throw new Error("Impossible, missing expr case");
    }
  }

  public static HashSet<IRTemp> use(IRNode n) {
    if (n instanceof IRMove) {
      IRMove m = (IRMove) n;
      IRExpr src = (IRExpr) m.source();
      IRExpr t = (IRExpr) m.target();
      // target <- source
      if (t instanceof IRTemp) {
        // x <- e
        // return vars(e)
        return vars(src);
      } else if (t instanceof IRMem) {
        // [e1] <- e2
        // return vars(e1) U vars(e2)
        HashSet<IRTemp> left = vars(((IRMem) t).expr());
        left.addAll(vars(m.source()));
        return left;
      }
    } else if (n instanceof IRCJump) {
      IRCJump cj = (IRCJump) n;
      return vars(cj.cond());
    } else if (n instanceof IRReturn) {
      IRReturn ret = (IRReturn) n;
      return vars(ret.rets());
    } else if (n instanceof IRCallStmt) {
      IRCallStmt call = (IRCallStmt) n;
      return vars(call.args());
    }
    return new HashSet<IRTemp>();
  }

  // https://www.cs.cornell.edu/courses/cs4120/2021sp/notes.html?id=livevar
  /**
   * x<-e :: Move(x, e) [e1] <- e2 :: Move(Mem(e1), e2) if e :: CJump(e, l1, l2) return e ::
   * Return(e)
   */
  public HashSet<IRTemp> def(IRNode n) {
    HashSet<IRTemp> res = new HashSet<IRTemp>();
    if (n instanceof IRCallStmt) {
      IRCallStmt call = (IRCallStmt) n;
      IRName funcname = (IRName) call.target();
      int numReturned = Tiling.getNumReturn(funcname.name());
      for (int i = 0; i < numReturned; i++) {
        res.add(new IRTemp("_RET" + i));
      }
    } else if (n instanceof IRMove) {
      // x <- e
      // return x
      IRMove m = (IRMove) n;
      IRExpr t = m.target();
      if (t instanceof IRTemp) {
        res.add((IRTemp) t);
      }
    }
    return res;
  }

  // @Override
  protected String stringify(HashSet<IRTemp> set) {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    // System.out.println(set);
    // System.out.println("set class: " + set.getClass());
    Iterator it = set.iterator();
    while (it.hasNext()) {
      Object obj = it.next();
      // System.out.println(obj);
      // System.out.println("obj class: " + obj.getClass());
      IRTemp t = (IRTemp) obj;
      sb.append(t.name() + ", ");
    }

    sb.append("}");
    return sb.toString();
  }

  @Override
  /** @return the live out stuff and mark dead code as dead */
  public Pair<ArrayList<HashSet<IRTemp>>, ArrayList<HashSet<IRTemp>>> analyze() {
    Pair<ArrayList<HashSet<IRTemp>>, ArrayList<HashSet<IRTemp>>> res = super.analyze();

    // adapted from https://www.cs.colostate.edu/~cs553/ClassNotes/lecture05-dataflow-CSE.ppt.pdf
    // slide 2
    for (int blockIdx = 0; blockIdx < blocks.size(); blockIdx++) {
      IRBlock b = blocks.get(blockIdx);

      for (int j = 0; j < b.cfgLength(); j++) {
        IRStmt stmt = b.getStmt(j);
        HashSet<IRTemp> defs = def(stmt);
        if (stmt instanceof IRMove && defs.size() == 1) {
          HashSet<IRTemp> stmtOut = outNew.get(stmt.getIndex());

          if (!stmtOut.containsAll(defs)) stmt.markForDeath();
          stmt.inCFGAnnotate += stmt.markedForDeath() ? " DEAD :( " : " LIVE :)";
        }
      }
    }
    return res;
  }
}

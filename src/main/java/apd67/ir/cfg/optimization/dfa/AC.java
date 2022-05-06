package apd67.ir.cfg.dfa;

import apd67.assembly.Tiling;
import apd67.ir.*;
import apd67.ir.IRStmt;
import apd67.ir.IRTemp;
import apd67.ir.cfg.CFG;
import apd67.ir.cfg.IRBlock;
import apd67.util.Pair;
import apd67.visitor.ir.IRTranslator;
import java.util.HashSet;
import java.util.List;

/** Available Copies */
public class AC extends DFA<Pair<IRTemp, IRTemp>> {
  /** Available Copies */
  public AC(CFG cfg) {
    super(cfg);

    // assign top element
    HashSet<Pair<IRTemp, IRTemp>> allEqPairs = new HashSet<>();
    for (IRBlock b : blocks) {
      for (int i = 0; i < b.cfgLength(); i++) {
        allEqPairs.addAll(gen(b.getStmt(i)));
      }
    }
    inNew.set(0, allEqPairs);
  }

  @Override
  protected HashSet<Pair<IRTemp, IRTemp>> in(IRStmt stmt, List<Integer> preds) {
    // intersection all preds
    HashSet<Pair<IRTemp, IRTemp>> intersect = new HashSet();
    if (preds.size() > 0) intersect.addAll(outNew.get(preds.get(0)));

    for (Integer i : preds) {
      intersect.retainAll(outNew.get(i));
    }

    return intersect;
  }

  @Override
  protected HashSet<Pair<IRTemp, IRTemp>> out(IRStmt stmt, List<Integer> succs) {
    HashSet<Pair<IRTemp, IRTemp>> inCopy = new HashSet(inNew.get(stmt.getIndex())); // in(n)
    HashSet<Pair<IRTemp, IRTemp>> killedIn = kill(stmt, inCopy); //  (in(n) - kill(n))
    HashSet<Pair<IRTemp, IRTemp>> genUkilledIn = gen(stmt); //  gen[n]
    genUkilledIn.addAll(killedIn); //  gen[n] U (in(n) - kill(n))
    return genUkilledIn;
  }

  public HashSet<Pair<IRTemp, IRTemp>> gen(IRStmt stmt) {
    HashSet<Pair<IRTemp, IRTemp>> gen = new HashSet();
    if (stmt instanceof IRMove) {
      // assignment
      IRMove m = (IRMove) stmt;
      if (m.target() instanceof IRTemp && m.source() instanceof IRTemp) { // x <- y
        IRTemp x = (IRTemp) m.target();
        IRTemp y = (IRTemp) m.source();
        Pair<IRTemp, IRTemp> pair = new Pair(x, y);
        gen.add(pair);
      }
    }
    return gen;
  }

  // remove elements from inUexpr according to kill rules (implemented
  // like this instead of standard algo for efficiency reasons)
  public HashSet<Pair<IRTemp, IRTemp>> kill(IRStmt stmt, HashSet<Pair<IRTemp, IRTemp>> inCopy) {
    HashSet<Pair<IRTemp, IRTemp>> res = new HashSet();
    if (stmt instanceof IRMove) {
      // assignment
      IRMove m = (IRMove) stmt;
      if (m.target() instanceof IRTemp) { // x <- e/y
        IRTemp x = (IRTemp) m.target();
        for (Pair<IRTemp, IRTemp> pair : inCopy) {
          if (!pair.contains(x)) res.add(pair);
        }
      }
    } else if (stmt instanceof IRCallStmt) {
      // assignment
      IRCallStmt call = (IRCallStmt) stmt;
      int i = 0;
      while (i++ < Tiling.getNumReturn(((IRName) call.target()).name())) {
        IRTemp x = IRTranslator.makeRetTempStatic(i);
        for (Pair<IRTemp, IRTemp> pair : inCopy) {
          if (!pair.contains(x)) res.add(pair);
        }
      }

    } else {
      res.addAll(inCopy);
    }

    return res;
  }

  @Override
  protected String stringify(HashSet<Pair<IRTemp, IRTemp>> set) {
    StringBuilder sb = new StringBuilder();
    sb.append("{");

    for (Pair<IRTemp, IRTemp> exp : set) {
      sb.append(exp.toString() + ", ");
    }

    sb.append("}");
    return sb.toString();
  }
}

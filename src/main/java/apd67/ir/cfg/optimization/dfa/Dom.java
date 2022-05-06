package apd67.ir.cfg.dfa;

import apd67.ir.*;
import apd67.ir.IRStmt;
import apd67.ir.cfg.CFG;
import java.util.HashSet;
import java.util.List;

public class Dom extends DFA<Integer> {
  public Dom(CFG cfg) {
    super(cfg);

    int numStmts = inNew.size();
    HashSet<Integer> allNodes = new HashSet<Integer>();
    for (int i = 0; i < numStmts; i++) {
      allNodes.add(i);
    }
    inNew.set(0, allNodes);
  }

  @Override
  protected HashSet<Integer> in(IRStmt stmt, List<Integer> preds) {
    HashSet<Integer> intersect = new HashSet<Integer>();
    if (preds.size() > 0) intersect.addAll(outNew.get(preds.get(0)));

    for (Integer i : preds) {
      intersect.retainAll(outNew.get(i));
    }

    return intersect;
  }

  @Override
  protected HashSet<Integer> out(IRStmt stmt, List<Integer> preds) {
    HashSet<Integer> union = new HashSet<Integer>();
    Integer n = stmt.getIndex();
    union.add(n);
    union.addAll(inNew.get(n));
    return union;
  }

  @Override
  protected String stringify(HashSet<Integer> nodes) {
    StringBuilder sb = new StringBuilder();
    sb.append("{");

    for (Integer n : nodes) {
      sb.append(n + ", ");
    }

    sb.append("}");
    return sb.toString();
  }
}

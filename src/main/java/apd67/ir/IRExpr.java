package apd67.ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public interface IRExpr extends IRNode {
  boolean isConstant();

  long constant();

  public HashSet<IRExpr> subExprs();

  public boolean contains(IRTemp x);

  public boolean hasMem();

  public IRExpr replaceAvailableExprs(
      HashMap<IRExpr, IRTemp> cache,
      HashSet<IRExpr> inexprs,
      HashSet<IRExpr> outexprs,
      ArrayList<IRStmt> newBody);
}

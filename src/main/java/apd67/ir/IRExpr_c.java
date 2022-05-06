package apd67.ir;

import apd67.ir.visit.CheckCanonicalIRVisitor;
import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/** An intermediate representation for expressions */
public abstract class IRExpr_c extends IRNode_c implements IRExpr {

  protected HashSet<IRExpr> subExprs = null;

  @Override
  public TranslationResult lower(LowerVisitor v) {
    return new TranslationResult(this);
  }

  @Override
  public CheckCanonicalIRVisitor checkCanonicalEnter(CheckCanonicalIRVisitor v) {
    return v.enterExpr();
  }

  @Override
  public boolean isCanonical(CheckCanonicalIRVisitor v) {
    return v.inExpr() || !v.inExp();
  }

  @Override
  public boolean isConstant() {
    return false;
  }

  @Override
  public long constant() {
    throw new UnsupportedOperationException();
  }

  public HashSet<IRExpr> subExprs() {
    subExprs = new HashSet();
    return subExprs;
  }

  public boolean hasMem() {
    return false;
  }

  public boolean contains(IRTemp x) {
    return false;
  }

  @Override
  public IRExpr replaceAvailableExprs(
      HashMap<IRExpr, IRTemp> cache,
      HashSet<IRExpr> inexprs,
      HashSet<IRExpr> outexprs,
      ArrayList<IRStmt> newBody) {
    return this;
  }
}

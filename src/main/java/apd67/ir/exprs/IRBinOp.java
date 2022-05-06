package apd67.ir;

import apd67.ir.visit.AggregateVisitor;
import apd67.ir.visit.CheckConstFoldedIRVisitor;
import apd67.ir.visit.ConstFoldVisitor;
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

/** An intermediate representation for a binary operation OP(left, right) */
public class IRBinOp extends IRExpr_c {

  /** Binary operators */
  public enum OpType {
    ADD,
    SUB,
    MUL,
    HMUL,
    DIV,
    MOD,
    AND,
    OR,
    XOR,
    LSHIFT,
    RSHIFT,
    ARSHIFT,
    EQ,
    NEQ,
    LT,
    ULT,
    GT,
    LEQ,
    GEQ;

    @Override
    public String toString() {
      switch (this) {
        case ADD:
          return "ADD";
        case SUB:
          return "SUB";
        case MUL:
          return "MUL";
        case HMUL:
          return "HMUL";
        case DIV:
          return "DIV";
        case MOD:
          return "MOD";
        case AND:
          return "AND";
        case OR:
          return "OR";
        case XOR:
          return "XOR";
        case LSHIFT:
          return "LSHIFT";
        case RSHIFT:
          return "RSHIFT";
        case ARSHIFT:
          return "ARSHIFT";
        case EQ:
          return "EQ";
        case NEQ:
          return "NEQ";
        case LT:
          return "LT";
        case ULT:
          return "ULT";
        case GT:
          return "GT";
        case LEQ:
          return "LEQ";
        case GEQ:
          return "GEQ";
      }
      throw new InternalCompilerError("Unknown op type");
    }
  };

  private OpType type;
  private IRExpr left, right;

  public IRBinOp(OpType type, IRExpr left, IRExpr right) {
    this.type = type;
    this.left = left;
    this.right = right;
  }

  public OpType opType() {
    return type;
  }

  public IRExpr left() {
    return left;
  }

  public IRExpr right() {
    return right;
  }

  @Override
  public String label() {
    return type.toString();
  }

  @Override
  public IRNode visitChildren(IRVisitor v) {
    IRExpr left = (IRExpr) v.visit(this, this.left);
    IRExpr right = (IRExpr) v.visit(this, this.right);

    if (left != this.left || right != this.right) return v.nodeFactory().IRBinOp(type, left, right);

    return this;
  }

  @Override
  public TranslationResult lower(LowerVisitor v) {
    TranslationResult trL = left.getTranslation();
    TranslationResult trR = right.getTranslation();

    IRExpr eL = trL.getExpr();
    IRExpr eR = trR.getExpr();
    LinkedList<IRStmt> stmtsL = trL.getStmts();
    LinkedList<IRStmt> stmtsR = trR.getStmts();

    // add function to compute this
    boolean commutable =
        (eL instanceof IRTemp) || (eL instanceof IRConst) || (eR instanceof IRConst);

    if (commutable) {
      stmtsL.addAll(stmtsR);

      return new TranslationResult(stmtsL, new IRBinOp(type, eL, eR));
    } else {
      IRTemp t = new IRTemp(v.nextTempName());
      stmtsL.add(new IRMove(t, eL));
      stmtsL.addAll(stmtsR);

      return new TranslationResult(stmtsL, new IRBinOp(type, t, eR));
    }
  }

  @Override
  public IRNode constFold(ConstFoldVisitor v) {
    if (isConstant()) {
      long l = left.constant();
      long r = right.constant();
      switch (type) {
        case DIV:
          if (r != 0) return new IRConst(l / r);
          else return this;
        case MUL:
          return new IRConst(l * r);
        case ADD:
          return new IRConst(l + r);
        case SUB:
          return new IRConst(l - r);
        case HMUL:
          return new IRConst(Math.multiplyHigh(l, r));
        case MOD:
          if (r != 0) return new IRConst(l % r);
          else return this;
        case AND:
          return new IRConst(l & r);
        case OR:
          return new IRConst(l | r);
        case XOR:
          return new IRConst(l ^ r);
        case LSHIFT:
          return new IRConst(l << r);
        case RSHIFT:
          return new IRConst(l >>> r);
        case ARSHIFT:
          return new IRConst(l >> r);
        case EQ:
          return new IRConst(l == r ? 1 : 0);
        case NEQ:
          return new IRConst(l == r ? 0 : 1);
        case ULT:
          return new IRConst(Long.compareUnsigned(l, r) < 0 ? 1 : 0);
        case LT:
          return new IRConst(l < r ? 1 : 0);
        case GT:
          return new IRConst(l > r ? 1 : 0);
        case LEQ:
          return new IRConst(l <= r ? 1 : 0);
        case GEQ:
          return new IRConst(l >= r ? 1 : 0);
      }
    }
    return this;
  }

  @Override
  public <T> T aggregateChildren(AggregateVisitor<T> v) {
    T result = v.unit();
    result = v.bind(result, v.visit(left));
    result = v.bind(result, v.visit(right));
    return result;
  }

  @Override
  public boolean isConstFolded(CheckConstFoldedIRVisitor v) {
    if (isConstant()) {
      switch (type) {
        case DIV:
        case MOD:
          return right.constant() == 0;
        default:
          return false;
      }
    }
    return true;
  }

  @Override
  public boolean isConstant() {
    return left.isConstant() && right.isConstant();
  }

  @Override
  public void printSExp(SExpPrinter p) {
    p.startList();
    p.printAtom(type.toString());
    left.printSExp(p);
    right.printSExp(p);
    p.endList();
  }

  public HashSet<IRExpr> subExprs() {
    // add this
    if (subExprs == null) {
      subExprs = new HashSet<IRExpr>();
      subExprs.add(this);
      subExprs.addAll(left.subExprs());
      subExprs.addAll(right.subExprs());
    }
    return subExprs;
  }

  @Override
  public boolean hasMem() {
    return left.hasMem() || right.hasMem();
  }

  @Override
  public boolean contains(IRTemp x) {
    return left.contains(x) || right.contains(x);
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
      // e1 + e2 where cached: e1 + e2 -> t
      if (t == null) {
        // throw new Error(
        //     "Impossible: avail expr should already be cached if in (" + toString() + ")");
        return this;
      }
      return t;
    } else if (outexprs.contains(this)) {
      if (t != null) { // cache does have e1 + e2, so it was previously killed
        newBody.add(new IRMove(t, this));
        IRBinOp repl =
            new IRBinOp(
                type,
                left.replaceAvailableExprs(cache, inexprs, outexprs, newBody),
                right.replaceAvailableExprs(cache, inexprs, outexprs, newBody));
        if (!this.equals(repl)) return repl;
        return t;
      } else { // cache does not have e1 + e2
        IRTemp tnew = new IRTemp();
        cache.put(this, tnew);
        newBody.add(new IRMove(tnew, this));
        IRBinOp repl =
            new IRBinOp(
                type,
                left.replaceAvailableExprs(cache, inexprs, outexprs, newBody),
                right.replaceAvailableExprs(cache, inexprs, outexprs, newBody));
        if (!this.equals(repl)) return repl;
        return tnew;
      }
    } else {
      // System.out.println("default else case happens on:" + this);
      IRBinOp repl =
          new IRBinOp(
              type,
              left.replaceAvailableExprs(cache, inexprs, outexprs, newBody),
              right.replaceAvailableExprs(cache, inexprs, outexprs, newBody));
      if (!this.equals(repl)) return repl;
      return this;
    }
  }

  @Override
  public boolean propagateCopy(Pair<IRTemp, IRTemp> copyPair) {
    boolean changed = false;
    IRTemp copiedVar = copyPair.first();
    IRTemp original = copyPair.second();

    if (left instanceof IRTemp) {
      if (left.equals(copiedVar)) {
        changed = true;
        left = original;
      }
    } else {
      changed = changed || left.propagateCopy(copyPair);
    }

    if (right instanceof IRTemp) {
      if (right.equals(copiedVar)) {
        changed = true;
        right = original;
      }
    } else {
      changed = changed || right.propagateCopy(copyPair);
    }
    return changed;
  }
}

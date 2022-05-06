package apd67.ir;

import apd67.ir.visit.AggregateVisitor;
import apd67.ir.visit.CheckCanonicalIRVisitor;
import apd67.ir.visit.IRVisitor;
import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import apd67.util.cs4120.SExpPrinter;
import apd67.visitor.ir.IRTranslator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/** An intermediate representation for a function call CALL(e_target, e_1, ..., e_n) */
public class IRCall extends IRExpr_c {
  protected IRExpr target;
  protected List<IRExpr> args;
  protected int numReturnVals;

  /**
   * @param target address of the code for this function call
   * @param args arguments of this function call
   */
  public IRCall(IRExpr target, IRExpr... args) {
    this(target, Arrays.asList(args));
  }

  /**
   * @param target address of the code for this function call
   * @param args arguments of this function call
   */
  public IRCall(IRExpr target, List<IRExpr> args) {
    this.target = target;
    this.args = args;
  }

  /**
   * @param target address of the code for this function call
   * @param args arguments of this function call
   */
  public IRCall(IRExpr target, int numReturnVals, IRExpr... args) {
    this(target, numReturnVals, Arrays.asList(args));
  }

  /**
   * @param target address of the code for this function call
   * @param args arguments of this function call
   */
  public IRCall(IRExpr target, int numReturnVals, List<IRExpr> args) {
    this.target = target;
    this.args = args;
    this.numReturnVals = numReturnVals;
  }

  public IRCallStmt toStmt() {
    return new IRCallStmt(target, numReturnVals, args);
  }

  public IRExpr target() {
    return target;
  }

  public List<IRExpr> args() {
    return args;
  }

  @Override
  public String label() {
    return "CALL";
  }

  @Override
  public IRNode visitChildren(IRVisitor v) {
    boolean modified = false;

    IRExpr target = (IRExpr) v.visit(this, this.target);
    if (target != this.target) modified = true;

    List<IRExpr> results = new ArrayList<>(args.size());
    for (IRExpr arg : args) {
      IRExpr newExpr = (IRExpr) v.visit(this, arg);
      if (newExpr != arg) modified = true;
      results.add(newExpr);
    }

    if (modified) return v.nodeFactory().IRCall(target, results);

    return this;
  }

  @Override
  public TranslationResult lower(LowerVisitor v) {
    LinkedList<IRStmt> stmtsLow = new LinkedList<>();
    LinkedList<IRExpr> tempArgs = new LinkedList<>();

    for (IRExpr e : args) {
      TranslationResult tr = e.getTranslation();

      IRTemp t = new IRTemp(v.nextTempName());
      tempArgs.add(t);

      stmtsLow.addAll(tr.getStmts());
      stmtsLow.add(new IRMove(t, tr.getExpr()));
    }

    stmtsLow.add(new IRCallStmt(this.target, tempArgs));

    IRTemp t = new IRTemp(v.nextTempName());
    stmtsLow.add(new IRMove(t, IRTranslator.makeRetTempStatic(0)));
    return new TranslationResult(stmtsLow, t);
  }

  @Override
  public <T> T aggregateChildren(AggregateVisitor<T> v) {
    T result = v.unit();
    result = v.bind(result, v.visit(target));
    for (IRExpr arg : args) result = v.bind(result, v.visit(arg));
    return result;
  }

  @Override
  public boolean isCanonical(CheckCanonicalIRVisitor v) {
    return !v.inExpr();
  }

  @Override
  public void printSExp(SExpPrinter p) {
    p.startList();
    p.printAtom("CALL");
    target.printSExp(p);
    for (IRExpr arg : args) arg.printSExp(p);
    p.endList();
  }
}

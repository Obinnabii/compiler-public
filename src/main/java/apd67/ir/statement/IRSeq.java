package apd67.ir;

import apd67.ir.visit.AggregateVisitor;
import apd67.ir.visit.CheckCanonicalIRVisitor;
import apd67.ir.visit.IRVisitor;
import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import apd67.util.cs4120.SExpPrinter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/** An intermediate representation for a sequence of statements SEQ(s1,...,sn) */
public class IRSeq extends IRStmt {
  private List<IRStmt> stmts;

  /** @param stmts the statements */
  public IRSeq(IRStmt... stmts) {
    this(Arrays.asList(stmts));
  }

  /**
   * Create a SEQ from a list of statements. The list should not be modified subsequently.
   *
   * @param stmts the sequence of statements
   */
  public IRSeq(List<IRStmt> stmts) {
    this.stmts = stmts;
  }

  public List<IRStmt> stmts() {
    return stmts;
  }

  @Override
  public String label() {
    return "SEQ";
  }

  @Override
  public IRNode visitChildren(IRVisitor v) {
    boolean modified = false;

    List<IRStmt> results = new ArrayList<>(stmts.size());
    for (IRStmt stmt : stmts) {
      IRStmt newStmt = (IRStmt) v.visit(this, stmt);
      if (newStmt != stmt) modified = true;
      results.add(newStmt);
    }

    if (modified) return v.nodeFactory().IRSeq(results);

    return this;
  }

  @Override
  public TranslationResult lower(LowerVisitor v) {
    LinkedList<IRStmt> loweredStmts = new LinkedList<>();
    for (IRStmt s : stmts) {
      // might need to include exprs?
      loweredStmts.addAll(s.getTranslation().getStmts());
    }
    return new TranslationResult(loweredStmts);
  }

  @Override
  public <T> T aggregateChildren(AggregateVisitor<T> v) {
    T result = v.unit();
    for (IRStmt stmt : stmts) result = v.bind(result, v.visit(stmt));
    return result;
  }

  @Override
  public CheckCanonicalIRVisitor checkCanonicalEnter(CheckCanonicalIRVisitor v) {
    return v.enterSeq();
  }

  @Override
  public boolean isCanonical(CheckCanonicalIRVisitor v) {
    return !v.inSeq();
  }

  @Override
  public void printSExp(SExpPrinter p) {
    p.startUnifiedList();
    p.printAtom("SEQ");
    for (IRStmt stmt : stmts) stmt.printSExp(p);
    p.endList();
  }
}

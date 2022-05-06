package apd67.ir.visit;

import apd67.ir.IRExp;
import apd67.ir.IRExpr;
import apd67.ir.IRStmt;
import java.util.LinkedList;

public class TranslationResult {
  private LinkedList<IRStmt> stmts;
  private IRExpr expr;

  public TranslationResult(LinkedList<IRStmt> stmts, IRExpr expr) {
    this.stmts = stmts;
    this.expr = expr;
  }

  public TranslationResult(IRExpr expr) {
    this(new LinkedList<IRStmt>(), expr);
  }

  public TranslationResult(LinkedList<IRStmt> stmts) {
    this(stmts, null);
  }

  public TranslationResult(IRStmt stmt) {
    LinkedList<IRStmt> stmts = new LinkedList<>();
    stmts.add(stmt);
    this.stmts = stmts;
    this.expr = null;
  }

  public TranslationResult() {
    this(new LinkedList<IRStmt>(), null);
  }

  public IRExpr getExpr() {
    return expr;
  }

  public LinkedList<IRStmt> getStmts() {
    return stmts;
  }

  public LinkedList<IRStmt> getAll() {
    LinkedList<IRStmt> stmtsAndExpr = (LinkedList<IRStmt>) stmts.clone();
    if (expr != null) {
      stmtsAndExpr.add(new IRExp(expr));
    }
    return stmtsAndExpr;
  }
}

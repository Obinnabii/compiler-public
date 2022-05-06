package apd67.ir;

import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/** An intermediate representation for statements */
public abstract class IRStmt extends IRNode_c {
  public int index = -1;
  public int block_num;
  public String inCFGAnnotate = "";
  public String outCFGAnnotate = "";
  public boolean markedForDeath = false;

  @Override
  public TranslationResult lower(LowerVisitor v) {
    return new TranslationResult(this);
  }

  /** @param index the index to set */
  public void setIndex(int index) {
    this.index = index;
  }

  /** @return the index */
  public int getIndex() {
    return index;
  }

  /** Set a statement as dead */
  public void markForDeath() {
    markedForDeath = true;
  }

  /** @return true if this statement is dead */
  public boolean markedForDeath() {
    return markedForDeath;
  }

  @Override
  /** @return dot format */
  public String toDot() {
    return toDot(false); // no annotations by default
  }

  public String toDot(boolean useAnnotations) {
    if (useAnnotations) {
      return "in: "
          + inCFGAnnotate.replace("\n", "")
          + "\\n"
          + this.toString()
          + "\nout: "
          + outCFGAnnotate.replace("\n", "")
          + "\\n";
    } else {
      return this.toString() + " \\n ";
    }
  }

  public void replaceAvailableExprs(
      HashMap<IRExpr, IRTemp> cache,
      HashSet<IRExpr> inexprs,
      HashSet<IRExpr> outexprs,
      ArrayList<IRStmt> newBody) {
    return;
  }
}

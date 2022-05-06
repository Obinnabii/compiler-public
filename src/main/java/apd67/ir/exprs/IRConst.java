package apd67.ir;

import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import apd67.util.cs4120.SExpPrinter;

/** An intermediate representation for a 64-bit integer constant. CONST(n) */
public class IRConst extends IRExpr_c {
  private long value;

  /** @param value value of this constant */
  public IRConst(long value) {
    this.value = value;
  }

  public long value() {
    return value;
  }

  @Override
  public TranslationResult lower(LowerVisitor v) {
    return new TranslationResult(this);
  }

  @Override
  public String label() {
    return "CONST(" + value + ")";
  }

  @Override
  public boolean isConstant() {
    return true;
  }

  @Override
  public long constant() {
    return value;
  }

  @Override
  public void printSExp(SExpPrinter p) {
    p.startList();
    p.printAtom("CONST");
    p.printAtom(String.valueOf(value));
    p.endList();
  }
}

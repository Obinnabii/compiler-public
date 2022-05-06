package apd67.ir;

import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import apd67.util.cs4120.SExpPrinter;

/** An intermediate representation for named memory address NAME(n) */
public class IRName extends IRExpr_c {
  private String name;

  /** @param name name of this memory address */
  public IRName(String name) {
    this.name = name;
  }

  public String name() {
    return name;
  }

  @Override
  public TranslationResult lower(LowerVisitor v) {
    return new TranslationResult(this);
  }

  @Override
  public String label() {
    return "NAME(" + name + ")";
  }

  @Override
  public void printSExp(SExpPrinter p) {
    p.startList();
    p.printAtom("NAME");
    p.printAtom(name);
    p.endList();
  }
}

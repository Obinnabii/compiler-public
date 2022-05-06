package apd67.ir;

import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import apd67.util.cs4120.SExpPrinter;

/** An intermediate representation for a temporary register TEMP(name) */
public class IRTemp extends IRExpr_c {
  private static long counter = 0;
  private String name;

  /** create generic temp */
  public IRTemp() {
    this.name = "_temp" + (counter++);
  }

  /** @param name name of this temporary register */
  public IRTemp(String name) {
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
    return "TEMP(" + name + ")";
  }

  @Override
  public void printSExp(SExpPrinter p) {
    p.startList();
    p.printAtom("TEMP");
    p.printAtom(name);
    p.endList();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof IRTemp)) return false;
    return name.equals(((IRTemp) o).name());
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean contains(IRTemp x) {
    return x.name.equals(name);
  }
}

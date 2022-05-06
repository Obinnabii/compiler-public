package apd67.ir;

import apd67.ir.visit.AggregateVisitor;
import apd67.ir.visit.CheckCanonicalIRVisitor;
import apd67.ir.visit.CheckConstFoldedIRVisitor;
import apd67.ir.visit.ConstFoldVisitor;
import apd67.ir.visit.IRVisitor;
import apd67.ir.visit.InsnMapsBuilder;
import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import apd67.util.Pair;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.cs4120.SExpPrinter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;

/** A node in an intermediate-representation abstract syntax tree. */
public abstract class IRNode_c implements IRNode {

  private TranslationResult translation = null;

  @Override
  public IRNode visitChildren(IRVisitor v) {
    return this;
  }

  @Override
  public IRNode constFold(ConstFoldVisitor v) {
    return this;
  }

  @Override
  public TranslationResult lower(LowerVisitor v) {
    return new TranslationResult();
  }

  @Override
  public void setTranslation(TranslationResult tr) {
    translation = tr;
  }

  @Override
  public TranslationResult getTranslation() {
    if (translation == null) throw new Error("Translation accessed before set");
    return translation;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  @Override
  public <T> T aggregateChildren(AggregateVisitor<T> v) {
    return v.unit();
  }

  @Override
  public InsnMapsBuilder buildInsnMapsEnter(InsnMapsBuilder v) {
    return v;
  }

  @Override
  public IRNode buildInsnMaps(InsnMapsBuilder v) {
    v.addInsn(this);
    return this;
  }

  @Override
  public CheckCanonicalIRVisitor checkCanonicalEnter(CheckCanonicalIRVisitor v) {
    return v;
  }

  @Override
  public boolean isCanonical(CheckCanonicalIRVisitor v) {
    return true;
  }

  @Override
  public boolean isConstFolded(CheckConstFoldedIRVisitor v) {
    return true;
  }

  @Override
  public abstract String label();

  @Override
  public abstract void printSExp(SExpPrinter p);

  @Override
  public String toString() {
    StringWriter sw = new StringWriter();
    try (PrintWriter pw = new PrintWriter(sw);
        SExpPrinter sp = new CodeWriterSExpPrinter(pw)) {
      printSExp(sp);
    }
    return sw.toString();
  }

  public String hashString() {
    return toString();
  }

  public boolean propagateCopy(Pair<IRTemp, IRTemp> copyPair) {
    return false;
  }

  public boolean propagateCopy(HashSet<Pair<IRTemp, IRTemp>> copyPairs) {
    boolean out = false;
    for (Pair<IRTemp, IRTemp> copyPair : copyPairs) out = out || propagateCopy(copyPair);
    return out;
  }

  @Override
  public boolean equals(Object o) {
    if (!(this.getClass().equals(o.getClass()))) return false;
    return toString().equals(((IRNode) o).toString());
  }

  @Override
  public int hashCode() {
    return hashString().hashCode();
  }

  public String toDot() {
    return toDot(false);
  }

  /** @return dot format */
  public String toDot(boolean annotate) {
    return this.toString();
  }
}

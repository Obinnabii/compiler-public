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
import apd67.util.cs4120.SExpPrinter;
import java.util.HashSet;

/** A node in an intermediate-representation abstract syntax tree. */
public interface IRNode extends Cloneable {

  /**
   * Visit the children of this IR node.
   *
   * @param v the visitor
   * @return the result of visiting children of this node
   */
  IRNode visitChildren(IRVisitor v);

  <T> T aggregateChildren(AggregateVisitor<T> v);

  InsnMapsBuilder buildInsnMapsEnter(InsnMapsBuilder v);

  IRNode buildInsnMaps(InsnMapsBuilder v);

  IRNode constFold(ConstFoldVisitor v);

  TranslationResult lower(LowerVisitor v);

  TranslationResult getTranslation();

  void setTranslation(TranslationResult tr);

  CheckCanonicalIRVisitor checkCanonicalEnter(CheckCanonicalIRVisitor v);

  boolean isCanonical(CheckCanonicalIRVisitor v);

  boolean isConstFolded(CheckConstFoldedIRVisitor v);

  String label();

  /**
   * Print an S-expression representation of this IR node.
   *
   * @param p the S-expression printer
   */
  void printSExp(SExpPrinter p);

  String toDot();

  String toDot(boolean annotate);

  public boolean propagateCopy(Pair<IRTemp, IRTemp> copyPair);

  public boolean propagateCopy(HashSet<Pair<IRTemp, IRTemp>> copyPairs);
}

package apd67.ir.visit;

import apd67.ir.IRCompUnit;
import apd67.ir.IRNode;
import apd67.ir.IRNodeFactory_c;

public class LowerVisitor extends IRVisitor {
  private long tmpCounter = 0;

  public LowerVisitor() {
    super(new IRNodeFactory_c());
  }

  public IRNode translate(IRNode n) {
    // DEPRECATED
    throw new Error("Used deprecated version of translate for IR Lowering");
    // IRNode nTrans = (IRNode) this.visit(n);
    // TranslationResult tr = nTrans.getTranslation();
    // return new IRSeq(tr.getAll());
    // return nTrans;
  }

  public IRCompUnit translate_(IRCompUnit n) {
    IRCompUnit nTrans = (IRCompUnit) n.visitChildren(this);
    nTrans.translateFunctions(); // swap functions for translated version
    return nTrans;
  }

  @Override
  protected IRVisitor enter(IRNode parent, IRNode n) {
    return this;
  }

  @Override
  protected IRNode leave(IRNode parent, IRNode n, IRNode n_, IRVisitor v_) {
    TranslationResult res = n_.lower((LowerVisitor) v_);
    n_.setTranslation(res);
    return n_;
  }

  public String nextTempName() {
    return "_lt" + (tmpCounter++);
  }
}

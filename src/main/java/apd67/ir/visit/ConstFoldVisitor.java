package apd67.ir.visit;

import apd67.ir.IRCompUnit;
import apd67.ir.IRNode;
import apd67.ir.IRNodeFactory_c;

public class ConstFoldVisitor extends IRVisitor {

  public ConstFoldVisitor() {
    super(new IRNodeFactory_c());
  }

  public IRCompUnit constFold(IRCompUnit n) {
    return (IRCompUnit) n.visitChildren(this);
  }

  @Override
  protected IRVisitor enter(IRNode parent, IRNode n) {
    return this;
  }

  @Override
  protected IRNode leave(IRNode parent, IRNode n, IRNode n_, IRVisitor v_) {
    return n_.constFold((ConstFoldVisitor) v_);
  }
}

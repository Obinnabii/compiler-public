package apd67.ast;

import apd67.ir.*;
import apd67.util.exception.TypeException;
import apd67.visitor.ir.IRTranslator;

public class Or extends Binop {

  private static int uniq = 0;

  public Or(Expr e1, Expr e2) {
    super("|", e1, e2);
  }

  @Override
  public Node irTranslate(IRTranslator v) throws TypeException {
    IRLabel l1 = v.makeGenericLabel();
    IRLabel l2 = v.makeGenericLabel();
    IRLabel lf = v.makeFalseLabel(); // produce unique labels
    String l1s = l1.name(), lfs = lf.name(), l2s = l2.name(); // get the unique label names
    IRTemp tx = new IRTemp("_x_" + uniq++);
    iRTranslation =
        new IRESeq(
            new IRSeq(
                new IRMove(tx, new IRConst(1)),
                new IRCJump((IRExpr) e1.getIRTranslation(), lfs, l1s),
                l1,
                new IRCJump((IRExpr) e2.getIRTranslation(), lfs, l2s),
                l2,
                new IRMove(tx, new IRConst(0)),
                lf),
            tx);
    return this;
  }
}

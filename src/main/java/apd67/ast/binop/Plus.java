package apd67.ast;

public class Plus extends Binop {
  public Plus(Expr e1, Expr e2) {
    super("+", e1, e2);
  }
  // TODO: Move this code to Binop (look at Xi Lang Spec @jrn79)
  //   public Plus typeCheck(Visitor v) throws TypeException {
  //     Expr new_e1 = (Expr) e1.typeCheck(v);
  //     Expr new_e2 = (Expr) e2.typeCheck(v);
  //     if ((new_e1.getType() instanceof IntType) && (new_e2.getType() instanceof IntType)) {
  //       Plus p = new Plus(new_e1, new_e2);
  //       p.setType(new IntType());
  //       return p;
  //     }
  //     throw new TypeException("Plus only works on ints", 3110, 4110);
  //   }
}

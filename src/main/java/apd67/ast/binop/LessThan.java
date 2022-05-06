package apd67.ast;

public class LessThan extends Binop {
  public LessThan(Expr e1, Expr e2) {
    super("<", e1, e2);
  }
}

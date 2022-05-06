package apd67.ast;

public class Minus extends Binop {
  public Minus(Expr e1, Expr e2) {
    super("-", e1, e2);
  }
}

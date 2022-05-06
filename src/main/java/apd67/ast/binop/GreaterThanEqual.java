package apd67.ast;

public class GreaterThanEqual extends Binop {
  public GreaterThanEqual(Expr e1, Expr e2) {
    super(">=", e1, e2);
  }
}

package apd67.ast;

public class GreaterThan extends Binop {
  public GreaterThan(Expr e1, Expr e2) {
    super(">", e1, e2);
  }
}

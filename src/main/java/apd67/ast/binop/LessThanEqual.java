package apd67.ast;

public class LessThanEqual extends Binop {
  public LessThanEqual(Expr e1, Expr e2) {
    super("<=", e1, e2);
  }
}

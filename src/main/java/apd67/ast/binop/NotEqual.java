package apd67.ast;

public class NotEqual extends Binop {
  public NotEqual(Expr e1, Expr e2) {
    super("!=", e1, e2);
  }
}

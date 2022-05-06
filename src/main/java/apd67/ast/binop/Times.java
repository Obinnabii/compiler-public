package apd67.ast;

public class Times extends Binop {
  public Times(Expr e1, Expr e2) {
    super("*", e1, e2);
  }
}

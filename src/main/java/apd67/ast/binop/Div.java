package apd67.ast;

public class Div extends Binop {
  public Div(Expr e1, Expr e2) {
    super("/", e1, e2);
  }
}

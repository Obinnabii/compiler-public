package apd67.ast;

public class Mod extends Binop {
  public Mod(Expr e1, Expr e2) {
    super("%", e1, e2);
  }
}

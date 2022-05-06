package apd67.ast;

public class Negative extends Unop {
  public Negative(Expr e1) {
    super("-", e1);
  }
}

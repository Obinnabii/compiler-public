package apd67.ast;

public class HighTimes extends Binop {
  public HighTimes(Expr e1, Expr e2) {
    super("*>>", e1, e2);
  }
}

package apd67.ast;

public class Equal extends Binop {
  public Equal(Expr e1, Expr e2) {
    super("==", e1, e2);
  }
}

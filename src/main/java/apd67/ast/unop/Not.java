package apd67.ast;

public class Not extends Unop {
  public Not(Expr e) {
    super("!", e);
  }
}

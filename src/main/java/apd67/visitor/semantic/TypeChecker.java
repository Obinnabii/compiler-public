package apd67.visitor.semantic;

import apd67.ast.Node;
import apd67.util.exception.SemanticError;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;

/** A type checker implementation that utilizes the visitor pattern. */
public class TypeChecker extends Visitor {
  public Context context;

  public TypeChecker() {
    context = new ContextDefault();
  }

  public TypeChecker(Context context) {
    this.context = context;
  }

  public Node leave(Node newNode, Node origNode) throws SemanticError {
    return newNode.typeCheck(this);
  }

  public IRTranslator toIRTranslator() {
    return new IRTranslator(context);
  }
}

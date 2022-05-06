package apd67.visitor.ir;

import apd67.ast.And;
import apd67.ast.Bool;
import apd67.ast.Expr;
import apd67.ast.Identifier;
import apd67.ast.Node;
import apd67.ast.Not;
import apd67.ast.Or;
import apd67.ast.Unop;
import apd67.ir.*;
import apd67.ir.IRLabel;
import apd67.ir.IRName;
import apd67.ir.IRTemp;
import apd67.ir.interpret.Configuration;
import apd67.util.exception.SemanticError;
import apd67.visitor.Visitor;
import apd67.visitor.semantic.*;

/** A IR translation implementation that utilizes the visitor pattern */
public class IRTranslator extends Visitor {
  public Context context;

  private static long nameCtr = 0;
  private boolean usedLength = false;

  public IRTranslator(Context context) {
    this.context = context;
  }

  public Node leave(Node newNode, Node origNode) throws SemanticError {
    return newNode.irTranslate(this);
  }

  public IRLabel makeTrueLabel() {
    return new IRLabel("true_" + nameCtr++);
  }

  public IRLabel makeFalseLabel() {
    return new IRLabel("false_" + nameCtr++);
  }

  public IRLabel makeErrorLabel() {
    return new IRLabel("err_" + nameCtr++);
  }

  public IRLabel makeGenericLabel() {
    return new IRLabel("_" + nameCtr++);
  }

  public static IRLabel makeGenericLabelStatic() {
    return new IRLabel("_" + nameCtr++);
  }

  public IRLabel uniqLabelFromString(String s) {
    return new IRLabel(s + "_" + nameCtr++);
  }

  public IRLabel makeFuncABILabel(Identifier id) throws SemanticError {
    return new IRLabel(getFuncABIString(id));
  }

  public IRTemp makeRetTemp(int retNum) {
    return new IRTemp(Configuration.ABSTRACT_RET_PREFIX + retNum);
  }

  public static IRTemp makeRetTempStatic(int retNum) {
    return new IRTemp(Configuration.ABSTRACT_RET_PREFIX + retNum);
  }

  public IRTemp makeArgTemp(int argNum) {
    return new IRTemp(Configuration.ABSTRACT_ARG_PREFIX + argNum);
  }

  public static IRTemp makeArgTempStatic(int argNum) {
    return new IRTemp(Configuration.ABSTRACT_ARG_PREFIX + argNum);
  }
  /**
   * For errorcatching, make a label and
   *
   * @param fname
   * @return
   * @throws SemanticError
   */
  public String getFuncABIString(Identifier id) throws SemanticError {
    try {
      String suffix = "";
      String funcABIName = "_I" + id.getName().replace("_", "__");

      suffix = context.lookup(id.getName()).getABIRepresentation();

      return funcABIName + suffix;
    } catch (SemanticError ce) {
      ce.line = id.getLine();
      ce.column = id.getColumn();
      throw ce;
    }
  }

  public boolean usedLength() {
    return usedLength;
  }

  public IRName getFuncABIName(Identifier id) throws SemanticError {
    if (id.getName() == "length") usedLength = true; // Length was used
    return new IRName(getFuncABIString(id));
  }

  public IRStmt CTranslate(Expr e, IRName trueName, IRName falseName) {
    IRLabel branchlbl = makeGenericLabel();
    IRName branchName = new IRName(branchlbl.name());
    if (e instanceof Bool)
      return ((Bool) e).getValue() ? new IRJump(trueName) : new IRJump(falseName);
    else if (e instanceof Unop) return CTranslate(((Not) e).expr(), falseName, trueName);
    else if (e instanceof And)
      return new IRSeq(
          CTranslate(((And) e).getE1(), branchName, falseName),
          branchlbl,
          CTranslate(((And) e).getE2(), trueName, falseName));
    else if (e instanceof Or)
      return new IRSeq(
          CTranslate(((Or) e).getE1(), trueName, branchName),
          branchlbl,
          CTranslate(((Or) e).getE2(), trueName, falseName));
    else return new IRCJump((IRExpr) e.getIRTranslation(), trueName.name(), falseName.name());
  }
}

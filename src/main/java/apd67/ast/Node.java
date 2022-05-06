package apd67.ast;

import apd67.ir.IRNode;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.util.exception.SemanticError;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;

public abstract class Node implements Cloneable {
  protected Type type = null;
  protected int line = 3110;
  protected int col = 4120;
  protected IRNode iRTranslation = null;

  /**
   * Returns the node that results after visiting all children of this node.
   *
   * @param v The visitor of this node.
   */
  // public abstract Node visitChildren(Visitor v);
  public abstract Node visitChildren(Visitor v) throws SemanticError;

  /**
   * Returns a node with the type of it in the field type, or throws a TypeException if the node
   * does not type check.
   *
   * @param v The visitor of this node.
   */
  // public abstract Node typeCheck(Visitor v) throws Exception;
  public Node typeCheck(TypeChecker v) throws SemanticError {
    return this;
  }

  /**
   * Returns a node with it's IR translation in the field irTranslation.
   *
   * @param v The visitor of this node.
   */
  public Node irTranslate(IRTranslator v) throws SemanticError {
    return this;
  }

  public abstract void prettyPrint(CodeWriterSExpPrinter cw);

  public Node accept(Visitor v) throws SemanticError {
    Visitor v2 = v.enter(this);
    Node n = visitChildren(v2);
    return v2.leave(n, this);
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public Type getType() throws TypeException {
    if (type == null) {
      // new Throwable().printStackTrace();
      throw new TypeException("Error: Type not found", line, col);
    }
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return col;
  }

  public IRNode getIRTranslation() {
    return iRTranslation;
  }

  public Node setColumn(int c) {
    col = c;
    return this;
  }

  public Node setLine(int l) {
    line = l;
    return this;
  }

  public Node setLineAndCol(Node donor) {
    line = donor.getLine();
    col = donor.getColumn();
    return this;
  }

  public boolean isUscore() {
    return false;
  }
}

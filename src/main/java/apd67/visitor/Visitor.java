package apd67.visitor;

import apd67.ast.Node;
import apd67.util.exception.SemanticError;

public class Visitor {
  /**
   * Method called before visiting children. This method should deal with mutating or copying the
   * current visitor if need be before visiting the children of {@code n}
   *
   * @param n The current node.
   * @return The visitor that will visit the children of {@code n}
   */
  public Visitor enter(Node n) {
    return this;
  }

  /**
   * Method is called after the children of origNode have been visited (creating newNode), but
   * before leaving the node.
   *
   * @param newNode Node created after visiting all children of origNode
   * @param origNode Original node visited before visiting children
   * @return New node after performing operations (overriden by children)
   */
  public Node leave(Node newNode, Node origNode) throws SemanticError {
    return newNode;
  }

  // // binop
  // public And visit(And n) { return n; }
  // public ArrayAccess visit(ArrayAccess n) { return n; }
  // public Div visit(Div n) { return n; }
  // public Equal visit(Equal n) { return n; }
  // public GreaterThan visit(GreaterThan n) { return n; }
  // public GreaterThanEqual visit(GreaterThanEqual n) { return n; }
  // public HighTimes visit(HighTimes n) { return n; }
  // public LessThan visit(LessThan n) { return n; }
  // public LessThanEqual visit(LessThanEqual n) { return n; }
  // public Minus visit(Minus n) { return n; }
  // public Mod visit(Mod n) { return n; }
  // public NotEqual visit(NotEqual n) { return n; }
  // public Or visit(Or n) { return n; }
  // public Plus visit(Plus p) { return p; }
  // public Times visit(Times n) { return n; }

  // // unop
  // public Negative visit(Negative n) { return n; }
  // public Not visit(Not n) { return n; }

  // // type
  // public ArrayType visit(ArrayType n) { return n; }
  // public BoolType visit(BoolType n) { return n; }
  // public IntType visit(IntType n) { return n; }

  // // stmt
  // public Assignment visit(Assignment n) { return n; }
  // public Block visit(Block n) { return n; }
  // public Declaration visit(Declaration n) { return n; }
  // public If visit(If n) { return n; }
  // public ProcedureCall visit(ProcedureCall n) { return n; }
  // public Return visit(Return n) { return n; }
  // public UscoreDecl visit(UscoreDecl n) { return n; }
  // public While visit(While n) { return n; }

  // // interface
  // public MethodDeclaration visit(MethodDeclaration n) { return n; }
  // public Use visit(Use n) { return n; }

  // // method
  // public MethodCall visit(MethodCall n) { return n; }
  // public MethodDef visit(MethodDef n) { return n; }

  // // literal
  // public Literal visit(Literal l) { return l; }
  // public ArrayList visit(ArrayList al) { return al; }
  // public Bool visit(Bool b) { return b; }
  // public Char visit(Char c) { return c; }
  // public Identifier visit(Identifier i) { return i; }
  // public Int visit(Int i) { return i; }
  // public Identifier visit(Identifier i) { return i; }
  // public StringLit visit(StringLit sl) { return sl; }
}

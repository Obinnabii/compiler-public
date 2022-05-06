package apd67.ast;

import apd67.ir.IRBinOp;
import apd67.ir.IRConst;
import apd67.ir.IRExpr;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;

public class Unop extends Expr {
  String op;
  Expr e1;

  public Unop(String op, Expr e1) {
    this.op = op;
    this.e1 = e1;
  }

  /** @return the expression to be negated */
  public Expr expr() {
    return e1;
  }

  @Override
  public Unop typeCheck(TypeChecker tc) throws SemanticError {
    Unop u;
    try {
      u = (Unop) this.clone();
    } catch (CloneNotSupportedException cnse) {
      cnse.printStackTrace();
      throw new Error(cnse.toString());
    }
    Type t = e1.getType();

    switch (op) {
      case "!":
        if (t.expectedEquals(new BoolType())) u.setType(new BoolType());
        else throw new TypeException("Can only negate bools", line, col);
        break;
      case "-":
        if (t.expectedEquals(new IntType())) u.setType(new IntType());
        else throw new TypeException("Can only apply - to ints", line, col);
        break;
      default:
        throw new Error("Unreachable typecheck case in unop");
    }
    return u;
  }

  // #TODO: Make sure to account for min-int!!! @Obi & @Apdettmer

  @Override
  public Unop visitChildren(Visitor v) throws SemanticError {
    Expr e_new = (Expr) e1.accept(v);

    if (e_new != e1) {
      switch (op) {
        case "!":
          return (Unop) new Not(e_new).setLineAndCol(this);
        case "-":
          return (Unop) new Negative(e_new).setLineAndCol(this);
        default:
          throw new Error("unreachable unop visitChildren case");
      }
    } else {
      return this;
    }
  }

  @Override
  public Node irTranslate(IRTranslator v) throws TypeException {
    IRExpr i1 = (IRExpr) e1.getIRTranslation();
    switch (op) {
      case "!":
        iRTranslation = new IRBinOp(IRBinOp.OpType.XOR, new IRConst(1), i1);
        break;
      case "-":
        iRTranslation = new IRBinOp(IRBinOp.OpType.SUB, new IRConst(0), i1);
        break;
      default:
        throw new Error("unimplemented unop " + op);
    }
    return this;
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();
    cw.printAtom(op);

    e1.prettyPrint(cw);

    cw.endList();
  }
}

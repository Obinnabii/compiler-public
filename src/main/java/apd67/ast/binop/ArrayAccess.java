package apd67.ast;

import apd67.ir.*;
import apd67.util.cs4120.*;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;
import java.util.LinkedList;

public class ArrayAccess extends Binary implements Variable {
  Variable var;
  Expr e;
  IRExpr addr; // used in assignment
  private static int uniq = 0;

  public ArrayAccess(Variable var, Expr e) {
    this.var = var;
    this.e = e;
  }

  /**
   * @param var - the base variable of the ArrayAccess
   * @param exprs - a list of expressions representing multi-dimensional array accessors;<br>
   *     a[1][2][3][4] -> ((((a, 1), 2), 3), 4)
   * @return the proper ArrayAccess object to represent the arguments provided.
   */
  public static ArrayAccess fromList(Variable var, LinkedList<Expr> exprs) {
    ArrayAccess b = new ArrayAccess(var, exprs.pop());
    while (!exprs.isEmpty()) {
      b = new ArrayAccess(b, exprs.pop());
    }
    return b;
  }

  @Override
  public ArrayAccess typeCheck(TypeChecker tc) throws SemanticError {
    ArrayAccess ac;
    try {
      ac = (ArrayAccess) this.clone();
    } catch (CloneNotSupportedException cnse) {
      cnse.printStackTrace();
      throw new Error(cnse.toString());
    }

    Type tExpr1 = ((Node) var).getType().expectedType();
    Type tExpr2 = e.getType();
    if (tExpr1.expectedArray() && tExpr2.expectedEquals(new IntType())) {
      ac.setType(((ArrayType) tExpr1).base);
    } else {
      throw new TypeException("Array access needs array and int operands", line, col);
    }
    return ac;
  }

  @Override
  public ArrayAccess visitChildren(Visitor v) throws SemanticError {
    Variable new_var = (Variable) ((Node) var).accept(v);
    Expr new_e = (Expr) e.accept(v);

    if (new_e != e || new_var != var) {
      return new ArrayAccess(new_var, new_e);
    } else {
      return this;
    }
  }

  @Override
  public Node irTranslate(IRTranslator v) throws TypeException {
    IRTemp ta = new IRTemp("_aaa_" + uniq++), ti = new IRTemp("_aai_" + uniq++);
    IRLabel lok1 = v.makeTrueLabel(), lerr1 = v.makeErrorLabel();
    IRLabel lok2 = v.makeTrueLabel(), lerr2 = v.makeErrorLabel();
    IRExpr etrans = (IRExpr) e.getIRTranslation();

    IRExpr vartrans =
        (var instanceof ArrayAccess)
            ? new IRMem(((ArrayAccess) var).getAddr())
            : (IRExpr) ((Node) var).getIRTranslation();
    iRTranslation =
        new IRESeq(
            new IRSeq(
                new IRMove(ta, vartrans),
                new IRMove(ti, etrans),
                new IRCJump(
                    new IRBinOp(
                        IRBinOp.OpType.ULT,
                        ti,
                        new IRMem(new IRBinOp(IRBinOp.OpType.SUB, ta, new IRConst(8)))),
                    lok1.name(),
                    lerr1.name()),
                lerr1,
                new IRExp(new IRCall(new IRName("_xi_out_of_bounds"), new IRConst(0))),
                lok1),
            new IRMem(
                new IRBinOp(
                    IRBinOp.OpType.ADD, ta, new IRBinOp(IRBinOp.OpType.MUL, ti, new IRConst(8)))));
    addr =
        new IRESeq(
            new IRSeq(
                new IRMove(ta, vartrans),
                new IRMove(ti, (IRExpr) e.getIRTranslation()),
                new IRCJump(
                    new IRBinOp(
                        IRBinOp.OpType.ULT,
                        ti,
                        new IRMem(new IRBinOp(IRBinOp.OpType.SUB, ta, new IRConst(8)))),
                    lok2.name(),
                    lerr2.name()),
                lerr2,
                new IRExp(new IRCall(new IRName("_xi_out_of_bounds"), new IRConst(0))),
                lok2),
            new IRBinOp(
                IRBinOp.OpType.ADD, ta, new IRBinOp(IRBinOp.OpType.MUL, ti, new IRConst(8))));
    return this;
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startList();

    cw.printAtom("[]");
    var.prettyPrint(cw);
    e.prettyPrint(cw);

    cw.endList();
  }

  public IRExpr getAddr() {
    return addr;
  }

  public String toString() {
    return "([]" + " " + var + " " + e + ")";
  }
}

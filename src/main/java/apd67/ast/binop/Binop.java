package apd67.ast;

import apd67.ir.*;
import apd67.util.cs4120.*;
import apd67.util.exception.SemanticError;
import apd67.util.exception.TypeException;
import apd67.visitor.*;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;

/** Class to handle Traditional Binop (not including array access) tedium. */
public class Binop extends Binary {
  String op;
  Expr e1, e2;
  private static int uniq = 0;

  public Binop(String op, Expr e1, Expr e2) {
    this.op = op;
    this.e1 = e1;
    this.e2 = e2;
    setLineAndCol(e1);
  }

  /** @return the e1 */
  public Expr getE1() {
    return e1;
  }

  /** @return the e2 */
  public Expr getE2() {
    return e2;
  }

  @Override
  public Expr typeCheck(TypeChecker tc) throws SemanticError {
    Type t1 = e1.getType();
    Type t2 = e2.getType();

    Expr e;
    try {
      e = (Expr) this.clone();
    } catch (CloneNotSupportedException cnse) {
      cnse.printStackTrace();
      throw new Error(cnse.toString());
    }

    // for clarity and ease of use, make some contant types
    IntType intType = new IntType();
    BoolType boolType = new BoolType();

    switch (op) {
      case "/":
      case "*>>":
      case "-":
      case "%":
      case "*":
        if (t1.expectedEquals(intType) && t2.expectedEquals(intType)) e.setType(new IntType());
        else throw new TypeException(op + " needs int operands", line, col);
        break;
      case "+":
        if (t1.expectedEquals(intType) && t2.expectedEquals(intType)) {
          e.setType(new IntType());
        } else if (t1.expectedArray() && t2.expectedArray() && t1.expectedEquals(t2)) {
          e.setType(t1);
        } else
          throw new TypeException(
              op + " needs int or array operands " + t1.expectedType() + " " + t2.expectedType(),
              line,
              col);
        break;
      case "<":
      case "<=":
      case ">":
      case ">=":
        if (t1.expectedEquals(intType) && t2.expectedEquals(intType)) e.setType(new BoolType());
        else throw new TypeException(op + " needs int operands", line, col);
        break;
      case "!=":
      case "==":
        if (t1.expectedEquals(intType) && t2.expectedEquals(intType)
            || t1.expectedEquals(boolType) && t2.expectedEquals(boolType)
            || t1.expectedArray() && t2.expectedArray() && t1.expectedEquals(t2)) {
          e.setType(new BoolType());
        } else throw new TypeException(op + " needs the same int/bool/array operands", line, col);
        break;
      case "&":
      case "|":
        if (t1.expectedEquals(boolType) && t2.expectedEquals(boolType)) e.setType(new BoolType());
        else throw new TypeException(op + " needs bool operands", line, col);
        break;
      default:
        throw new Error("unreachable typeChecker binop case");
    }
    return e;
  }

  @Override
  public Expr visitChildren(Visitor v) throws SemanticError {
    Expr new_e1 = (Expr) e1.accept(v);
    Expr new_e2 = (Expr) e2.accept(v);
    if (new_e1 != e1 || new_e2 != e2) {
      Expr e;
      switch (op) {
        case "&":
          return new And(new_e1, new_e2);
        case "/":
          return new Div(new_e1, new_e2);
        case "==":
          return new Equal(new_e1, new_e2);
        case ">":
          return new GreaterThan(new_e1, new_e2);
        case ">=":
          return new GreaterThanEqual(new_e1, new_e2);
        case "*>>":
          return new HighTimes(new_e1, new_e2);
        case "<":
          return new LessThan(new_e1, new_e2);
        case "<=":
          return new LessThanEqual(new_e1, new_e2);
        case "-":
          return new Minus(new_e1, new_e2);
        case "%":
          return new Mod(new_e1, new_e2);
        case "!=":
          return new NotEqual(new_e1, new_e2);
        case "|":
          return new Or(new_e1, new_e2);
        case "+":
          return new Plus(new_e1, new_e2);
        case "*":
          return new Times(new_e1, new_e2);
        default:
          throw new Error("unreachable visitChildren binop case");
      }
    } else {
      return this;
    }
  }

  @Override
  public Node irTranslate(IRTranslator v) throws SemanticError {
    IRExpr i1 = (IRExpr) e1.getIRTranslation();
    IRExpr i2 = (IRExpr) e2.getIRTranslation();

    IRBinOp.OpType irOp = IRBinOp.OpType.ADD;
    switch (op) {
      case "+":
        if (e1.getType().expectedArray()) {
          IRTemp tm = new IRTemp("_m_concat_" + uniq++),
              tn1 = new IRTemp("_n1_" + uniq++),
              tn2 = new IRTemp("_n2_" + uniq++);
          IRLabel lh1 = v.makeGenericLabel(),
              lh2 = v.makeGenericLabel(),
              l11 = v.makeTrueLabel(),
              l12 = v.makeTrueLabel(),
              le1 = v.makeFalseLabel(),
              le2 = v.makeFalseLabel();
          // vars used for value to throw away side effects of irtranslation i.e. avoid
          // reinitializing arrays
          IRExpr i1comp, i2comp;
          if (!(i1 instanceof IRCall || i1 instanceof IRTemp))
            i1comp = ((IRESeq) e1.getIRTranslation()).expr();
          else i1comp = i1;
          if (!(i2 instanceof IRCall || i2 instanceof IRTemp))
            i2comp = ((IRESeq) e2.getIRTranslation()).expr();
          else i2comp = i2;

          // didn't realize below that the irtranslation gives me the value at the specified address
          // (duh)
          // leaving just in case I forgot about something and I need it
          // if (e1 instanceof ArrayAccess) {
          //   i1 = new IRMem(((ArrayAccess) e1).getAddr());
          // }
          // if (e2 instanceof ArrayAccess) {
          //   i2 = new IRMem(((ArrayAccess) e2).getAddr());
          // }
          iRTranslation =
              new IRESeq(
                  new IRSeq(
                      new IRMove(
                          tn1, new IRMem(new IRBinOp(IRBinOp.OpType.SUB, i1, new IRConst(8)))),
                      new IRMove(
                          tn2, new IRMem(new IRBinOp(IRBinOp.OpType.SUB, i2, new IRConst(8)))),
                      new IRMove(
                          tm,
                          new IRCall(
                              new IRName("_xi_alloc"),
                              new IRBinOp(
                                  IRBinOp.OpType.ADD,
                                  new IRBinOp(
                                      IRBinOp.OpType.MUL,
                                      new IRBinOp(IRBinOp.OpType.ADD, tn1, tn2),
                                      new IRConst(8)),
                                  new IRConst(8)))),
                      new IRMove(new IRMem(tm), new IRBinOp(IRBinOp.OpType.ADD, tn1, tn2)),
                      // loop 1
                      new IRMove(new IRTemp("_ctr1"), new IRConst(0)),
                      new IRSeq(
                          lh1,
                          new IRCJump(
                              new IRBinOp(IRBinOp.OpType.ULT, new IRTemp("_ctr1"), tn1),
                              l11.name(),
                              le1.name()),
                          l11,
                          new IRMove(
                              new IRTemp("_move"),
                              new IRMem(
                                  new IRBinOp(
                                      IRBinOp.OpType.ADD,
                                      i1comp,
                                      new IRBinOp(
                                          IRBinOp.OpType.MUL,
                                          new IRTemp("_ctr1"),
                                          new IRConst(8))))),
                          new IRMove(
                              new IRMem(
                                  new IRBinOp(
                                      IRBinOp.OpType.ADD,
                                      tm,
                                      new IRBinOp(
                                          IRBinOp.OpType.MUL,
                                          new IRBinOp(
                                              IRBinOp.OpType.ADD,
                                              new IRTemp("_ctr1"),
                                              new IRConst(1)),
                                          new IRConst(8)))),
                              new IRTemp("_move")),
                          new IRMove(
                              new IRTemp("_ctr1"),
                              new IRBinOp(IRBinOp.OpType.ADD, new IRTemp("_ctr1"), new IRConst(1))),
                          new IRJump(new IRName(lh1.name())),
                          le1),
                      // loop 2
                      new IRMove(new IRTemp("_ctr2"), new IRConst(0)),
                      new IRSeq(
                          lh2,
                          new IRCJump(
                              new IRBinOp(IRBinOp.OpType.ULT, new IRTemp("_ctr2"), tn2),
                              l12.name(),
                              le2.name()),
                          l12,
                          new IRMove(
                              new IRTemp("_move"),
                              new IRMem(
                                  new IRBinOp(
                                      IRBinOp.OpType.ADD,
                                      i2comp,
                                      new IRBinOp(
                                          IRBinOp.OpType.MUL,
                                          new IRTemp("_ctr2"),
                                          new IRConst(8))))),
                          new IRMove(
                              new IRMem(
                                  new IRBinOp(
                                      IRBinOp.OpType.ADD,
                                      tm,
                                      new IRBinOp(
                                          IRBinOp.OpType.MUL,
                                          new IRBinOp(
                                              IRBinOp.OpType.ADD,
                                              new IRBinOp(
                                                  IRBinOp.OpType.ADD,
                                                  new IRTemp("_ctr1"),
                                                  new IRTemp("_ctr2")),
                                              new IRConst(1)),
                                          new IRConst(8)))),
                              new IRTemp("_move")),
                          new IRMove(
                              new IRTemp("_ctr2"),
                              new IRBinOp(IRBinOp.OpType.ADD, new IRTemp("_ctr2"), new IRConst(1))),
                          new IRJump(new IRName(lh2.name())),
                          le2)),
                  new IRBinOp(IRBinOp.OpType.ADD, tm, new IRConst(8)));
          return this;
        } else {
          irOp = IRBinOp.OpType.ADD;
          break;
        }
      case "-":
        irOp = IRBinOp.OpType.SUB;
        break;
      case "*":
        irOp = IRBinOp.OpType.MUL;
        break;
      case "*>>":
        irOp = IRBinOp.OpType.HMUL;
        break;
      case "/":
        irOp = IRBinOp.OpType.DIV;
        break;
      case "%":
        irOp = IRBinOp.OpType.MOD;
        break;
      case "&":
        irOp = IRBinOp.OpType.AND;
        break;
      case "|":
        irOp = IRBinOp.OpType.OR;
        break;
      case "<":
        irOp = IRBinOp.OpType.LT;
        break;
      case ">":
        irOp = IRBinOp.OpType.GT;
        break;
      case "<=":
        irOp = IRBinOp.OpType.LEQ;
        break;
      case ">=":
        irOp = IRBinOp.OpType.GEQ;
        break;
      case "==":
        irOp = IRBinOp.OpType.EQ;
        break;
      case "!=":
        irOp = IRBinOp.OpType.NEQ;
        break;
      default:
        throw new Error("unimplemented operator " + op);
    }
    iRTranslation = new IRBinOp(irOp, i1, i2);
    return this;
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();

    cw.printAtom(op);
    e1.prettyPrint(cw);
    e2.prettyPrint(cw);

    cw.endList();
  }

  public String toString() {
    return "(" + op + " " + e1 + " " + e2 + ")";
  }
}

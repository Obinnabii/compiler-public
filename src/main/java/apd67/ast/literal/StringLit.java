package apd67.ast;

import apd67.ir.IRBinOp;
import apd67.ir.IRCall;
import apd67.ir.IRConst;
import apd67.ir.IRESeq;
import apd67.ir.IRMem;
import apd67.ir.IRMove;
import apd67.ir.IRName;
import apd67.ir.IRSeq;
import apd67.ir.IRStmt;
import apd67.ir.IRTemp;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import java.util.LinkedList;

public class StringLit extends Literal {
  String value;
  private static int uniq = 0;

  public StringLit(String s) {
    value = s;
    setType(new ArrayType(new IntType(), null));
  }

  @Override
  public StringLit visitChildren(Visitor v) throws SemanticError {
    return this;
  }

  // gives the list of IR stmts associated with placing
  // each character in memory
  private LinkedList<IRStmt> genStmtLst(IRTemp tm, long n) {
    LinkedList<IRStmt> res = new LinkedList<IRStmt>();
    int offset = 8;
    res.add(
        new IRMove(
            tm,
            new IRCall(
                new IRName("_xi_alloc"),
                new IRBinOp(
                    IRBinOp.OpType.ADD,
                    new IRBinOp(IRBinOp.OpType.MUL, new IRConst(n), new IRConst(8)),
                    new IRConst(8)))));
    res.add(new IRMove(new IRMem(tm), new IRConst(n)));
    boolean slash = false;
    for (int i = 0; i < n; i++) {
      char c = value.charAt(i);
      if (c != '\\') {
        if (slash) {
          switch (c) {
            case 'n':
              c = '\n';
              break;
            case '\'':
              c = '\'';
              break;
            case '\"':
              c = '\"';
              break;
            case 'f':
              c = '\f';
              break;
            case 't':
              c = '\t';
              break;
            case 'r':
              c = '\r';
              break;
            case 'b':
              c = '\b';
              break;
            default:
              c = value.charAt(i);
              break;
          }
          slash = false;
        }
        res.add(
            new IRMove(
                new IRMem(new IRBinOp(IRBinOp.OpType.ADD, tm, new IRConst(offset))),
                new IRConst((long) c)));
        offset += 8;
      } else slash = true;
    }
    return res;
  }

  @Override
  public Node irTranslate(IRTranslator v) throws TypeException {
    long n = value.length();
    IRTemp tm = new IRTemp("_mstr_" + uniq++);
    LinkedList<IRStmt> memInit = genStmtLst(tm, n);
    iRTranslation =
        new IRESeq(new IRSeq(memInit), new IRBinOp(IRBinOp.OpType.ADD, tm, new IRConst(8)));
    return this;
  }

  public String toString() {
    return "\"" + value + "\"";
  }
}

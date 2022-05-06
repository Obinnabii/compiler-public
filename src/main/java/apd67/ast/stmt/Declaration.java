package apd67.ast;

import apd67.ir.*;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;

// Set line and col

public class Declaration extends Stmt implements Variable {
  Identifier id;
  Type declType;

  IRBinOp.OpType add = IRBinOp.OpType.ADD;
  IRBinOp.OpType mul = IRBinOp.OpType.MUL;

  public Declaration(Identifier id, Type t) {
    this.id = id;
    this.declType = t;
    setLineAndCol(id);
  }

  @Override
  public Declaration typeCheck(TypeChecker tc) throws SemanticError {
    Declaration d;

    try {
      d = (Declaration) this.clone();
    } catch (CloneNotSupportedException cnse) {
      cnse.printStackTrace();
      throw new Error(cnse.toString());
    }

    try {
      tc.context.add(id.toString(), declType);
      d.setType(new StatementType(true));
      return d;
    } catch (ContextException e) {
      throw new TypeException("Multiple identifiers: " + id.toString(), line, col);
    }
  }

  /**
   * recursively go over building up from the bottom. loop over elements of children, recursively do
   * that
   *
   * @param v
   * @param idxWrapper indices (in type of array)
   * @param baseAddr
   * @param layer
   * @param prevSize
   * @return
   */
  private IRStmt arrayInit(
      IRTranslator v, Type idxWrapper, IRExpr baseAddr, int layer, IRExpr prevSize) {
    // System.out.println(idxWrapper);
    if (!(idxWrapper instanceof ArrayType)) {
      return new IRSeq();
    } else {
      ArrayType arr = (ArrayType) idxWrapper;
      if (arr.size.isEmpty() || arr.size.get() == null) {
        return new IRSeq();
      }

      IRTemp tm = new IRTemp("_m_" + layer),
          tn = new IRTemp("_n_" + layer),
          loopCtr = new IRTemp("_loop_ctr_" + layer);
      IRLabel lh = v.makeGenericLabel(), l1 = v.makeTrueLabel(), le = v.makeFalseLabel();
      IRStmt loop;
      /*IRStmt nest =
      arrayInit(
          v,
          arr.base,
          new IRBinOp(add, tm, new IRConst(8)),
          layer + 1,
          (IRExpr) arr.size.get().getIRTranslation());*/
      loop =
          new IRSeq(
              new IRMove(loopCtr, new IRConst(0)),
              new IRMove(tn, (IRExpr) arr.size.get().getIRTranslation()),
              new IRSeq(
                  lh, // loop header
                  new IRCJump(
                      new IRBinOp(IRBinOp.OpType.ULT, loopCtr, prevSize), l1.name(), le.name()),
                  l1,
                  // S start
                  // array init -- repeated throughout loop
                  new IRSeq(
                      // initialize new inner array -- size = 8 * (curr arr dim) + 8
                      // move result of Call(xi_alloc) into tm
                      new IRMove(
                          tm,
                          new IRCall(
                              new IRName("_xi_alloc"),
                              new IRBinOp(
                                  add, new IRBinOp(mul, tn, new IRConst(8)), new IRConst(8)))),
                      new IRMove(
                          new IRMem(tm), tn), // store array size at beginning of alloc'd space
                      new IRMove(
                          new IRMem(
                              new IRBinOp(
                                  add, baseAddr, new IRBinOp(mul, loopCtr, new IRConst(8)))),
                          new IRBinOp(
                              add,
                              tm,
                              new IRConst(
                                  8)))), // move ptr to beginning of new alloc'd array into spot in
                  // parent array
                  arrayInit(
                      v,
                      arr.base,
                      new IRBinOp(add, tm, new IRConst(8)),
                      layer + 1,
                      (IRExpr) arr.size.get().getIRTranslation()),
                  new IRMove(loopCtr, new IRBinOp(add, loopCtr, new IRConst(1))),
                  // S end
                  new IRJump(new IRName(lh.name())),
                  le));
      return loop;
    }
  }

  // private IRSeq translateArrayType() {

  // }

  @Override
  public Node irTranslate(IRTranslator v) throws SemanticError {
    if (declType instanceof ArrayType) {
      ArrayType arr = (ArrayType) declType;
      if (arr.size.isEmpty()) {
        iRTranslation = new IRTemp(id.toString());
      } else {
        // System.out.println(arr);
        IRTemp tm = new IRTemp("_m_arrinit"), tn = new IRTemp("_n_arrinit");
        IRSeq baseInit =
            new IRSeq(
                new IRMove(tn, (IRExpr) arr.size.get().getIRTranslation()),
                new IRMove(
                    tm,
                    new IRCall(
                        new IRName("_xi_alloc"),
                        new IRBinOp(add, new IRBinOp(mul, tn, new IRConst(8)), new IRConst(8)))),
                new IRMove(new IRMem(tm), tn),
                new IRMove(new IRTemp(id.toString()), new IRBinOp(add, tm, new IRConst(8))));
        IRStmt multidimInit =
            arrayInit(
                v,
                arr.base,
                new IRBinOp(add, tm, new IRConst(8)),
                0,
                (IRExpr) arr.size.get().getIRTranslation());
        iRTranslation = new IRSeq(baseInit, multidimInit);
      }
    } else {
      iRTranslation = new IRTemp(id.toString());
    }
    return this;
  }

  @Override
  public Declaration visitChildren(Visitor v) throws SemanticError {
    // Identifier new_id = (Identifier) id.accept(v);
    Type new_declType = (Type) declType.accept(v);

    if (new_declType != declType) {
      return new Declaration(id, new_declType);
    } else {
      return this;
    }
  }

  public String getName() {
    return id.getName();
  }

  public Type getDeclType() {
    return declType;
  }

  /**
   * @param d
   * @return true if {@code d} produces the same method signature as {@code this}.
   */
  public boolean equivalent(Declaration d) {
    return getDeclType().equals(d.getDeclType());
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();

    id.prettyPrint(cw);
    declType.prettyPrint(cw);

    cw.endList();
  }
}

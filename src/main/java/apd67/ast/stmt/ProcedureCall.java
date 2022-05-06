package apd67.ast;

import apd67.ir.IRCallStmt;
import apd67.ir.IRExpr;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.util.exception.SemanticError;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;
import java.util.LinkedList;
import java.util.ListIterator;

// Set line and col

public class ProcedureCall extends Stmt {
  Identifier id;
  LinkedList<Expr> args;

  public ProcedureCall(Identifier id, LinkedList<Expr> args) {
    this.id = id;
    this.args = args;
    setLineAndCol(id);
  }

  @Override
  public ProcedureCall typeCheck(TypeChecker tc) throws SemanticError {
    ProcedureCall pc;
    try {
      pc = (ProcedureCall) this.clone();
    } catch (CloneNotSupportedException cnse) {
      throw new Error(cnse.toString());
    }

    Type type;
    try {
      type = tc.context.lookup(id.getName());
    } catch (ContextException ce) {
      throw new TypeException(ce.getMessage(), line, col);
    }

    if (!(type instanceof MethodTypeAndParams)) {
      throw new TypeException(id.getName() + " not a function", line, col);
    }

    MethodTypeAndParams t = (MethodTypeAndParams) type;
    if (t.getTypes().size() != 0) {
      throw new TypeException("Not a procedure call", line, col);
    }

    LinkedList<Declaration> params = t.getParams();

    if (params.size() != args.size()) {
      throw new TypeException("Expected " + params.size() + " args, got " + args.size(), line, col);
    }

    ListIterator<Declaration> paramsIt = params.listIterator();
    ListIterator<Expr> argsIt = args.listIterator();

    while (argsIt.hasNext()) {
      Expr e = argsIt.next();
      Declaration p = paramsIt.next();

      if (!(e.getType().expectedEquals(p.getDeclType()))) {
        throw new TypeException(
            "Expected " + p.getDeclType() + ", got " + e.getType().expectedType(), line, col);
      }
    }

    // set type to unit and return
    pc.setType(new StatementType(true));
    return pc;
  }

  @Override
  public Node irTranslate(IRTranslator v) throws SemanticError {
    LinkedList<IRExpr> iRExprs = new LinkedList<IRExpr>();
    for (Expr arg : args) iRExprs.add((IRExpr) arg.getIRTranslation());
    this.iRTranslation = new IRCallStmt(v.getFuncABIName(id), 0, iRExprs);
    return this;
  }

  @Override
  public ProcedureCall visitChildren(Visitor v) throws SemanticError {
    Identifier new_id = (Identifier) id.accept(v);

    boolean isDifferent = new_id != id;

    LinkedList<Expr> new_args = new LinkedList<>();
    for (Expr a : args) {
      Expr a_new = (Expr) a.accept(v);
      new_args.addLast(a_new);
      isDifferent = isDifferent || a_new != a;
    }

    if (isDifferent) {
      return new ProcedureCall(new_id, new_args);
    } else {
      return this;
    }
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();
    id.prettyPrint(cw);

    for (Expr arg : args) {
      arg.prettyPrint(cw);
    }

    cw.endList();
  }
}

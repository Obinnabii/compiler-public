package apd67.ast;

import apd67.ir.*;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;
import java.util.LinkedList;
import java.util.ListIterator;

// Set line and col

public class MethodCall extends Expr {
  Identifier id;
  LinkedList<Expr> args;

  public MethodCall(Identifier id, LinkedList<Expr> args) {
    this.id = id;
    this.args = args;
    setLineAndCol(id);
  }

  @Override
  public MethodCall typeCheck(TypeChecker tc) throws SemanticError {
    MethodCall mc;
    try {
      mc = (MethodCall) this.clone();
    } catch (CloneNotSupportedException cnse) {
      throw new Error(cnse.toString());
    }

    // hard coded exception
    if (id.getName() == "length") {
      if (args.size() != 1) {
        throw new TypeException("length expects 1 arg, found " + args.size(), line, col);
      }
      Expr e = args.getLast();
      if (e.getType().expectedArray()) {
        // update type
        mc.setType(new IntType());

        return mc;
      }

      throw new TypeException("length expects an array, got " + e.getType(), line, col);
    }

    MethodTypeAndParams t;
    try {
      Type temp = tc.context.lookup(id.getName()); // get function type
      if (!(temp instanceof MethodTypeAndParams)) {
        throw new TypeException(id.getName() + " not a function", line, col);
      }
      t = (MethodTypeAndParams) temp;
    } catch (ContextException ce) {
      throw new TypeException(ce.getMessage(), line, col);
    }

    // check arguments are as expected
    LinkedList<Declaration> decls = t.getParams();

    if (decls.size() != args.size()) {
      String msg =
          "Wrong number of arguments (expected " + decls.size() + ", found " + args.size() + ")";
      throw new TypeException(msg, line, col);
    }

    ListIterator<Expr> argIt = args.listIterator();
    ListIterator<Declaration> declIt = decls.listIterator();
    while (argIt.hasNext()) {
      Type decType = declIt.next().getDeclType();
      Type argType = argIt.next().getType().expectedType();
      if (!(decType.expectedEquals(argType))) {
        throw new TypeException("Expected " + decType + " found " + decType, line, col);
      }
    }

    // update type
    mc.setType(t);

    return mc;
  }

  @Override
  public Node irTranslate(IRTranslator v) throws SemanticError {
    LinkedList<IRExpr> iRExprs = new LinkedList<IRExpr>();
    for (Expr arg : args) iRExprs.add((IRExpr) arg.getIRTranslation());
    int numReturned = 1;
    if (this.type instanceof MethodTypeAndParams) {
      numReturned = ((MethodTypeAndParams) this.type).getTypes().size();
    }
    this.iRTranslation = new IRCall(v.getFuncABIName(id), numReturned, iRExprs);
    return this;
  }

  @Override
  public MethodCall visitChildren(Visitor v) throws SemanticError {
    Identifier new_id = (Identifier) id.accept(v);

    boolean isDifferent = new_id != id;

    LinkedList<Expr> new_args = new LinkedList<>();
    for (Expr d : args) {
      Expr d_new = (Expr) d.accept(v);
      new_args.addLast(d_new);
      isDifferent = isDifferent || d_new != d;
    }

    if (isDifferent) {
      return new MethodCall(new_id, new_args);
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

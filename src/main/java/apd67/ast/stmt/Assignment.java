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

public class Assignment extends Stmt {
  public LinkedList<Variable> vars;
  public Expr e;

  public Assignment(LinkedList<Variable> vars, Expr e) {
    this.vars = vars;
    this.e = e;
    Variable first = vars.peek();
    setLine(first.getLine());
    setColumn(first.getColumn());
  }

  private void typeCheckSingle(TypeChecker tc, Variable var, Type t, boolean isFuncCall)
      throws SemanticError {
    Type varT = null;
    if (var instanceof Declaration) {
      varT = ((Declaration) var).getDeclType();
    } else if (var instanceof ArrayAccess) {
      varT = ((ArrayAccess) var).getType();
    } else if (var instanceof Identifier) {
      try {
        varT = tc.context.lookup(((Identifier) var).getName());
      } catch (ContextException ce) {
        throw new TypeException(ce.getMessage(), line, col);
      }

    } else if (!(var instanceof UscoreDecl)) {
      throw new Error("Unreachable assignment case: not array or declaration");
    }

    // if underscore, don't need to worry about types matching
    if (!(var instanceof UscoreDecl)) {
      if (!(t.expectedEquals(varT))) {
        throw new TypeException("cannot assign " + t.expectedType() + " to " + varT, line, col);
      }
    } else {
      // only allow underscore on function calls
      if (!isFuncCall) {
        throw new TypeException("Expected function call", line, col);
      }
    }
  }

  @Override
  public Assignment typeCheck(TypeChecker tc) throws SemanticError {
    Assignment a;
    try {
      a = (Assignment) this.clone();
    } catch (CloneNotSupportedException cnse) {
      cnse.printStackTrace();
      throw new Error(cnse.toString());
    }

    if (vars.size() == 1) {
      if (vars.getFirst() instanceof UscoreDecl
          && e.getType() instanceof MethodTypeAndParams
          && vars.size() != ((MethodTypeAndParams) e.getType()).getTypes().size()) {
        String msg =
            "Expected "
                + vars.size()
                + " vars, got "
                + ((MethodTypeAndParams) e.getType()).getTypes().size();
        throw new TypeException(msg, line, col);
      }
      typeCheckSingle(tc, vars.getFirst(), e.getType(), e.getType() instanceof MethodTypeAndParams);

    } else if (vars.size() > 1) {
      if (!(e.getType() instanceof MethodTypeAndParams)) {
        throw new TypeException("Can only multi-assign to functions", line, col);
      }
      LinkedList<Type> returnType = ((MethodTypeAndParams) e.getType()).getTypes();

      if (returnType.size() != vars.size()) {
        throw new TypeException(
            "Expected " + vars.size() + " vars, got " + returnType.size(), line, col);
      }

      ListIterator<Type> returnTypeIt = returnType.listIterator();
      ListIterator<Variable> varIt = vars.listIterator();

      while (varIt.hasNext()) {
        Variable var = varIt.next();
        Type t = returnTypeIt.next();

        typeCheckSingle(tc, var, t, true);
      }
    }

    a.setType(new StatementType(true)); // unit type
    return a;
  }

  @Override
  public Node irTranslate(IRTranslator v) throws SemanticError {
    IRExpr eTrans = (IRExpr) e.getIRTranslation();
    LinkedList<IRStmt> stmts = new LinkedList<IRStmt>();
    int i = 0;

    if (eTrans instanceof IRCall) {
      IRCall eCall = (IRCall) eTrans;
      stmts.addLast(eCall.toStmt());
    }

    for (Variable var : vars) {
      IRNode varTrans = ((Node) var).getIRTranslation();
      if (var instanceof ArrayAccess) {
        ArrayAccess arr = (ArrayAccess) var;
        stmts.add(
            new IRMove(
                new IRMem(arr.getAddr()), eTrans instanceof IRCall ? v.makeRetTemp(i++) : eTrans));
      } else {
        stmts.add(
            new IRMove((IRExpr) varTrans, eTrans instanceof IRCall ? v.makeRetTemp(i++) : eTrans));
      }
      iRTranslation = new IRSeq(stmts);
    }
    return this;
  }

  @Override
  public Assignment visitChildren(Visitor v) throws SemanticError {
    Expr new_e = (Expr) e.accept(v);
    boolean isDifferent = new_e != e;

    LinkedList<Variable> new_vars = new LinkedList<>();

    for (Variable var : vars) {
      Variable new_var = (Variable) ((Node) var).accept(v);
      new_vars.addLast(new_var);
      isDifferent |= new_var != var;
    }

    if (isDifferent) {
      return new Assignment(new_vars, new_e);
    } else {
      return this;
    }
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();
    cw.printAtom("=");

    if (vars.size() > 1) {
      cw.startUnifiedList();
    }

    for (Variable var : vars) {
      var.prettyPrint(cw);
    }

    if (vars.size() > 1) {
      cw.endList();
    }

    // Print Lines
    e.prettyPrint(cw);

    cw.endList();
  }
}

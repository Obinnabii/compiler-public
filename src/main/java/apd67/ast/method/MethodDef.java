package apd67.ast;

import apd67.ir.*;
import apd67.ir.IRTemp;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;
import java.util.LinkedList;

// Set line and col

public class MethodDef extends Node {
  private Identifier id;
  private Block body;
  private MethodTypeAndParams typeAndParams;

  public MethodDef(
      Identifier id, LinkedList<Declaration> params, LinkedList<Type> types, Block body) {
    this.id = id;
    this.body = body;
    typeAndParams = new MethodTypeAndParams(params, types);
    setLineAndCol(id);
  }

  public MethodDef(Identifier id, MethodTypeAndParams typeAndParams, Block body) {
    this.id = id;
    this.body = body;
    this.typeAndParams = typeAndParams;
    setLineAndCol(id);
  }

  public MethodTypeAndParams getTypeAndParams() {
    return typeAndParams;
  }

  /** @return */
  public MethodDeclaration toDeclaration() {
    return new MethodDeclaration(id, typeAndParams);
  }

  @Override
  public MethodDef typeCheck(TypeChecker tc) throws SemanticError {
    MethodDef md; // might be unneeded, don't see any types assoc w/ method def
    try {
      md = (MethodDef) this.clone();
    } catch (CloneNotSupportedException cnse) {
      throw new Error(cnse.toString());
    }

    LinkedList<Type> returnTypes = typeAndParams.getTypes();
    // returns so body MUST be void
    if (returnTypes.size() != 0 && ((StatementType) body.getType()).isUnit()) {
      throw new TypeException("Must return in all cases", line, col);
    }

    return md;
  }

  @Override
  public Node irTranslate(IRTranslator v) throws SemanticError {
    LinkedList<IRStmt> paramsAndBodyIR = new LinkedList<>();
    int i = 0;
    IRStmt bodyIR = (IRStmt) body.getIRTranslation();

    if (((StatementType) body.getType()).isUnit()) {
      bodyIR = new IRSeq(bodyIR, new IRReturn());
      ((StatementType) body.getType()).setUnit(false);
    }

    for (Declaration decl : typeAndParams.getParams()) {
      IRTemp argTemp = v.makeArgTemp(i++);
      IRMove moveParam = new IRMove((IRExpr) decl.getIRTranslation(), argTemp);
      paramsAndBodyIR.addLast(moveParam);
    }
    paramsAndBodyIR.addLast(bodyIR);

    this.iRTranslation = new IRFuncDecl(v.getFuncABIString(id), new IRSeq(paramsAndBodyIR));
    return this;
  }

  @Override
  public MethodDef visitChildren(Visitor v) throws SemanticError {
    // Identifier new_id = (Identifier) id.accept(v);

    // boolean isDifferent = new_id != id;
    boolean isDifferent = false;

    if (v instanceof TypeChecker) {
      ((TypeChecker) v).context.enterScope();
    }

    MethodTypeAndParams new_typeAndParams = (MethodTypeAndParams) typeAndParams.accept(v);
    isDifferent = isDifferent || new_typeAndParams != typeAndParams;

    if (v instanceof TypeChecker) {
      try {
        ((TypeChecker) v).context.setCurrentReturnType(new_typeAndParams.getTypes());
      } catch (ContextException ce) {
        throw new TypeException(ce.getMessage(), line, col);
      }
    }

    Block new_body = (Block) body.accept(v);

    if (v instanceof TypeChecker) {
      ((TypeChecker) v).context.leaveScope();
    }

    if (isDifferent || new_body != body) {
      return new MethodDef(id, new_typeAndParams, new_body);
    } else {
      return this;
    }
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();
    id.prettyPrint(cw);

    // Params
    typeAndParams.prettyPrint(cw);

    // Body
    body.prettyPrint(cw);
    cw.endList();
  }
}

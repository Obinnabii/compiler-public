package apd67.ast;

import apd67.ir.*;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.*;
import java.util.LinkedList;

// set line and col
// TODO: pass in a program name...
public class Program extends ParseTree {
  LinkedList<Use> uses;
  LinkedList<MethodDef> methods;

  public Program(LinkedList<Use> uses, MethodDef md) {
    this.uses = uses;
    this.methods = new LinkedList<MethodDef>();
    methods.add(md);
    this.setLineAndCol(methods.peek());
  }

  public Program(LinkedList<Use> uses, LinkedList<MethodDef> methods) {
    this.uses = uses;
    this.methods = methods;
    this.setLineAndCol(methods.peek());
  }

  public Program(MethodDef md) {
    this(new LinkedList<Use>(), md);
  }

  public void appendMethod(MethodDef md) {
    methods.add(md);
  }

  @Override
  public Node irTranslate(IRTranslator v) throws SemanticError {
    iRTranslation = new IRCompUnit(this.name);
    for (MethodDef method : methods)
      ((IRCompUnit) iRTranslation).appendFunc((IRFuncDecl) method.getIRTranslation());
    return this;
  }

  @Override
  public Program visitChildren(Visitor v) throws SemanticError {
    boolean isDifferent = false;

    LinkedList<Use> new_uses = new LinkedList<>();
    for (Use u : uses) {
      Use u_new = (Use) u.accept(v);
      new_uses.addLast(u_new);
      isDifferent = isDifferent || u_new != u;
    }
    LinkedList<MethodDef> new_methods = new LinkedList<>();
    for (MethodDef md : methods) {
      MethodDef md_new = (MethodDef) md.accept(v);
      new_methods.addLast(md_new);
      isDifferent = isDifferent || md_new != md;
    }

    if (isDifferent) {
      return new Program(new_uses, new_methods);
    } else {
      return this;
    }
  }

  @Override
  public LinkedList<Use> getUses() {
    return uses;
  }

  public LinkedList<MethodDef> getMethods() {
    return methods;
  }

  @Override
  public LinkedList<MethodDeclaration> getMethodDeclarations() {
    LinkedList<MethodDeclaration> declarations = new LinkedList<MethodDeclaration>();
    for (MethodDef md : getMethods()) {
      declarations.add(md.toDeclaration());
    }
    return declarations;
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();

    // Print use statements
    cw.startList();
    for (Use use : uses) {
      use.prettyPrint(cw);
    }
    cw.endList();

    // Print Lines
    cw.startUnifiedList();
    for (MethodDef md : methods) {
      md.prettyPrint(cw);
    }
    cw.endList();

    cw.endList();
    cw.close();
  }
}

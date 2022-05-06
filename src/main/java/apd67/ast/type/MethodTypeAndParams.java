package apd67.ast;

import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.semantic.*;
import java.util.LinkedList;

public class MethodTypeAndParams extends Type {
  private LinkedList<Declaration> params;
  /** The return type of the method */
  private LinkedList<Type> types;

  public MethodTypeAndParams(LinkedList<Declaration> params, LinkedList<Type> types) {
    this.params = params;
    this.types = types;
    setABIRepresentation();
  }

  public void setABIRepresentation() {
    int size = types.size();
    ABIRepresentation = (size == 0) ? "_p" : ((size > 1) ? "_t" + size : "_");
    for (Type t : types) ABIRepresentation += t.getABIRepresentation();
    for (Declaration d : params) ABIRepresentation += d.getDeclType().getABIRepresentation();
  }

  @Override
  public MethodTypeAndParams visitChildren(Visitor v) throws SemanticError {
    boolean isDifferent = false;

    LinkedList<Declaration> new_params = new LinkedList<>();
    for (Declaration d : params) {
      Declaration d_new = (Declaration) d.accept(v);
      new_params.addLast(d_new);
      isDifferent = isDifferent || d_new != d;
    }
    LinkedList<Type> new_types = new LinkedList<>();
    for (Type t : types) {
      Type t_new = (Type) t.accept(v);
      new_types.addLast(t_new);
      isDifferent = isDifferent || t_new != t;
    }

    return isDifferent ? new MethodTypeAndParams(new_params, new_types) : this;
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    // Params
    cw.startList();
    for (Declaration param : params) {
      param.prettyPrint(cw);
    }
    cw.endList();

    // Return Type
    cw.startList();
    for (Type type : types) {
      type.prettyPrint(cw);
    }
    cw.endList();
  }

  public LinkedList<Declaration> getParams() {
    return params;
  }

  public LinkedList<Type> getTypes() {
    return types;
  }

  public boolean isProcedure() {
    return types.size() == 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof MethodTypeAndParams) {
      MethodTypeAndParams mt = (MethodTypeAndParams) o;
      LinkedList<Type> mtTypes = mt.getTypes();
      LinkedList<Declaration> mtParams = mt.getParams();
      // Same size
      if (types.size() != mtTypes.size() && params.size() != mtParams.size()) return false;

      // return Types
      for (int i = 0; i < types.size(); i++) {
        if (!types.get(i).equals(mtTypes.get(i))) return false;
      }

      // params
      for (int i = 0; i < params.size(); i++) {
        if (!params.get(i).equivalent(mtParams.get(i))) return false;
      }

      return true;
    }
    return false;
  }
}

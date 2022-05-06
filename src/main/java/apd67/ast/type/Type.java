package apd67.ast;

import java.util.LinkedList;

public abstract class Type extends Node {
  protected String ABIRepresentation;

  public boolean expectedArray() {
    if (this instanceof MethodTypeAndParams) {
      LinkedList<Type> returnType = ((MethodTypeAndParams) this).getTypes();
      if (returnType.size() != 1) return false;
      Type t = returnType.getFirst();
      return t.expectedArray();
    }
    if (this instanceof ArrayType) return true;

    return false;
  }

  public Type expectedType() {
    if (this instanceof MethodTypeAndParams) {
      LinkedList<Type> returnType = ((MethodTypeAndParams) this).getTypes();
      if (returnType.size() == 1) return returnType.getFirst();
      ;
      return this;
    }
    return this;
  }

  public boolean expectedEquals(Type t) {
    Type tThis = this;
    Type tThat = t;
    if (this instanceof MethodTypeAndParams) {
      LinkedList<Type> returnType = ((MethodTypeAndParams) this).getTypes();
      if (returnType.size() != 1) return false;
      tThis = returnType.getFirst();
    }
    if (t instanceof MethodTypeAndParams) {
      LinkedList<Type> returnType = ((MethodTypeAndParams) t).getTypes();
      if (returnType.size() != 1) return false;
      tThat = returnType.getFirst();
    }
    // TODO: add case where both are methodTypeAndParams
    return tThis.equals(tThat);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (this instanceof IntType) return o instanceof IntType;
    if (this instanceof BoolType) return o instanceof BoolType;
    // ArrayType overridden in ArrayType.java
    return false;
  }

  /**
   * @return How this type is represented according to XI ABI Spec. For MethodType and params, it
   *     returns the suffix of the method reffered to by this type. <br>
   *     Found here: {@link
   *     https://www.cs.cornell.edu/courses/cs4120/2021sp/project/abi.pdf?1617476008}
   */
  public String getABIRepresentation() {
    return ABIRepresentation;
  }
}

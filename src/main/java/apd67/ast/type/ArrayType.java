package apd67.ast;

import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.visitor.Visitor;
import apd67.visitor.semantic.*;
import java.util.LinkedList;
import java.util.Optional;

public class ArrayType extends Type {
  public Type base;
  public Optional<Expr> size = Optional.empty();
  private boolean isEmpty = false;

  public ArrayType(Type base, Expr size) {
    this.base = base;
    this.size = Optional.ofNullable(size);
    setLineAndCol(base);
    ABIRepresentation = "a" + base.getABIRepresentation();
  }

  public ArrayType(Type base, Expr size, boolean isEmpty) {
    this(base, size);
    this.isEmpty = isEmpty;
  }

  /**
   * @param base - the base type of the ArrayType
   * @param sizes - a list of maxIndices in this fashion;<br>
   *     int[1][2][3][] -> [null, 3, 2, 1]
   * @return the proper ArrayType object to represent the arguments provided.
   */
  public static ArrayType fromList(Type base, LinkedList<Expr> maxIndices) {
    ArrayType b = new ArrayType(base, maxIndices.pop());
    while (!maxIndices.isEmpty()) {
      b = new ArrayType(b, maxIndices.pop());
    }
    return b;
  }

  @Override
  public ArrayType typeCheck(TypeChecker tc) throws TypeException {
    if (size.isPresent() && !size.get().getType().expectedEquals(new IntType())) {
      Expr size_ = size.get();
      throw new TypeException(
          "Expected int, found " + size_.getType(), size_.getLine(), size_.getColumn());
    }
    return this;
  }

  @Override
  public ArrayType visitChildren(Visitor v) throws SemanticError {
    Type new_base = (Type) base.accept(v);
    boolean isDifferent = new_base != base;

    Expr new_size;
    if (size.isPresent()) {
      new_size = (Expr) size.get().accept(v);
      isDifferent |= new_size != size.get();

      if (isDifferent) {
        return new ArrayType(new_base, new_size);
      }
    }

    if (isDifferent) {
      return new ArrayType(new_base, null);
    } else {
      return this;
    }
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();
    cw.printAtom("[]");
    base.prettyPrint(cw);

    if (size.isPresent()) {
      size.get().prettyPrint(cw);
    }

    cw.endList();
  }

  public String toString() {
    String maxIndeString = size.isPresent() ? " " + size.get().toString() : "";
    return "([] " + base + maxIndeString + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o instanceof ArrayType) {
      if (((ArrayType) o).isEmpty || this.isEmpty) return true;
      return base.equals(((ArrayType) o).base);
    }
    return false;
  }
}

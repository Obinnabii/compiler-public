package apd67.ast;

import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.visitor.Visitor;

public class StatementType extends Type {
  private boolean isUnit;

  public StatementType(Boolean isUnit) {
    super();
    this.isUnit = isUnit;
    ABIRepresentation = "stmt";
  }

  public boolean isUnit() {
    return isUnit;
  }

  public void setUnit(Boolean isUnit) {
    this.isUnit = isUnit;
  }

  @Override
  public StatementType visitChildren(Visitor v) {
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o instanceof StatementType) return ((StatementType) o).isUnit == isUnit;
    return false;
  }

  public String toString() {
    return isUnit ? "unit" : "void";
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {}
}

package apd67.ast;

import apd67.ir.IRNode;
import apd67.util.cs4120.CodeWriterSExpPrinter;

public interface Variable {

  public void prettyPrint(CodeWriterSExpPrinter cw);

  public int getLine();

  public int getColumn();

  public IRNode getIRTranslation();

  public boolean isUscore();
}

package apd67.ast;

import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.visitor.Visitor;

public class Use extends Node {
  Identifier id;

  public Use(Identifier id) {
    this.id = id;
  }

  public String getName() {
    return id.getName();
  }

  @Override
  public Use visitChildren(Visitor v) throws SemanticError {
    // Identifier new_id = (Identifier) id.accept(v);
    // if (new_id != id) {
    //   return new Use(new_id);
    // } else {
    return this;
    // }
  }

  public void prettyPrint(CodeWriterSExpPrinter cw) {
    cw.startUnifiedList();

    cw.printAtom("use");
    id.prettyPrint(cw);

    cw.endList();
  }
}

package apd67.ir.interpret;

import apd67.ir.IRBinOp;
import apd67.ir.IRBinOp.OpType;
import apd67.ir.IRCallStmt;
import apd67.ir.IRCompUnit;
import apd67.ir.IRConst;
import apd67.ir.IRFuncDecl;
import apd67.ir.IRMove;
import apd67.ir.IRName;
import apd67.ir.IRNode;
import apd67.ir.IRNodeFactory_c;
import apd67.ir.IRReturn;
import apd67.ir.IRSeq;
import apd67.ir.IRStmt;
import apd67.ir.IRTemp;
import apd67.ir.parse.IRLexer;
import apd67.ir.parse.IRParser;
import apd67.ir.visit.CheckCanonicalIRVisitor;
import apd67.ir.visit.CheckConstFoldedIRVisitor;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.cs4120.SExpPrinter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class Interpreter {

  public static void main(String[] args) {
    runDemo(constructExampleIR1());
    System.out.println();
    System.out.println();
    System.out.println();
    runDemo(constructExampleIR2());
  }

  /**
   * An example for you to learn how to use our IR AST, parser, interpreter. It will first pretty
   * print the constructed IR, runs some simulation, makes some canonical checks, and then parse the
   * pretty-printed code back.
   */
  private static void runDemo(IRCompUnit compUnit) {
    // IR pretty-printer demo
    System.out.println("Code:");
    String prettyPrintedProgram = prettyPrint(compUnit);
    System.out.println(prettyPrintedProgram);

    // IR interpreter demo
    {
      IRSimulator sim = new IRSimulator(compUnit);
      long result = sim.call("b", 2, 1);
      System.out.println("b(2,1) == " + result);
    }

    // IR canonical checker demo
    {
      CheckCanonicalIRVisitor cv = new CheckCanonicalIRVisitor();
      System.out.print("Canonical?: ");
      System.out.println(cv.visit(compUnit));
    }

    // IR constant-folding checker demo
    {
      CheckConstFoldedIRVisitor cv = new CheckConstFoldedIRVisitor();
      System.out.print("Constant-folded?: ");
      System.out.println(cv.visit(compUnit));
    }

    // IR parser demo: parse the code printed above back
    IRCompUnit compUnit2 = parse(prettyPrintedProgram);
    if (compUnit2 != null) {
      IRSimulator sim = new IRSimulator(compUnit2);
      long result = sim.call("b", 2, 1);
      System.out.println("b(2,1) == " + result);
    }
  }

  /** Construct the IR by Java Code */
  private static IRCompUnit constructExampleIR1() {
    // IR roughly corresponds to the following:
    //     a(i:int, j:int): int, int {
    //         return i, (2 * j);
    //     }
    //     b(i:int, j:int): int {
    //         x:int, y:int = a(i, j);
    //         return x + 5 * y;
    //     }

    // b(2, 1) {
    //   x, y = 2, 2
    //   return 2 + 5 * 2 // 12;
    //

    String arg0 = Configuration.ABSTRACT_ARG_PREFIX + 0;
    String arg1 = Configuration.ABSTRACT_ARG_PREFIX + 1;
    String ret0 = Configuration.ABSTRACT_RET_PREFIX + 0;
    String ret1 = Configuration.ABSTRACT_RET_PREFIX + 1;

    IRStmt aBody =
        new IRSeq(
            new IRMove(new IRTemp("i"), new IRTemp(arg0)),
            new IRMove(new IRTemp("j"), new IRTemp(arg1)),
            new IRReturn(
                new IRTemp("i"), new IRBinOp(OpType.MUL, new IRConst(2), new IRTemp("j"))));
    IRFuncDecl aFunc = new IRFuncDecl("_Ia_t2iiii", aBody);

    IRStmt bBody =
        new IRSeq(
            new IRCallStmt(new IRName("_Ia_t2iiii"), new IRTemp(arg0), new IRTemp(arg1)),
            new IRMove(new IRTemp("x"), new IRTemp(ret0)),
            new IRMove(new IRTemp("y"), new IRTemp(ret1)),
            new IRReturn(
                new IRBinOp(
                    OpType.ADD,
                    new IRTemp("x"),
                    new IRBinOp(OpType.MUL, new IRConst(5), new IRTemp("y")))));
    IRFuncDecl bFunc = new IRFuncDecl("b", bBody);

    IRCompUnit compUnit = new IRCompUnit("test");
    compUnit.appendFunc(aFunc);
    compUnit.appendFunc(bFunc);
    return compUnit;
  }

  /** Construct the IR by S-expressions */
  private static IRCompUnit constructExampleIR2() {
    return parse("(COMPUNIT test (FUNC b (SEQ (RETURN (CONST 42)))))");
  }

  public static String prettyPrint(IRNode compUnit) {
    StringWriter sw = new StringWriter();
    try (PrintWriter pw = new PrintWriter(sw);
        SExpPrinter sp = new CodeWriterSExpPrinter(pw)) {
      compUnit.printSExp(sp);
    }
    return sw.toString();
  }

  public static IRCompUnit parse(String prog) {
    try (StringReader r = new StringReader(prog)) {
      IRParser parser = new IRParser(new IRLexer(r), new IRNodeFactory_c());
      try {
        return parser.parse().<IRCompUnit>value();
      } catch (RuntimeException e) {
        throw e;
      } catch (Exception e) {
        // Used by CUP to indicate an unrecoverable error.
        String msg = e.getMessage();
        if (msg != null) System.err.println("Syntax error: " + msg);
        return null;
      }
    }
  }
}

package apd67.cli;

import apd67.assembly.AssemblyBuilder;
import apd67.ast.*;
import apd67.ir.*;
import apd67.ir.interpret.IRSimulator;
import apd67.ir.interpret.Interpreter;
import apd67.ir.visit.*;
import apd67.lexer.XiLexer;
import apd67.parser.*;
import apd67.util.cs4120.CodeWriterSExpPrinter;
import apd67.util.exception.*;
import apd67.util.polyglot.OptimalCodeWriter;
import apd67.visitor.ir.IRTranslator;
import apd67.visitor.semantic.FunctionCollector;
import apd67.visitor.semantic.TypeChecker;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java_cup.runtime.*;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;

public class Main {
  private static List<String> extensions = Arrays.asList("xi", "ixi");
  private static String outDir = "./";
  private static String srcDir = "./";
  private static String libDir = "./";
  private static String asmDir = "./";
  private static String wellTypedString = "Valid Xi Program";
  private static Cli cli;

  public static void main(String[] args) throws Exception {
    // Make a Cli instance
    cli = new Cli();

    // Parse the user defined arguments
    try {
      cli.parseArgs(args);
      outDir = cli.getOutputDir();
      srcDir = cli.getSrcPath();
      libDir = cli.getLibPath();
      asmDir = cli.getAssemblyPath();
    } catch (ParseException exn) {
      System.out.println(exn.getMessage());
      System.exit(1);
    }

    // #### --report-opts ####
    if (cli.hasOption(Cli.reportOptimsOption)) {
      System.out.println("dce\ncopy\ncf\ncse\n");
    }

    // #### --help ####
    else if (cli.hasOption(Cli.helpOption) || cli.poorlyFormed()) {
      cli.printHelp();

      // well-formed arguments
    } else {
      // Get the files
      String[] files = cli.getFiles();

      for (int i = 0; i < files.length; i++) {
        // Instantiate a file reader
        String filepath = srcDir + files[i];
        boolean isIxi = FilenameUtils.isExtension(filepath, "ixi");

        if (!FilenameUtils.isExtension(filepath, extensions)) {
          System.out.println("Error: " + filepath + " is not a xi or ixi file.");
          continue;
        }

        FileReader fr;
        try {
          fr = new FileReader(filepath);
        } catch (FileNotFoundException e) {
          System.out.println("Error: No file named " + filepath + " was found.");
          continue;
        }

        // #### --lex ####
        if (cli.hasOption(Cli.lexOption)) {
          FileWriter fw = makeFileWriter(filepath, ".lexed");
          lexFile(fr, fw, isIxi);

        }

        // #### --parse ####
        else if (cli.hasOption(Cli.parseOption)) {
          CodeWriterSExpPrinter cw = makeCodeWriter(filepath, ".parsed");
          parseFile(fr, cw, isIxi, filepath);
        }

        // #### --typecheck ####
        else if (cli.hasOption(Cli.typecheckOption)) {
          CodeWriterSExpPrinter cw = makeCodeWriter(filepath, ".typed");
          try {
            typeCheckFile(fr, isIxi, filepath);
            cw.printAtom(wellTypedString);
            cw.close();
          } catch (XiError xe) {
            cw.printAtom(xe.getFormattedMessage());
            cw.close();
          }
        }

        // #### --irgen ####
        else if (cli.hasOption(Cli.irgenOption)) {
          CodeWriterSExpPrinter cw = makeCodeWriter(filepath, ".ir");
          IRCompUnit irTree = null;

          try {
            irTree = getIRTree(fr, isIxi, filepath);
          } catch (XiError xe) {
            cw.printAtom(xe.getFormattedMessage());
            cw.close();
            System.exit(1);
          }

          // System.out.println(Interpreter.prettyPrint(irTree));

          IRCompUnit irLowered = getLoweredIR(irTree);

          String sLow = Interpreter.prettyPrint(irLowered);
          cw.printAtom(sLow);
          cw.close();

        }
        // #### --irrun ####
        else if (cli.hasOption(Cli.irrunOption)) {

          CodeWriterSExpPrinter cw = makeCodeWriter(filepath, ".ir");
          IRCompUnit irTree = null;

          try {
            irTree = getIRTree(fr, isIxi, filepath);

            // cw.close();
          } catch (XiError xe) {
            cw.printAtom(xe.getFormattedMessage());
            cw.close();
            System.exit(1);
          }

          IRCompUnit irLowered = getLoweredIR(irTree);

          // #### --irsim ####
          if (cli.hasOption(Cli.irsimOption)) {
            IRSimulator sim = new IRSimulator(irLowered);
            sim.call("_Imain_paai", 0);
          } else {
            System.exit(2);
          }

        }

        // #### Default Behavior ####
        else {
          try {
            // typeCheckFile(fr, isIxi, filepath);
            IRCompUnit irTree = null;
            irTree = getIRTree(fr, isIxi, filepath);

            IRCompUnit irLowered = getLoweredIR(irTree, filepath);

            AssemblyBuilder ab = new AssemblyBuilder(irLowered);
            ab.allocateRegisters();
            String text = ab.toString();

            // code to write to file goes here
            FileWriter fw = makeFileWriter(filepath, ".s", asmDir);
            fw.write(text);
            fw.close();

          } catch (XiError xe) {
            System.out.println(xe.getTypecheckerFormattedMessage());
          }
        }
      }
    }
  }

  // helper needed b/c if any specific optimization enabled, others off by default
  private static boolean specificOptEnabled() {
    return cli.hasOption(Cli.optCopyPropOption)
        || cli.hasOption(Cli.optDeadCodeElimOption)
        || cli.hasOption(Cli.optConstFoldOption)
        || cli.hasOption(Cli.optCSEOption);
  }

  private static IRCompUnit getLoweredIR(IRCompUnit irTree) {
    // #### -O ####
    if (!cli.hasOption(Cli.disableOptOption)) {
      irTree = new ConstFoldVisitor().constFold(irTree);
    }

    LowerVisitor lv = new LowerVisitor();
    IRCompUnit irLowered = lv.translate_(irTree);

    irLowered.reorder();

    return irLowered;
  }

  private static IRCompUnit getLoweredIR(IRCompUnit irTree, String filepath) throws IOException {
    // generate initial cfg/ir if needed
    // if ()
    //     || (cli.hasOption(Cli.dotGenOption) && cli.getCFGPhase().equals("initial"))) {
    // generate and save tree
    // i am worried this lower will affect irTree later
    // but we need this b/c we do constant folding before lowering

    // generate .dot file
    if (cli.hasOption(Cli.dotGenOption) && cli.getCFGPhase().equals("initial")) {
      LowerVisitor lv = new LowerVisitor();
      IRCompUnit irLowered = lv.translate_(irTree);
      // to be implemented
      String fn = FilenameUtils.removeExtension(filepath);
      CodeWriterSExpPrinter cw = makeCodeWriter(fn + "_initial", ".dot");
      String sDot = irLowered.toDot();
      cw.printAtom(sDot);
      cw.close();
    }
    if (cli.hasOption(Cli.optIRGenOption) && cli.getIRPhase().equals("initial")) {
      // generate ir
      LowerVisitor lv = new LowerVisitor();
      IRCompUnit irLowered = lv.translate_(irTree);

      String fn = FilenameUtils.removeExtension(filepath);
      CodeWriterSExpPrinter cw = makeCodeWriter(fn + "_initial", ".ir");
      String sLow = Interpreter.prettyPrint(irLowered);
      cw.printAtom(sLow);
      cw.close();
    }

    // #### -O ####
    // must not have -O flag and either have no specific opt,
    // or this specific opt (if others are active)
    boolean constFold =
        !cli.hasOption(Cli.disableOptOption)
            && (!specificOptEnabled()
                || (specificOptEnabled() && cli.hasOption(Cli.optConstFoldOption)));
    if (constFold) {
      irTree = new ConstFoldVisitor().constFold(irTree);
    }

    LowerVisitor lv = new LowerVisitor();
    IRCompUnit irLowered = lv.translate_(irTree);

    boolean cse =
        !cli.hasOption(Cli.disableOptOption)
            && (!specificOptEnabled() || (specificOptEnabled() && cli.hasOption(Cli.optCSEOption)));
    if (cse) {
      // apply common subexpression elim
      irLowered.cse();
    }

    boolean copyProp =
        !cli.hasOption(Cli.disableOptOption)
            && (!specificOptEnabled()
                || (specificOptEnabled() && cli.hasOption(Cli.optCopyPropOption)));
    if (copyProp) {
      // apply copy prop analysis
      irLowered.copyProp();
    }

    boolean deadCodeElim =
        !cli.hasOption(Cli.disableOptOption)
            && (!specificOptEnabled()
                || (specificOptEnabled() && cli.hasOption(Cli.optDeadCodeElimOption)));
    if (deadCodeElim) {
      // apply dead code elim analysis
      irLowered.eliminateDeadCode();
    }

    if (cli.hasOption(Cli.optIRGenOption) && cli.getIRPhase().equals("final")) {
      // write ir
      System.out.println("writing to file: " + filepath);
      String fn = FilenameUtils.removeExtension(filepath);
      CodeWriterSExpPrinter cw = makeCodeWriter(fn + "_final", ".ir");
      String sLow = Interpreter.prettyPrint(irLowered);
      cw.printAtom(sLow);
      cw.close();
    }

    if (cli.hasOption(Cli.dotGenOption) && cli.getCFGPhase().equals("final")) {
      // write cfg .dot
      String fn = FilenameUtils.removeExtension(filepath);
      CodeWriterSExpPrinter cw = makeCodeWriter(fn + "_final", ".dot");
      String sDot = irLowered.toDot();
      cw.printAtom(sDot);
      cw.close();
    }

    // NOTE: Always do this after optimization;
    irLowered.reorder();

    return irLowered;
  }

  private static IRCompUnit getIRTree(FileReader fr, boolean isIxi, String filepath)
      throws XiError, Exception {
    try {
      ParseTree parseTree = makeParseTree(fr, isIxi, filepath);

      TypeChecker tc = collectFunctions(parseTree, filepath);
      ParseTree parseTreeTyped = (ParseTree) parseTree.accept(tc);

      IRTranslator irt = tc.toIRTranslator();
      ParseTree irTree = (ParseTree) parseTreeTyped.accept(irt);
      IRCompUnit ir = (IRCompUnit) irTree.getIRTranslation();

      if (irt.usedLength()) {
        // add length definition to ir iff program calls length
        ir.appendLength();
      }

      return ir;
    } catch (XiError xe) {
      throw xe.optionalySetFilename(FilenameUtils.getName(filepath));
    }
  }

  private static ParseTree typeCheckFile(FileReader fr, boolean isIxi, String filepath)
      throws XiError, Exception {
    try {
      ParseTree parseTree = makeParseTree(fr, isIxi, filepath);
      TypeChecker tc = collectFunctions(parseTree, filepath);
      ParseTree parseTreeTyped = (ParseTree) parseTree.accept(tc);
      return parseTreeTyped;
    } catch (XiError xe) {
      throw xe.optionalySetFilename(FilenameUtils.getName(filepath));
    }
  }

  private static void parseFile(
      FileReader fr, CodeWriterSExpPrinter cw, boolean isIxi, String filepath) throws Exception {
    try {
      Node parseTree = makeParseTree(fr, isIxi, filepath);
      parseTree.prettyPrint(cw);

    } catch (XiError xe) {
      cw.printAtom(xe.getFormattedMessage());
      cw.close();
    }
  }

  private static void lexFile(FileReader fr, FileWriter fw, boolean isIxi) throws IOException {
    XiLexer lex = new XiLexer(fr, isIxi);
    while (true) {
      try {
        Symbol t = lex.next_token();
        if (t == null || t.sym == apd67.parser.sym.EOF) {
          break;
        }
        fw.write(t + "\n");
        fw.flush();
      } catch (LexerException e) {
        fw.write(e.getFormattedMessage());
        fw.flush();
        break;
      } catch (Throwable e) {
        fw.write(e.getMessage());
        fw.flush();
        break;
      }
    }
  }

  private static FileWriter makeFileWriter(String filepath, String extension, String dir)
      throws IOException {
    filepath = FilenameUtils.removeExtension(filepath);
    String outputpath = dir + filepath + extension;

    if (outputpath.lastIndexOf('/') != -1) {
      String parentdirpath = outputpath.substring(0, outputpath.lastIndexOf('/'));
      File parentdir = new File(parentdirpath);
      parentdir.mkdirs();
    }

    File outfile = new File(outputpath);
    outfile.createNewFile();
    return new FileWriter(outfile);
  }

  private static FileWriter makeFileWriter(String filepath, String extension) throws IOException {
    return makeFileWriter(filepath, extension, outDir);
  }

  private static TypeChecker collectFunctions(ParseTree pt, String filepath) throws Exception {
    FunctionCollector fc = new FunctionCollector();
    String filename = FilenameUtils.getName(filepath);

    for (Use u : pt.getUses()) {
      String uname = u.getName() + ".ixi";
      String upath = libDir + uname;
      FileReader fr;
      try {
        fr = new FileReader(upath);
      } catch (FileNotFoundException e) {
        throw new TypeException("Interface " + uname + " not found", u.getLine(), u.getColumn())
            .setFilename(filename);
      }
      ParseTree usePt = makeParseTree(fr, true, upath);
      fc.collectFunctions(usePt, uname);
    }

    fc.collectFunctions(pt, filepath);
    return fc.toTypeChecker();
  }

  private static CodeWriterSExpPrinter makeCodeWriter(String filepath, String extension)
      throws IOException {
    return cli.hasOption(Cli.parsePrintOption)
        ? new CodeWriterSExpPrinter(new OptimalCodeWriter(new PrintWriter(System.out), 80))
        : new CodeWriterSExpPrinter(new OptimalCodeWriter(makeFileWriter(filepath, extension), 80));
  }

  private static ParseTree makeParseTree(FileReader fr, boolean isIxi, String filepath)
      throws Exception {
    parser parse = new parser(new XiLexer(fr, isIxi));
    return ((ParseTree) parse.parse().value).setName(FilenameUtils.getName(filepath));
  }
}

// NOTE: look into filepath

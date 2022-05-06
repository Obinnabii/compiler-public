package apd67.cli;

import apd67.lexer.*;
import apd67.parser.*;
import apd67.util.exception.*;
import java.io.*;
import java_cup.runtime.*;
import org.apache.commons.cli.*;

public class Cli {
  private Options options;
  private CommandLine cmd;
  /** A formatter that is configured to print the help message;d */
  private HelpFormatter hf = new HelpFormatter();

  // string representations of command line arguments and descriptions
  private String xicUsageString = "xic [options] <source files>";
  public static final String helpOption = "h";
  private static final String helpOptionDescription = "Print a synopsis of options.";

  public static final String lexOption = "l";
  public static final String lexOptionDescription = "Generate output from lexical analysis.";

  public static final String parseOption = "p";
  public static final String parseOptionDescription = "Generate output from syntactical analysis.";

  public static final String parsePrintOption = "pr";
  public static final String parsePrintOptionDescription =
      "Print syntactical analysis output to the command line.";

  public static final String dirOption = "D";
  public static final String dirOptionDescription =
      "Specify where to place generated diagnostic files i.e. <path>.";

  public static final String srcPathOption = "sourcepath";
  public static final String srcPathOptionDescription = "Specify where to find input source files.";

  public static final String libPathOption = "libpath";
  public static final String libPathOptionDescription =
      "Specify where to find library interface files.";

  public static final String typecheckOption = "tc";
  public static final String typecheckOptionDescription = "Generate output from semantic analysis.";

  public static final String disableOptOption = "O";
  public static final String disableOptOptionDescription = "Disable all optimizations.";

  public static final String targetOption = "target";
  public static final String targetOptionDescription =
      "Specify the operating system for which to generate code (default: Linux). Other OS's not"
          + " supported.";

  public static final String assemblyPathOption = "d";
  public static final String assemblyPathOptionDescription =
      "Specify where to place the generated .s assembly output files";

  public static final String irgenOption = "irg";
  public static final String irgenOptionDescription = "Generate intermediate code.";

  public static final String irrunOption = "irr";
  public static final String irrunOptionDescription = "Generate and interpret intermediate code.";

  public static final String irsimOption = "sim";
  public static final String irsimOptionDescription = "Simulate intermediate code.";

  public static final String optIRGenOption = "optir";
  public static final String optIRGenOptionDescription =
      "Report the IR at the specified phase of optimization. Supported phases: initial, final";

  public static final String dotGenOption = "optcfg";
  public static final String dotGenOptionDescription =
      "Report the control-flow graph at the specified phase of optimization. Supported phases:"
          + " initial, final";

  public static final String reportOptimsOption = "report-opts";
  public static final String reportOptimsOptionDescription =
      "List of optimizations supported by the compiler.";

  public static final String optCopyPropOption = "Ocopy";
  public static final String optCopyPropOptionDescription = "Enable copy propogation.";

  public static final String optDeadCodeElimOption = "Odce";
  public static final String optDeadCodeElimOptionDescription = "Enable dead code elimination.";

  public static final String optConstFoldOption = "Ocf";
  public static final String optConstFoldOptionDescription = "Enable constant folding.";

  public static final String optCSEOption = "Ocse";
  public static final String optCSEOptionDescription = "Enable constant folding.";

  public static final String optRegisterOption = "Oreg";
  public static final String optRegisterOptionDescription =
      "Enable register allocation(not supported).";

  /**
   * Constructs a new command line interface object. The Cli must then be passed the user specified
   * arguments using the {@code parseArgs} method in order to work correctly
   */
  public Cli() {
    options = new Options();

    options.addOption(helpOption, "help", false, helpOptionDescription);
    options.addOption(lexOption, "lex", false, lexOptionDescription);
    options.addOption(parseOption, "parse", false, parseOptionDescription);
    options.addOption(typecheckOption, "typecheck", false, typecheckOptionDescription);
    options.addOption(irgenOption, "irgen", false, irgenOptionDescription);
    options.addOption(irrunOption, "irrun", false, irrunOptionDescription);
    options.addOption(irsimOption, "irsim", false, irsimOptionDescription);
    options.addOption(disableOptOption, false, disableOptOptionDescription);
    options.addOption(parsePrintOption, "print", false, parsePrintOptionDescription);
    options.addOption(targetOption, true, targetOptionDescription);
    options.addOption(optCopyPropOption, false, optCopyPropOptionDescription);
    options.addOption(optDeadCodeElimOption, false, optDeadCodeElimOptionDescription);
    options.addOption(optConstFoldOption, false, optConstFoldOptionDescription);
    options.addOption(optCSEOption, false, optCSEOptionDescription);
    options.addOption(optRegisterOption, false, optCSEOptionDescription);

    Option assemblyPathOptObject =
        Option.builder(assemblyPathOption)
            .hasArg(true)
            .argName("path")
            .desc(assemblyPathOptionDescription)
            .build();
    options.addOption(assemblyPathOptObject);

    Option dirOptObject =
        Option.builder(dirOption).hasArg(true).argName("path").desc(dirOptionDescription).build();
    options.addOption(dirOptObject);

    Option srcPathOptObject =
        Option.builder(srcPathOption)
            .hasArg(true)
            .argName("path")
            .desc(srcPathOptionDescription)
            .build();
    options.addOption(srcPathOptObject);

    Option libPathOptObject =
        Option.builder(libPathOption)
            .hasArg(true)
            .argName("path")
            .desc(libPathOptionDescription)
            .build();
    options.addOption(libPathOptObject);

    Option optIRGenObject =
        Option.builder(optIRGenOption)
            .longOpt("optir")
            .hasArg(true)
            .argName("phase")
            .desc(optIRGenOptionDescription)
            .build();
    options.addOption(optIRGenObject);

    Option dotGenOptObject =
        Option.builder(dotGenOption)
            .longOpt("optcfg")
            .hasArg(true)
            .argName("phase")
            .desc(dotGenOptionDescription)
            .build();
    options.addOption(dotGenOptObject);

    Option reportOptimsOptObject =
        Option.builder().longOpt(reportOptimsOption).desc(reportOptimsOptionDescription).build();
    options.addOption(reportOptimsOptObject);
  }

  /**
   * @param optString the string representing a particular option. See the Cli file for option
   *     strings
   * @return True if the option was specified, false otherwise
   */
  public Boolean hasOption(String optString) {
    return cmd != null ? cmd.hasOption(optString) : false;
  }

  /** @return True if xic was called incorrectly. False otherwise. */
  public Boolean poorlyFormed() {
    return cmd != null ? getFiles().length == 0 || cmd.getOptions().length == 0 : true;
  }

  /** @return an array of files that the compiler was called on. */
  public String[] getFiles() {
    return cmd.getArgs();
  }

  /** Print the help message in the proper format. */
  public void printHelp() {
    hf.printHelp(xicUsageString, options);
  }

  /**
   * Gives the Cli the arguments specified by the user and primes the Cli instance to the options
   * specified.
   *
   * @param args arguments specified by the user
   * @return Cli with functionality that is appropriate for the user-specified options
   * @throws ParseException
   */
  public Cli parseArgs(String[] args) throws ParseException {
    cmd = new DefaultParser().parse(options, args);
    return this;
  }

  /** @return the appropriate output directory */
  public String getOutputDir() {
    return formatPath(cmd.getOptionValue(dirOption, "./"));
  }

  /** @return the appropriate assembly directory */
  public String getAssemblyPath() {
    return formatPath(cmd.getOptionValue(assemblyPathOption, "./"));
  }

  /** @return the appropriate source directory */
  public String getSrcPath() {
    return formatPath(cmd.getOptionValue(srcPathOption, "./"));
  }

  /** @return the appropriate library directory */
  public String getLibPath() {
    return formatPath(cmd.getOptionValue(libPathOption, "./"));
  }

  /** @return the phase of optimization to generate a cfg for */
  public String getCFGPhase() {
    return cmd.getOptionValue(dotGenOption);
  }

  /** @return the phase of optimization to generate the IR for */
  public String getIRPhase() {
    return cmd.getOptionValue(optIRGenOption);
  }

  /**
   * @param path
   * @return {@code path} formatted to meet unix standards
   */
  private String formatPath(String path) {
    path = path.length() == 0 ? "./" : path;
    if (path.charAt(path.length() - 1) != '/') {
      path = path + "/";
    }
    return path;
  }
}

package apd67.assembly;

import apd67.assembly.register.RegisterEnum;
import apd67.ir.IRTemp;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Instruction {
  private String instr;
  private List<IRTemp> temps;
  private List<RegisterEnum> regs = new LinkedList<>();
  private List<String> regStr = new LinkedList<>();
  private boolean isLabel = false;
  private boolean isJump = false;
  private String call = "";
  private int registerSize = 64;
  private boolean notFirst = false;

  public Instruction(String instruction, List<IRTemp> temps) {
    instr = instruction + "\n";
    this.temps = temps;
  }

  public Instruction(String instruction, IRTemp... temps) {
    this(instruction, Arrays.asList(temps));
  }

  public Instruction(String instruction, int registerSize, List<IRTemp> temps) {
    this(instruction, temps);
    this.registerSize = registerSize;
  }

  public Instruction(String instruction, int registerSize, IRTemp... temps) {
    this(instruction, registerSize, Arrays.asList(temps));
  }

  public Instruction(String instruction, int registerSize, boolean notFirst, IRTemp... temps) {
    this(instruction, registerSize, Arrays.asList(temps));
    this.notFirst = notFirst;
  }

  public Instruction makeLabel() {
    this.isLabel = true;
    return this;
  }

  public Instruction makeJump() {
    this.isJump = true;
    return this;
  }

  public String getInstruction() {
    return instr;
  }

  public List<IRTemp> getTemps() {
    return temps;
  }

  // public void setRegisters(List<RegisterEnum> registers) {
  //   regs = registers;
  // }

  public void setRegisters(List<String> registers) {
    regStr = registers;
  }

  public boolean isLabel() {
    return isLabel;
  }

  public boolean isJump() {
    return isJump;
  }

  public Instruction setCall(String c) {
    call = c;
    return this;
  }

  public String call() {
    return call;
  }

  public boolean isCall() {
    return !(call.equals(""));
  }

  private String subregister(String r) {
    if (registerSize == 64) return r;
    else if (registerSize == 8) {
      switch (r) {
        case "rax":
          return "al";
        case "rcx":
          return "cl";
        case "rdx":
          return "dl";
        case "rbx":
          return "bl";
        case "rsi":
          return "sil";
        case "rdi":
          return "dil";
        case "rsp":
          return "spl";
        case "rbp":
          return "bpl";
        case "r8":
          return "r8b";
        case "r9":
          return "r9b";
        case "r10":
          return "r10b";
        case "r11":
          return "r11b";
        case "r12":
          return "r12b";
        case "r13":
          return "r13b";
        case "r14":
          return "r14b";
        case "r15":
          return "r15b";
        default:
          return r;
      }
    } else throw new Error("invalid register size");
  }

  private String subregister(RegisterEnum r) {
    if (registerSize == 64) return r.toString();
    else if (registerSize == 8) {
      switch (r) {
        case RAX:
          return "al";
        case RCX:
          return "cl";
        case RDX:
          return "dl";
        case RBX:
          return "bl";
        case RSI:
          return "sil";
        case RDI:
          return "dil";
        case RSP:
          return "spl";
        case RBP:
          return "bpl";
        case R8:
          return "r8b";
        case R9:
          return "r9b";
        case R10:
          return "r10b";
        case R11:
          return "r11b";
        case R12:
          return "r12b";
        case R13:
          return "r13b";
        case R14:
          return "r14b";
        case R15:
          return "r15b";
        default:
          throw new Error("register enum missing case");
      }
    } else throw new Error("invalid register size");
  }

  public String toString() {
    String[] split = instr.split("`d\\d|`s\\d");
    StringBuilder sb = new StringBuilder();
    if (!isLabel) sb.append("\t");

    // would not run on len one because math
    for (int i = 0; i < split.length - 1; i++) {
      sb.append(split[i]);

      // print registers if allocated, otherwise print the temps
      if (notFirst && i == 0) {
        if (regs.size() != 0) sb.append(regs.get(i));
        else if (regStr.size() != 0) sb.append(regStr.get(i));
        else sb.append(temps.get(i).label());
      } else {
        if (regs.size() != 0) sb.append(subregister(regs.get(i)));
        else if (regStr.size() != 0) sb.append(subregister(regStr.get(i)));
        else sb.append(temps.get(i).label());
      }
    }

    sb.append(split[split.length - 1]);
    // sb.append("\t#" + instr); // comment

    return sb.toString();
  }
}

package apd67.assembly.register;

import apd67.assembly.Instruction;
import apd67.assembly.Tiling;
import apd67.ir.IRTemp;
import java.util.*;
import java.util.Queue;

public class TrivialRegisterAllocator2 extends RegisterAllocator {
  private ValuedRegister reg;
  private int level = 5;
  private Map<String, Long> mappedTemps = new HashMap<String, Long>();
  private Queue<ValuedRegister> registers = initialRegisters();
  private int ret = level;

  /** The representation of {@link RegisterEnum} along with the values they currently hold */
  private class ValuedRegister {
    public RegisterEnum regEnum;
    public String name; // location on stack relative to frame pointer
    public boolean cleared;

    public ValuedRegister(RegisterEnum e) {
      regEnum = e;
      name = "";
      cleared = true;
    }

    public String toString() {
      return regEnum.toString();
    }
  }

  public Queue<ValuedRegister> initialRegisters() {
    ValuedRegister r12 = new ValuedRegister(RegisterEnum.R12);
    ValuedRegister r13 = new ValuedRegister(RegisterEnum.R13);
    ValuedRegister r14 = new ValuedRegister(RegisterEnum.R14);
    return new LinkedList(Arrays.asList(r12, r13, r14));
  }

  /**
   * Checks that the temp with the name {@code tempName} is in a register and sets {@code reg} to
   * the corresponding {@code ValuedRegister} if true
   *
   * @param tempName
   * @return true if the temp is currently in a register.
   */
  public boolean inRegister(String tempName) {
    for (ValuedRegister r : registers) {
      if (!r.cleared && r.name.equals(tempName)) {
        reg = r;
        return true;
      }
    }
    return false;
  }

  private boolean isArg(String temp) {
    return temp.length() > 4 && temp.substring(0, 4).equals("_ARG");
  }

  private boolean isRet(String temp) {
    return temp.length() > 4 && temp.substring(0, 4).equals("_RET");
  }

  private int getNum(String temp) {
    return Integer.parseInt(temp.substring(4));
  }

  /**
   * Assign a {@code temp} to a register.
   *
   * @return an {@link RegisterResult} with the assigned register and the code to properly move
   *     values to and from the stack.
   */
  public RegisterResult getRegister(String temp) {
    // Still in a register
    if (isArg(temp)) return new RegisterResult(getArgRegister(getNum(temp)));
    if (isRet(temp)) return new RegisterResult(getRetRegister(getNum(temp)));
    if (inRegister(temp)) return new RegisterResult(reg.regEnum);

    // cycle the queue
    reg = registers.poll();
    registers.add(reg);
    // Not in a register but on the stack
    List<Instruction> result = moveIntoRegister(reg, temp);

    return new RegisterResult(reg.regEnum, result);
  }

  public RegisterResult getRegister(IRTemp temp) {
    return getRegister(temp.name());
  }

  public String getArgRegister(int i) {
    switch (i) {
      case 0:
        return "rdi";
      case 1:
        return "rsi";
      case 2:
        return "rdx";
      case 3:
        return "rcx";
      case 4:
        return "r8";
      case 5:
        return "r9";
      default:
        return "[rbp + " + (8 * (i - 4)) + "]";
    }
  }

  public static String getCallRegister(int i) {
    switch (i) {
      case 0:
        return "rdi";
      case 1:
        return "rsi";
      case 2:
        return "rdx";
      case 3:
        return "rcx";
      case 4:
        return "r8";
      case 5:
        return "r9";
      default:
        return "[rsp + " + (8 * (i - 6)) + "]";
    }
  }

  private String getRetRegister(int i) {
    switch (i) {
      case 0:
        return "rax";
      case 1:
        return "rdx";
      default:
        return "[rbp - " + (8 * (ret + i - 2)) + "]";
    }
  }

  public long getLevel() {
    return level;
  }

  public List<Instruction> saveRegisters() {
    List<Instruction> instrs = new ArrayList<Instruction>();
    instrs.add(new Instruction("mov [rbp - " + (2 * 8) + "], r8"));
    instrs.add(new Instruction("mov [rbp - " + (3 * 8) + "], r9"));
    instrs.add(new Instruction("mov [rbp - " + (4 * 8) + "], r10"));
    return instrs;
  }

  public List<Instruction> restoreRegisters() {
    List<Instruction> instrs = new ArrayList<Instruction>();
    instrs.add(new Instruction("mov r10, [rbp - " + (4 * 8) + "]"));
    instrs.add(new Instruction("mov r9, [rbp - " + (3 * 8) + "]"));
    instrs.add(new Instruction("mov r8, [rbp - " + (2 * 8) + "]"));
    return instrs;
  }

  /**
   * Make the appropriate moves to assign {@code reg} to {@code temp}
   *
   * @param reg
   * @param temp
   * @return
   */
  private List<Instruction> moveIntoRegister(ValuedRegister reg, String temp) {
    LinkedList<Instruction> insns = new LinkedList();
    // Move the current values onto the stack
    if (!reg.cleared) {
      long loc = 0;
      // get the location of the temp on the stack
      if (mappedTemps.containsKey(reg.name)) loc = mappedTemps.get(reg.name);
      else {
        loc = (level++) * 8;
        mappedTemps.put(reg.name, loc);
      }
      insns.add(regMoveInsn(reg, loc, false));
    }
    reg.cleared = false;

    // Potentially an If statement
    if (mappedTemps.containsKey(temp)) insns.add(regMoveInsn(reg, mappedTemps.get(temp), true));
    reg.name = temp;
    return insns;
  }

  private Instruction regMoveInsn(ValuedRegister reg, Long loc, boolean intoReg) {
    String temp = "[" + RegisterEnum.RBP + " - " + loc + "]";
    // Are we moving into the register or out?
    String insn = "mov " + temp + ", " + reg;
    if (intoReg) insn = "mov " + reg + ", " + temp;

    return new Instruction(insn);
  }

  public List<Instruction> handleCall(Instruction call) {
    LinkedList<Instruction> result = new LinkedList();
    int in = Tiling.getNumInput(call.call());
    int out = Tiling.getNumReturn(call.call());
    if (out > 2) {
      ret = level; // set base address for returns
      level += out - 2; // make space in stack for returned values
      result.add(new Instruction("# ret -> " + ret + ",  level  -> " + level));
      result.add(new Instruction("mov " + getCallRegister(in) + ", rbp"));
      result.add(new Instruction("sub " + getCallRegister(in) + ", " + ret * 8));
    }
    // System.out.println(call.call());
    result.add(call);
    return result;
  }

  public List<Instruction> clearRegisters() {
    LinkedList<Instruction> insns = new LinkedList();
    // Move the current values onto the stack
    for (ValuedRegister reg : registers) {
      if (!reg.cleared) {
        long loc = 0;
        // get the location of the temp on the stack
        if (mappedTemps.containsKey(reg.name)) loc = mappedTemps.get(reg.name);
        else {
          loc = (level++) * 8;
          mappedTemps.put(reg.name, loc);
        }
        insns.add(regMoveInsn(reg, loc, false));
      }
      reg.cleared = true;
    }
    return insns;
  }
}

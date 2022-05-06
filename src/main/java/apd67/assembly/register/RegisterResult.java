package apd67.assembly.register;

import apd67.assembly.Instruction;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RegisterResult {
  private List<Instruction> instrs = new LinkedList<>();
  private RegisterEnum reg;
  private String mem_loc;

  public RegisterResult(RegisterEnum register, Instruction... instructions) {
    instrs = Arrays.asList(instructions);
    reg = register;
  }

  public RegisterResult(RegisterEnum register, List<Instruction> instructions) {
    instrs = instructions;
    reg = register;
  }

  public RegisterResult(String mem) {
    mem_loc = mem;
  }

  public List<Instruction> getInstructions() {
    return instrs;
  }

  public RegisterEnum reg() {
    return reg;
  }

  public String toString() {
    return mem_loc == null ? reg.toString() : mem_loc;
  }
}

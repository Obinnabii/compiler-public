package apd67.assembly;

import apd67.assembly.register.RegisterResult;
import apd67.assembly.register.TrivialRegisterAllocator2;
import apd67.ir.*;
import apd67.ir.IRCompUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssemblyBuilder {
  // private List<Instruction> instructions;
  private Map<String, List<Instruction>> functions = new HashMap<String, List<Instruction>>();
  // private Map<String, IRData> dataMap = new HashMap<String, IRData>();

  public AssemblyBuilder(IRCompUnit cu) {
    for (IRFuncDecl f : cu.functions().values()) {
      Tiling tl = new Tiling();
      tl.tileFunc(f);
      functions.put(f.name(), tl.getInstructions());
    }
    // dataMap = cu.dataMap(); // optional ?
  }

  public void allocateRegisters() {
    // iterate through each function body

    for (String name : functions.keySet()) {
      TrivialRegisterAllocator2 ra = new TrivialRegisterAllocator2();
      List<Instruction> instrs = functions.get(name);
      List<Instruction> new_instrs = new ArrayList<Instruction>();

      for (Instruction instr : instrs) {
        if (instr.toString().contains("# save registers\n")) {
          // System.out.println("saving registers");
          new_instrs.addAll(ra.saveRegisters());
        } else if (instr.toString().contains("# restore registers\n")) {
          // System.out.println("restoring registers");
          new_instrs.addAll(ra.restoreRegisters());
        } else if (instr.isLabel() || instr.isJump()) {
          new_instrs.addAll(ra.clearRegisters());
          new_instrs.add(instr);
        } else if (instr.isCall()) {
          // System.out.println(instr);
          new_instrs.addAll(ra.handleCall(instr));
        } else {
          List<String> registers = new ArrayList<String>();
          for (IRTemp temp : instr.getTemps()) {
            RegisterResult rr = ra.getRegister(temp);
            new_instrs.addAll(rr.getInstructions());
            registers.add(rr.toString());
          }
          instr.setRegisters(registers);
          new_instrs.add(instr);
        }
      }

      // enter must be added retroactively b/c we don't know ra.level until
      // after allocating whole body
      new_instrs.add(1, new Instruction("enter " + (ra.getLevel() * 8) + ", 0"));

      functions.put(name, new_instrs);
    }
  }

  public String toString() {
    // add header
    StringBuilder sb = new StringBuilder();

    sb.append("\t.text\n");
    sb.append("\t.intel_syntax noprefix\n");
    sb.append("\t.globl	_Imain_paai\n");
    sb.append("\t.type  _Imain_paai, @function\n");

    // main needs to go first
    for (Instruction insn : functions.get("_Imain_paai")) {
      sb.append(insn);
    }

    for (String name : functions.keySet()) {
      if (!name.equals("_Imain_paai")) {
        List<Instruction> insns = functions.get(name);
        for (Instruction insn : insns) {
          sb.append(insn);
        }
      }
    }
    return sb.toString();
  }
}

package apd67.assembly;

import apd67.assembly.register.TrivialRegisterAllocator2;
import apd67.ir.*;
import apd67.visitor.ir.*;
import java.util.LinkedList;
import java.util.List;

public class Tiling {
  private int debug_level = 0;
  private static int tempCounter = 0;
  private List<Instruction> instructions = new LinkedList<Instruction>();
  private IRTemp ret_loc = new IRTemp("__LOC");

  public Tiling() {
    // add later?
  }

  public void tileFunc(IRFuncDecl f) {
    if (debug_level > 1) {
      emit(new Instruction("# tiling " + f.name()));
    }
    emit(new Instruction(f.name() + ":").makeLabel());
    // emit(new Instruction("# prologue")); // TODO: add substitution logic in allocator
    emit(new Instruction("push r12"));
    emit(new Instruction("push r13"));
    emit(new Instruction("push r14"));
    if (getNumReturn(f.name()) > 2) {
      int i = getNumInput(f.name());
      emit(new Instruction("mov `d0, `s0", ret_loc, IRTranslator.makeArgTempStatic(i++)));
    }
    tileStmt(f.body());
  }

  public void tileStmt(IRStmt s) {
    if (debug_level > 0) emit(new Instruction("# " + s));

    switch (s.label()) {
      case "CALL_STMT":
        tileCallStmt((IRCallStmt) s);
        break;
      case "CJUMP": // Obi
        tileCJump((IRCJump) s);
        break;
      case "JUMP": // Obi
        tileJump((IRJump) s);
        break;
      case "MOVE": // Obi
        tileMove((IRMove) s);
        break;
      case "RETURN": // Obi
        tileReturn((IRReturn) s);
        break;
      case "SEQ":
        tileSeq((IRSeq) s);
        break;
      default: // Labels
        tileLabel((IRLabel) s);
        break;
    }
  }

  // private String getArgRegister(int i) {
  //   switch (i) {
  //     case 0:
  //       return "rdi";
  //     case 1:
  //       return "rsi";
  //     case 2:
  //       return "rdx";
  //     case 3:
  //       return "rcx";
  //     case 4:
  //       return "r8";
  //     case 5:
  //       return "r9";
  //     default:
  //       return "[rsp + "+ (8 * (i-6)) +"]";
  //   }
  // }

  private void tileCallStmt(IRCallStmt s) {

    // move all possible arguments into registers
    if (debug_level > 1) {
      emit(new Instruction("# moving arguments into arg registers / mem address"));
    }
    for (int i = 0; i < s.args().size(); i++) {
      // System.out.println(i);
      IRExpr e = s.args().get(i);
      emit(
          new Instruction(
              "mov " + TrivialRegisterAllocator2.getCallRegister(i) + ", `s0", tileExpr(e)));
    }
    // System.out.println(i);
    // spill remaining args
    // ....
    // for (int j = s.args().size() - 1; j >= 6; j--) {
    //   IRExpr e = s.args().get(j);
    //   // TODO: FIX THIS
    //   // emit(new Instruction("mov [rbp - `offset], `s0", tileExpr(e)));
    // }

    // force stack to be 16 byte aligned (TODO: add this logic to reg alloc.)
    emit(
        new Instruction("call " + ((IRName) s.target()).name())
            .setCall(((IRName) s.target()).name()));
  }

  private void tileLabel(IRLabel l) {
    if (debug_level > 1) {
      emit(new Instruction("# making label " + l));
    }
    emit(new Instruction(l.name() + ":").makeLabel());
  }

  private void tileSeq(IRSeq seq) {
    // if (debug_level > 1) {
    //   emit(new Instruction("# tiling a sequence"));
    // }
    for (IRStmt s : seq.stmts()) tileStmt(s);
  }

  private void tileJump(IRJump j) {
    if (debug_level > 0) {
      emit(new Instruction("# tiling jump " + j));
    }
    if (j.target() instanceof IRName) {
      String targetString = ((IRName) j.target()).name();
      emit(new Instruction("jmp " + targetString).makeJump());
    } else {
      throw new Error("Jump target is not an IRName");
    }
  }

  private void tileCJump(IRCJump cj) {
    if (debug_level > 0) {
      emit(new Instruction("# tiling CJump " + cj));
    }
    if (cj.hasFalseLabel()) throw new Error("Cjump has a false label during tiling");
    if (cj.cond() instanceof IRBinOp) tileCJumpBinop((IRBinOp) cj.cond(), cj.trueLabel());
    else compJump(cj.cond(), new IRConst(1), "jge " + cj.trueLabel());
  }

  private void tileCJumpBinop(IRBinOp b, String label) {
    if (debug_level > 1) {
      emit(new Instruction("# tiling CJumpBinop " + b + ", " + label));
    }
    switch (b.opType()) {
      case EQ:
        compJump(b.left(), b.right(), "je " + label);
        break;
      case NEQ:
        compJump(b.left(), b.right(), "jne " + label);
        break;
      case LT:
        compJump(b.left(), b.right(), "jl " + label);
        break;
      case ULT:
        compJump(b.left(), b.right(), "jb " + label);
        break;
      case LEQ:
        compJump(b.left(), b.right(), "jle " + label);
        break;
      case GT:
        compJump(b.left(), b.right(), "jg " + label);
        break;
      case GEQ:
        compJump(b.left(), b.right(), "jge " + label);
        break;
      default:
        compJump(tileExpr(b), new IRConst(1), "jge " + label + " # switch default");
        break;
    }
  }

  public static int getNumReturn(String funcname) {
    String[] split = funcname.split("[_]+");
    String encoding = split[split.length - 1];
    char spec = encoding.charAt(0);
    if (spec == 'p') return 0;
    else if (spec == 't') {
      String digits = encoding.replaceAll("[^0-9]", "");
      return Integer.parseInt(digits);
    } else return 1;
  }

  public static int getNumInput(String funcname) {
    String[] split = funcname.split("[_]+");
    String encoding = split[split.length - 1];
    char spec = encoding.charAt(0);
    if (spec == 'p' || spec != 't') {
      String no_arr = encoding.replaceAll("a", "");
      return no_arr.length() - 1;
    } else {
      String no_arr = encoding.replaceAll("a", "");
      String digits = no_arr.replaceAll("[^0-9]", "");
      int num = Integer.parseInt(digits);
      return no_arr.length() - digits.length() - num - 1;
    }
  }

  private void compJump(IRExpr e1, IRExpr e2, String jmp) {
    IRTemp t1 = null, t2 = null;
    // if (!e1.isConstant()) t1 = tileExpr(e1);
    // if (!e2.isConstant()) t2 = tileExpr(e2);
    // if (e1.isConstant() && e2.isConstant()) {
    //   emit(new Instruction("cmp " + e1.constant() + ", " + e2.constant()));
    // } else if (e1.isConstant()) {
    //   emit(new Instruction("cmp " + e1.constant() + ", `s0", t2));
    // } else if (e2.isConstant()) {
    //   emit(new Instruction("cmp `s0, " + e2.constant(), t1));
    // } else {
    //   emit(new Instruction("cmp `s0, `s1", t1, t2));
    // }
    t1 = tileExpr(e1);
    t2 = tileExpr(e2);
    emit(new Instruction("cmp `s0, `s1", t1, t2));
    emit(new Instruction(jmp).makeJump());
  }

  private void tileReturn(IRReturn r) {
    int i = 0;
    for (IRExpr e : r.rets()) {
      if (i <= 1) tileMove(new IRMove(IRTranslator.makeRetTempStatic(i), tileExpr(e)));
      else emit(new Instruction("mov [`d0 + " + (i - 2) * 8 + "], `s0", ret_loc, tileExpr(e)));
      // tileMove(
      // new IRMove(
      //   new IRMem(new IRBinOp(IRBinOp.OpType.SUB, ret_loc, new IRConst(8 * (i - 2)))), e));

      i++;
    }
    emit(new Instruction("pop r14"));
    emit(new Instruction("pop r13"));
    emit(new Instruction("pop r12"));
    emit(new Instruction("leave")); // epilogue
    emit(new Instruction("ret"));
  }

  private void tileMove(IRMove m) {
    IRExpr src = m.source(), target = m.target();
    if (debug_level == -101) {
      emit(new Instruction("# move " + m));
    }
    if (target instanceof IRMem) {
      IRMem mem = (IRMem) target;
      IRTemp srcTiled = tileExpr(m.source());
      IRTemp dstTiled = tileExpr(mem.expr());
      emit(new Instruction("mov [`d0], `s0", dstTiled, srcTiled));
    } else if (src instanceof IRMem) {
      IRMem mem = (IRMem) src;
      IRTemp srcTiled = tileExpr(mem.expr());
      IRTemp dstTiled = tileExpr(m.target());
      emit(new Instruction("mov `d0, [`s0]", dstTiled, srcTiled));
    } else {
      IRTemp srcTiled = tileExpr(src);
      IRTemp dst = tileExpr(m.target());
      emit(new Instruction("mov  `d0, `s0", dst, srcTiled));
    }
  }

  public IRTemp tileExpr(IRExpr e) {
    if (debug_level > 0) emit(new Instruction("# " + e));
    if (e instanceof IRBinOp) {
      return tileBinOp((IRBinOp) e);
    } else if (e instanceof IRConst) { // Jack
      return tileConst((IRConst) e);
    } else if (e instanceof IRMem) { // Obi
      return tileMem((IRMem) e);
    } else if (e instanceof IRName) { // Jack - we should not handle this here
      throw new Error("IRName should only occur in jump or call");
    } else if (e instanceof IRTemp) { // Jack - ???????? idk what to do here
      return (IRTemp) e;
    } else {
      throw new Error("Unreachable tile expr case on " + e);
    }
  }

  private IRTemp tileBinOp(IRBinOp b) {
    switch (b.opType()) {
      case ADD:
        return tileSum(b);
      case SUB:
        return tileSub(b);
      case MUL:
        return tileMul(b, "rax");
      case HMUL:
        return tileMul(b, "rdx");
      case DIV:
        return tileDivMod(b, "rax");
      case MOD:
        return tileDivMod(b, "rdx");
      case AND:
        return tileBasicOp(b, "and");
      case OR:
        return tileBasicOp(b, "or");
      case XOR:
        return tileBasicOp(b, "xor");
      case LSHIFT:
        return tileBasicOp(b, "shl");
      case RSHIFT:
        return tileBasicOp(b, "shr");
      case ARSHIFT:
        return tileBasicOp(b, "sar");
      case EQ:
        return tileComparison(b, "sete");
      case NEQ:
        return tileComparison(b, "setne");
      case LT:
        return tileComparison(b, "setl");
      case ULT:
        return tileComparison(b, "setb");
      case LEQ:
        return tileComparison(b, "setle");
      case GT:
        return tileComparison(b, "setg");
      case GEQ:
        return tileComparison(b, "setge");
      default:
        throw new Error("Unimplemented");
    }
  }

  private IRTemp tileSum(IRBinOp b) {
    IRTemp r = new IRTemp(getTempName());
    // IRTemp rL = tileExpr(b.left());
    // IRTemp rR = tileExpr(b.right());
    // String op = "";
    // switch (b.opType()) {
    //   case ADD:
    //     op = "+";
    //     break;
    //     // case SUB:
    //     //   op = "-";
    //     //   break;
    //   default:
    //     op = "";
    //     break;
    // }
    // emit(new Instruction("lea `d0, [`s0 " + op + " `s1]", r, rL, rR));
    IRTemp rL = tileExpr(b.left());
    emit(new Instruction("mov `d0, `s0", r, rL));

    IRTemp rR = tileExpr(b.right());
    emit(new Instruction("add `d0, `s0", r, rR));
    return r;
  }

  private IRTemp tileSub(IRBinOp b) {
    IRTemp r = new IRTemp(getTempName());
    IRTemp rL = tileExpr(b.left());
    emit(new Instruction("mov `d0, `s0", r, rL));

    IRTemp rR = tileExpr(b.right());
    emit(new Instruction("sub `d0, `s0", r, rR));
    return r;
  }

  private IRTemp tileMul(IRBinOp b, String res) {
    IRTemp r = new IRTemp(getTempName());
    IRTemp rL = tileExpr(b.left());
    emit(new Instruction("mov rax, `s0", rL));

    IRTemp rR = tileExpr(b.right());
    emit(new Instruction("imul `s0", rR));
    emit(new Instruction("mov `d0, " + res, r));

    return r;
  }

  private IRTemp tileDivMod(IRBinOp b, String res) {
    IRTemp r = new IRTemp(getTempName());
    IRTemp rL = tileExpr(b.left());
    emit(new Instruction("mov rax, `s0", rL));

    IRTemp rR = tileExpr(b.right());
    emit(new Instruction("cqo"));
    emit(new Instruction("idiv `s0", rR));
    emit(new Instruction("mov `d0, " + res, r));

    return r;
  }

  private IRTemp tileBasicOp(IRBinOp b, String op) {
    IRTemp r = new IRTemp(getTempName());
    IRTemp rL = tileExpr(b.left());
    emit(new Instruction("mov `d0, `s0", r, rL));

    IRTemp rR = tileExpr(b.right());
    emit(new Instruction(op + " `d0, `s0", r, rR));

    return r;
  }

  private IRTemp tileComparison(IRBinOp b, String set) {
    IRTemp r = new IRTemp(getTempName());

    IRTemp rL = tileExpr(b.left());
    emit(new Instruction("mov `d0, `s0", r, rL));

    IRTemp rR = tileExpr(b.right());
    emit(new Instruction("cmp `s0, `s1", r, rR));

    // emit(new Instruction("xor `s0, `s1", r, r));
    emit(new Instruction(set + "  `d0", 8, r));
    emit(new Instruction("and `d0, 1", 8, r));
    emit(new Instruction("movzx `d0, `s0", 8, true, r, r));
    // emit(new Instruction(set + "  al", r));

    return r;
  }

  private IRTemp tileConst(IRConst c) {
    IRTemp r = new IRTemp(getTempName());
    if (c.value() < Integer.MAX_VALUE) {
      emit(new Instruction("mov  `d0, " + c.value(), r));
    } else {
      emit(new Instruction("movabs  `d0, " + c.value(), r));
    }

    return r;
  }

  private IRTemp tileMem(IRMem m) {
    if (debug_level == -101) {
      emit(new Instruction("# tiling mem"));
    }
    IRTemp r = new IRTemp(getTempName());
    IRTemp rL = tileExpr(m.expr());

    emit(new Instruction("mov `d0, [`s0]", r, rL));

    return r;
  }

  private String getTempName() {
    return "_tmp" + (tempCounter++);
  }

  /** Add insn to list of instructions that are result of tiling */
  public void emit(Instruction insn) {
    instructions.add(insn);
  }

  public List<Instruction> getInstructions() {
    return instructions;
  }
}

package apd67.assembly.register;

public enum RegisterEnum {
  RAX,
  RBX,
  RCX,
  RDX,
  RBP, // frame pointer
  RSP, // stack pointer
  RSI,
  RDI,
  R8,
  R9,
  R10,
  R11,
  R12,
  R13,
  R14,
  R15;

  @Override
  public String toString() {
    switch (this) {
      case RAX:
        return "rax";
      case RBX:
        return "rbx";
      case RCX:
        return "rcx";
      case RDX:
        return "rdx";
      case RBP:
        return "rbp";
      case RSP:
        return "rsp";
      case RSI:
        return "rsi";
      case RDI:
        return "rdi";
      case R8:
        return "r8";
      case R9:
        return "r9";
      case R10:
        return "r10";
      case R11:
        return "r11";
      case R12:
        return "r12";
      case R13:
        return "r13";
      case R14:
        return "r14";
      case R15:
        return "r15";
    }
    throw new Error("Unknown register");
  }
};

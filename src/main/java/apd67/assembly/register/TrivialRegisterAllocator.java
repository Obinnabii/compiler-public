// package apd67.assembly.register;

// import apd67.assembly.Instruction;
// import java.util.*;
// import java.util.LinkedList;
// import java.util.ArrayList;

// public class TrivialRegisterAllocator extends RegisterAllocator {

//   private LinkedList<Instruction> original;
//   private String pm_name;
//   private String concrete_name;
//   private RegisterResult r8;
//   private RegisterResult r9;
//   private RegisterResult r10;

//   public TrivialRegisterAllocator(LinkedList<Instruction> alloc) {
//     this.original = alloc;
//     this.pm_name = "";
//     this.concrete_name = "";
//     r8 = new RegisterResult(Register.concrete_reg.r8);
//     r9 = new RegisterResult(Register.concrete_reg.r9);
//     r10 = new RegisterResult(Register.concrete_reg.r10);
//   }

//   public RegisterResult allocateRegister(String tempName) {
//     for (Instruction x : original) {
//       for (RegisterResult y : x.getRegister()) {
//         if (!pm_name.equals(String(y.reg()))) {
//           pm_name = String(reg);
//         }
//       }
//     }
//     LinkedList<Instruction> result = new LinkedList<Instruction>();
//     for (Instruction z : original) {
//       result.addAll(start(z));
//       result.addAll(concrete(z));
//       concrete_name = "";
//     }
//     return result;
//   }

//   public RegisterResult getRegister(String tempName) {
//     LinkedList<RegisterResult> result = new LinkedList<RegisterResult>();
//     List<IRTemp> update = new ArrayList<IRTemp>();
//     LinkedList<Instruction> temp = start(new Instruction("mov", update));
//     switch (temp.size()) {
//       case 1:
//       case 2:
//         if (!temp.get(1).isLabel()) {
//           result.addFirst(r10);
//         }
//       case 3:
//         if (!temp.get(2).isLabel()) {
//           result.addFirst(r9);
//         }
//       default:
//         break;
//     }
//     return result;
//   }

//   private LinkedList<Instruction> start(Instruction temp) {
//     LinkedList<Instruction> result = new LinkedList<Instruction>();
//     List<IRTemp> update = new ArrayList<IRTemp>();
//     LinkedList<RegisterResult> current = temp.getTemps();
//     switch (current.size()) {
//       case 3:
//         concrete_name = current.get(2).name();
//         result.addFirst(new Instruction("mov", update));
//       case 2:
//         concrete_name = current.get(1).name();
//         result.addFirst(new Instruction("mov", update));
//       case 1:
//         concrete_name = current.get(0).name();
//         result.addFirst(new Instruction("mov", update));
//       default:
//         return result;
//     }
//   }

//   private LinkedList<Instruction> concrete(Instruction temp) {
//     LinkedList<Instruction> result = new LinkedList<Instruction>();
//     List<IRTemp> update = new ArrayList<IRTemp>();
//     result.add(new Instruction("lea", update));
//     result.add(new Instruction("mov", update));
//     return result;
//   }
//
//   private List<AssemblyBuilder> prologue(AssemblyBuilder tempName) {
//     LinkedList<AssemblyBuilder> result = new LinkedList<AssemblyBuilder>();
//     LinkedList<ValuedRegister> current = tempName.getTemps();
//       switch(current.size()) {
//         case 3:
//           mappedTemps.put(current.get(2).name(), level*8);
//           result.addFirst(new AbstractAssembly("MOV"));
//         case 2:
//           mappedTemps.put(used.get(1).name(), level*9);
//           result.addFirst(new AbstractAssembly("MOV"));
//         case 1:
//           mappedTemps.put(used.get(0).name(), level*10);
//           result.addFirst(new AbstractAssembly("MOV"));
//         default:
//           return result;
//       }
//     }
// }

package apd67.assembly.register;

import apd67.ir.IRTemp;

public abstract class RegisterAllocator {

  /**
   * tempName will be in the register allocateRegister(tempName).getRegister() after executing
   * allocateRegister(tempName).getInstructions() (if any)
   */
  // public abstract RegisterResult allocateRegister(String tempName);

  /**
   * gives the RegisterResult associated with tempName e.g. first IRTemp("x") is allocated to a
   * register calling getRegister("x") returns the register containing the value associated with x
   */
  public abstract RegisterResult getRegister(String tempName);

  public abstract RegisterResult getRegister(IRTemp temp);
}

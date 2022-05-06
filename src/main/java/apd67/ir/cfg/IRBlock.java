package apd67.ir.cfg;

import apd67.ir.*;
import apd67.ir.IRCJump;
import apd67.ir.IRJump;
import apd67.ir.IRLabel;
import apd67.ir.IRName;
import apd67.ir.IRNode;
import apd67.ir.IRStmt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class IRBlock {
  public static String FIRST_NODE = "first_ir_node";
  private IRLabel label;
  private LinkedList<IRNode> body = new LinkedList<>();
  private ArrayList<String> out = new ArrayList<>();
  private ArrayList<String> in = new ArrayList<>();
  public boolean mark;
  public boolean isHead = false;
  public boolean hasSatJump = false;
  private static int dotUniqGen = 0;
  private String dotLabel;
  private String dotID;
  /**
   * A statement that does not fall through to the next statement, but rather transfers control
   * elsewhere. These statements includes IRReturn, IRJump, and IRCJump nodes
   */
  private IRNode exit;

  private IRJump satJump;

  public IRBlock() {
    super();
  }

  /**
   * A statement that does not fall through to the next statement, but rather transfers control
   * elsewhere. These statements includes IRReturn, IRJump, and IRCJump nodes
   */
  public IRBlock(LinkedList<IRNode> body) {
    this.body = body;
  }

  public IRBlock(boolean isHead) {
    this.isHead = isHead;
  }

  public void addLabel(IRLabel lbl) {
    this.label = lbl;
  }

  public void addNode(IRNode n) {
    body.addLast(n);
  }

  public void addExit(IRNode e) {
    exit = (IRStmt) e;
    if (e instanceof IRJump) {
      IRName n = (IRName) ((IRJump) e).target();
      out.add(n.name());
    }
    if (e instanceof IRCJump) {
      out.add(((IRCJump) e).trueLabel());
      String fl = ((IRCJump) e).falseLabel();
      if (fl != null) out.add(fl);
    }
  }

  public void addSatisfactoryJump(IRJump j) {
    hasSatJump = true;
    satJump = j;
  }

  public void removeExit() {
    exit = null;
  }

  public void addOut(String s) {
    out.add(s);
  }

  public void addIn(String s) {
    in.add(s);
  }

  public IRStmt exit() {
    return (IRStmt) exit;
  }

  public String blockName() {
    return isHead() ? FIRST_NODE : label().name();
  }
  /** @return the labels of all blocks that this block links into */
  public ArrayList<String> out() {
    return out;
  }

  /** @return the labels of all blocks that link into this block */
  public ArrayList<String> in() {
    return in;
  }

  public boolean hasExit() {
    return exit != null;
  }

  public boolean hasLabel() {
    return label != null;
  }

  public IRLabel label() {
    if (isHead && !hasLabel()) return new IRLabel(FIRST_NODE);
    return label;
  }

  public boolean isHead() {
    return isHead;
  }

  public int size() {
    int l = hasLabel() ? 1 : 0;
    int e = hasExit() ? 1 : 0;
    return l + e + body.size();
  }

  public int cfgLength() {
    // int e = hasExit() ? 1 : 0;
    // return e + body.size();
    return size();
  }

  private static boolean jumpOrReturn(IRNode n) {
    return n instanceof IRJump || n instanceof IRCJump || n instanceof IRReturn;
  }

  private static int pos = 0;

  // Should this be in the nodeFactory?
  public static CFG makeCFG(List<IRStmt> nodeList) {
    pos = 0;
    HashMap<String, Integer> map = new HashMap<String, Integer>();

    ArrayList<IRBlock> table = new ArrayList<>();
    ListIterator<IRStmt> it = nodeList.listIterator();

    IRBlock block = new IRBlock(true);
    while (it.hasNext()) {
      IRStmt n = it.next();

      // non-ordinary statement?
      if (jumpOrReturn(n)) { // exit
        block.addExit(n);
        table.add(block);
        addToMap(block, map);
        block = new IRBlock();
      } else if (n instanceof IRLabel) { // Label
        // not a new Block
        if (block.size() != 0) {
          block.addExit(new IRJump(new IRName(((IRLabel) n).name())));
          table.add(block);
          addToMap(block, map);
          block = new IRBlock();
        }
        block.addLabel((IRLabel) n);
      } else {
        if (block.hasLabel() || pos == 0) block.addNode(n);
      }
    }

    if (block.size() != 0) {
      table.add(block);
      addToMap(block, map);
    }

    return new CFG(map, table);
  }

  private static void addToMap(IRBlock block, HashMap<String, Integer> map) {
    if (block.isHead() || block.hasLabel()) {
      String name = block.isHead() ? FIRST_NODE : block.label().name();
      map.put(name, pos++);
    }
  }

  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append("BLOCK{");
    if (hasLabel()) s.append(label.toString());
    for (IRNode n : body) {
      s.append(n);
    }
    if (hasExit()) s.append(exit.toString());
    if (hasSatJump) s.append(satJump.toString());
    s.append("}");
    return s.toString();
  }

  public LinkedList<IRStmt> toList() {
    LinkedList<IRStmt> result = new LinkedList<>();
    if (hasLabel()) result.addLast(label);
    for (IRNode n : body) {
      result.addLast((IRStmt) n);
    }
    if (hasExit()) result.addLast((IRStmt) exit);
    if (hasSatJump) result.addLast(satJump);
    return result;
  }

  private String getDotLabel(boolean annotate) {
    if (dotLabel == null) {
      StringBuilder s = new StringBuilder();

      if (hasLabel()) {
        s.append(label.toString() + "\\n");
      }
      // int len = body.size();
      // if (len > 8) {
      //   int curr = 0;
      //   for (IRNode n : body) {
      //     if (curr < 2 || curr >= len - 2) {
      //       String nStr = n.toString();
      //       s.append(nStr.substring(0, nStr.length() - 1) + "\\n");
      //     } else if (curr == 2) s.append("...\\n");
      //     curr++;
      //   }
      // } else {
      //   for (IRNode n : body) {
      //     String nStr = n.toString();
      //     s.append(nStr.substring(0, nStr.length() - 1) + "\\n");
      //   }
      // }
      for (IRNode n : body) {
        String nStr = n.toDot(annotate);
        s.append(nStr.substring(0, nStr.length() - 1) + "\\n");
      }
      if (hasExit()) {
        s.append(exit.toDot(annotate) + "\\n");
      }
      if (hasSatJump) {
        s.append(satJump.toDot(annotate) + "\\n");
      }
      dotLabel = s.toString();
    }
    return dotLabel;
  }

  public String getDotID() {
    if (dotID == null) dotID = "n" + dotUniqGen++;
    return dotID;
  }

  public String toDot() {
    return toDot(false);
  }

  public String toDot(boolean annotate) {
    StringBuilder sb = new StringBuilder();
    // for (String s : out) {
    //   sb.append(dotID + " -> " + s.toString());
    // }
    sb.append(getDotID());
    sb.append(" [label=\"");
    sb.append(getDotLabel(annotate));
    sb.append("\"]");
    return sb.toString();
  }

  /**
   * @param i
   * @return the ith statement in the body of the block (exit included)
   */
  public IRStmt getStmt(int i) {
    int labelOffset = hasLabel() ? 1 : 0;

    if (i == 0 && hasLabel()) return label();
    if (hasExit() && (i == (cfgLength() - 1))) return (IRStmt) exit();
    return (IRStmt) body.get(i - labelOffset);
  }
}

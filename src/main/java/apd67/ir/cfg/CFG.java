package apd67.ir.cfg;

import apd67.ir.*;
import apd67.ir.IRNode;
import apd67.ir.cfg.dfa.LiveVar;
import apd67.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CFG {
  /** A mapping between labels of blocks to their index in table */
  private HashMap<String, Integer> map;
  /** Contains all IRBlocks in initial order */
  private ArrayList<IRBlock> table;

  private int totalMarked = 0;

  private static int uniqGraph = 0;

  public String name = "G" + uniqGraph++;

  public CFG(HashMap<String, Integer> map, ArrayList<IRBlock> table) {
    this.map = map;
    this.table = table;
    for (IRBlock block : this.table) fixIn(block);
  }

  public CFG(String name, HashMap<String, Integer> map, ArrayList<IRBlock> table) {
    this.map = map;
    this.table = table;
    for (IRBlock block : this.table) fixIn(block);
  }

  public void fixIn(IRBlock block) {
    // Look at the addToMap function
    String name = block.isHead() ? IRBlock.FIRST_NODE : block.label().name();
    for (String s : block.out()) {
      table.get(map.get(s)).addIn(name);
      ;
    }
  }

  public static IRSeq toSeq(List<IRBlock> blocks) {
    LinkedList<IRStmt> result = new LinkedList<>();
    for (IRBlock block : blocks) {
      // System.out.println(block);
      result.addAll(block.toList());
    }
    return new IRSeq(result);
  }

  public IRSeq toSeq() {
    return toSeq(table);
  }

  /**
   * returns the sequence represented by this CFG without any dead code (markedForDeath by LiveVar)
   * and a boolean that is only true if dead code was identified.
   *
   * @return a sequence of the form (live code, true if dead code was identified)
   */
  public Pair<IRSeq, Boolean> toLiveSeq() {
    LinkedList<IRStmt> liveStmtList = new LinkedList<>();
    boolean deadCodeIdentified = false;
    for (IRBlock block : table) {
      for (IRStmt stmt : block.toList()) {
        if (stmt.markedForDeath()) deadCodeIdentified = true;
        else liveStmtList.add(stmt);
      }
    }
    IRSeq liveIRSeq = new IRSeq(liveStmtList);
    return new Pair(liveIRSeq, deadCodeIdentified);
  }

  public ArrayList<IRBlock> greedyReordering() {
    resetMarks();
    ArrayList<IRBlock> ordering = new ArrayList<>();
    // while exists an unmarked block
    //    pick an unmarked block
    //    produce a maximal trace starting from that block
    while (totalMarked < table.size()) {
      int bIndex = getUnmarked();
      ordering.addAll(trace(bIndex));
    }
    return ordering;
  }

  private void resetMarks() {
    for (IRBlock b : table) {
      b.mark = false;
    }
    totalMarked = 0;
  }

  private LinkedList<IRBlock> trace(int blockIndex) {

    IRBlock b = table.get(blockIndex);
    if (b.mark) throw new Error("Unmarked block is marked");
    b.mark = true;
    totalMarked++;

    // backtrack[i] = idx of prev node to reach node i in trace
    int[] backtrack = new int[table.size()];

    ArrayList<String> children = b.out();
    // initialize backtrack
    for (String s : children) {
      int childIdx = map.get(s);
      backtrack[childIdx] = blockIndex;
    }

    int lastIdx = blockIndex; // index of last block in trace (deepest)

    int i = 0;
    while (i < children.size()) {
      String label = children.get(i);
      int blockIdx = map.get(label);
      IRBlock c = table.get(blockIdx);

      if (!c.mark) {
        c.mark = true;
        this.totalMarked++;
        children.addAll(c.out());
        lastIdx = blockIdx;
        for (String childStr : children) {
          // for all (unmarked) children, initialize their backtrack to c
          int childIdx = map.get(childStr);
          if (!table.get(childIdx).mark) {
            backtrack[childIdx] = blockIdx;
          }
        }
        // path.add(c);
      }
      i++;
    }

    // for (int j = 0; j < backtrack.length; j++) {
    //   System.out.println("backtrack[" + j + "] = " + backtrack[j]);
    // }

    // reconstruct path by backtracking
    LinkedList<IRBlock> path = new LinkedList<>();
    while (lastIdx != blockIndex) {
      table.get(lastIdx).mark = true;
      path.addFirst(table.get(lastIdx));
      lastIdx = backtrack[lastIdx];
    }
    path.addFirst(b);

    return path;
  }

  private int getUnmarked() {
    // change to use heuristics and be more efficient
    // for (int i = table.size(); i-- > 0; ) {
    for (int i = 0; i < table.size(); i++) {
      if (!table.get(i).mark) {
        return i;
      }
    }
    throw new Error("impossible, unmarked"); // should never happen
  }

  public static void fixJumps(ArrayList<IRBlock> table) {
    for (int i = 1; i < table.size(); i++) {
      IRBlock b = table.get(i - 1);
      IRBlock next = table.get(i);
      if (b.hasExit()) {
        if (b.exit() instanceof IRJump) { // Jump
          String exString = ((IRName) ((IRJump) b.exit()).target()).name();
          if (next.hasLabel() && next.label().name() == exString) b.removeExit();
        }

        if (b.exit() instanceof IRCJump) { // CJump
          IRCJump e = ((IRCJump) b.exit());
          String t = e.trueLabel();
          String f = e.falseLabel();
          if (next.hasLabel() && next.label().name() == f) {
            b.addExit((IRNode) new IRCJump(e.cond(), t));
          } else if (next.hasLabel() && next.label().name() == t) {
            IRExpr new_cond = new IRBinOp(IRBinOp.OpType.XOR, new IRConst(1), e.cond());
            b.addExit((IRNode) new IRCJump(new_cond, f));
          } else {
            b.addExit((IRNode) new IRCJump(e.cond(), t));
            b.addSatisfactoryJump(new IRJump(new IRName(f)));
          }
        }
      }

      if (i == table.size() - 1) {
        if (next.hasExit() && next.exit() instanceof IRCJump) {
          IRCJump e = ((IRCJump) next.exit());
          String t = e.trueLabel();
          String f = e.falseLabel();

          next.addExit((IRNode) new IRCJump(e.cond(), t));
          next.addSatisfactoryJump(new IRJump(new IRName(f)));
        }
      }
    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (IRBlock b : table) {
      sb.append(b);
      sb.append("\n");
    }
    return sb.toString();
  }

  public String toDot() {
    return toDot(false);
  }

  public String toDot(boolean annotate) {
    StringBuilder sb = new StringBuilder();
    sb.append("subgraph " + name + " {\n");
    sb.append("\tnode [shape=box]\n");
    sb.append("\tn" + name + " [label=\"\", shape=none,height=.0,width=.0]\n");
    sb.append("\tn" + name + " -> " + table.get(0).getDotID() + "\n");
    for (IRBlock b : table) {
      HashSet<String> drawn = new HashSet<String>();
      for (String s : b.out()) {
        if (!drawn.contains(s)) {
          drawn.add(s);
          IRBlock nxt = table.get(map.get(s));
          sb.append("\t" + b.getDotID() + " -> " + nxt.getDotID() + "\n");
        }
      }
      sb.append("\t" + b.toDot(annotate) + "\n");
    }
    sb.append("}");
    return sb.toString();
  }

  public boolean propagateCopies(ArrayList<HashSet<Pair<IRTemp, IRTemp>>> in) {
    boolean changed = false;
    for (IRBlock block : table) {
      for (IRStmt stmt : block.toList()) {
        HashSet<IRTemp> usedTemps = LiveVar.use(stmt);
        HashSet<Pair<IRTemp, IRTemp>> relPairs =
            getRelevantCopyPairs(in.get(stmt.getIndex()), usedTemps);
        changed = changed || stmt.propagateCopy(relPairs);
      }
    }
    return changed;
  }

  public HashSet<Pair<IRTemp, IRTemp>> getRelevantCopyPairs(
      HashSet<Pair<IRTemp, IRTemp>> in, HashSet<IRTemp> usedTemps) {
    HashSet<Pair<IRTemp, IRTemp>> res = new HashSet();
    for (IRTemp useTemp : usedTemps) {
      for (Pair<IRTemp, IRTemp> p : in) {
        if (p.first().equals(useTemp)) res.add(p);
      }
    }
    return res;
  }

  public ArrayList<IRBlock> getTable() {
    return table;
  }

  public Map<String, Integer> getMap() {
    return map;
  }
}

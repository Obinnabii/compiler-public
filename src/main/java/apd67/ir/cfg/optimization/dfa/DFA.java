package apd67.ir.cfg.dfa;

import apd67.ir.IRStmt;
import apd67.ir.cfg.CFG;
import apd67.ir.cfg.IRBlock;
import apd67.util.Pair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public abstract class DFA<T> {
  // blocks in the cfg
  protected ArrayList<IRBlock> blocks;
  protected Map<String, Integer> blockMap;

  // data structs for running the dataflow analysis
  protected ArrayList<HashSet<T>> inOld;
  protected ArrayList<HashSet<T>> inNew;
  protected ArrayList<HashSet<T>> outOld;
  protected ArrayList<HashSet<T>> outNew;

  // lookup table for converting from
  protected int[] lookupIndices;

  // Lists of precomputed predecessor and successor indices
  protected ArrayList<ArrayList<Integer>> pred;
  protected ArrayList<ArrayList<Integer>> succ;

  public DFA(CFG cfg) {
    // initialize map
    blocks = cfg.getTable();
    blockMap = cfg.getMap();

    // block offsets
    lookupIndices = new int[blocks.size()];

    // Compute the statement/block offsets
    int numStmts = 0;
    for (int i = 0; i < blocks.size(); i++) {
      IRBlock b = blocks.get(i);
      // set indices for each statement
      for (int j = 0; j < b.cfgLength(); j++) b.getStmt(j).setIndex(j + numStmts);

      lookupIndices[i] = numStmts;
      numStmts += b.cfgLength();
    }

    // Initialize the Arrays (set capacity)
    pred = new ArrayList<>(numStmts);
    succ = new ArrayList<>(numStmts);
    inOld = new ArrayList<>(numStmts);
    inNew = new ArrayList<>(numStmts);
    outOld = new ArrayList<>(numStmts);
    outNew = new ArrayList<>(numStmts);
    for (int i = 0; i < numStmts; i++) {
      pred.add(new ArrayList());
      succ.add(new ArrayList());
      inOld.add(new HashSet());
      inNew.add(new HashSet());
      outOld.add(new HashSet());
      outNew.add(new HashSet());
    }

    computePredAndSucc();
  }

  public Pair<ArrayList<HashSet<T>>, ArrayList<HashSet<T>>> analyze() {
    return analyze(false);
  }

  /** @return */
  public Pair<ArrayList<HashSet<T>>, ArrayList<HashSet<T>>> analyze(boolean annotate) {
    do {
      int idx = 0;
      for (int blockIdx = 0; blockIdx < blocks.size(); blockIdx++) {
        IRBlock b = blocks.get(blockIdx);

        for (int j = 0; j < b.cfgLength(); j++) {
          // copy so old doesn't get updated when new is
          HashSet<T> inCopy = new HashSet<T>();
          inCopy.addAll(inNew.get(idx));
          inOld.set(idx, inCopy);

          HashSet<T> outCopy = new HashSet<T>();
          outCopy.addAll(outNew.get(idx));
          outOld.set(idx, outCopy);

          IRStmt stmt = b.getStmt(j);

          // logic about predecessors
          inNew.set(idx, in(stmt, pred.get(idx)));
          outNew.set(idx, out(stmt, succ.get(idx)));

          idx++;
        }
      }
    } while (!(inOld.equals(inNew) && outOld.equals(outNew)));
    if (annotate) annotate();
    return new Pair(inNew, outNew);
  }

  protected abstract HashSet<T> in(IRStmt stmt, List<Integer> preds);

  protected abstract HashSet<T> out(IRStmt stmt, List<Integer> succs);

  protected void computePredAndSucc() {
    // Loop through all the blocks
    for (int blockIndex = 0; blockIndex < blocks.size(); blockIndex++) {
      IRBlock b = blocks.get(blockIndex);

      // Loop through all the statements in the block
      for (int stmtIndex = 0; stmtIndex < b.cfgLength(); stmtIndex++) {
        Integer stmntLookupIndex = lookupIndex(blockIndex, stmtIndex);

        // Exit node (Look at the out edges of the block)
        if (stmtIndex == b.cfgLength() - 1) {
          for (Integer blockSuccIndex : nameToBlockIndex(b.out())) {
            blockSuccIndex = lookupIndices[blockSuccIndex];
            pred.get(blockSuccIndex).add(stmntLookupIndex);
            succ.get(stmntLookupIndex).add(blockSuccIndex);
          }
        } // Regular statement
        else {
          pred.get(stmntLookupIndex + 1).add(stmntLookupIndex);
          succ.get(stmntLookupIndex).add(stmntLookupIndex + 1);
        }
      }
    }
  }

  /**
   * Convert a list of block names to a list of block indices in {@code blocks}
   *
   * @param blockNameList
   * @return {@code blockNameList} as a list of indices
   */
  protected ArrayList<Integer> nameToBlockIndex(List<String> blockNameList) {
    ArrayList<Integer> result = new ArrayList();
    for (String name : blockNameList) {
      result.add(blockMap.get(name));
    }
    return result;
  }

  /**
   * Returns the offest for statement [stmtIdx] within block [blockIdx] inside inNew, inOld, outNew,
   * and outOld.
   */
  protected int lookupIndex(int blockIdx, int stmtIdx) {
    return lookupIndices[blockIdx] + stmtIdx;
  }

  /** Returns the index of the last statement in the block. */
  protected int lookupEndingIndex(int blockIdx) {
    return lookupIndex(blockIdx, blocks.get(blockIdx).cfgLength() - 1);
  }

  protected void annotate() {
    for (IRBlock b : blocks) {
      for (int j = 0; j < b.cfgLength(); j++) {
        IRStmt s = b.getStmt(j);
        s.inCFGAnnotate = stringify(inNew.get(s.getIndex()));
        s.outCFGAnnotate = stringify(outNew.get(s.getIndex()));
      }
    }
  }

  protected abstract String stringify(HashSet<T> t);
}

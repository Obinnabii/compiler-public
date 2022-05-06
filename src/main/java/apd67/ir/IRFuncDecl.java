package apd67.ir;

import apd67.ir.cfg.*;
import apd67.ir.cfg.dfa.*;
import apd67.ir.cfg.dfa.LiveVar;
import apd67.ir.visit.AggregateVisitor;
import apd67.ir.visit.IRVisitor;
import apd67.ir.visit.InsnMapsBuilder;
import apd67.ir.visit.LowerVisitor;
import apd67.ir.visit.TranslationResult;
import apd67.util.Pair;
import apd67.util.cs4120.SExpPrinter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/** An IR function declaration */
public class IRFuncDecl extends IRNode_c {
  private String name;
  private IRStmt body; // do not change this directly, use updatedBody()
  private CFG cfg;
  private boolean updatedBody = false;

  public IRFuncDecl(String name, IRStmt body) {
    this.name = name;
    this.body = body;
  }

  public String name() {
    return name;
  }

  public IRStmt body() {
    return body;
  }

  @Override
  public String label() {
    return "FUNC " + name;
  }

  @Override
  public IRNode visitChildren(IRVisitor v) {
    IRStmt stmt = (IRStmt) v.visit(this, body);

    if (stmt != body) return v.nodeFactory().IRFuncDecl(name, stmt);

    return this;
  }

  @Override
  public TranslationResult lower(LowerVisitor v) {
    LinkedList<IRStmt> stmts = body.getTranslation().getStmts();
    // stmts.addFirst(new IRLabel(name));
    return new TranslationResult(stmts);
  }

  // initialize CFG if not made yet, otherwise pass
  private void initCFG() {
    if (cfg == null) {
      List<IRStmt> stmts = ((IRSeq) body).stmts();
      cfg = IRBlock.makeCFG(stmts);
      cfg.name = name;
      // System.out.println(cfg.toDot());
      updatedBody = false;
    }
  }

  private void updateCFG() {
    if (updatedBody) {
      List<IRStmt> stmts = ((IRSeq) body).stmts();
      cfg = IRBlock.makeCFG(stmts);
      cfg.name = name;
      // System.out.println(cfg.toDot());
      updatedBody = false;
    }
  }

  private void updateBody(IRStmt newBody) {
    body = newBody;
    updatedBody = true;
  }

  private void initOrUpdateCFG() {
    if (cfg == null) initCFG();
    else updateCFG();
  }

  public void eliminateDeadCode() {
    initOrUpdateCFG();
    boolean change = false;
    int maxLoop = 100;
    int i = 0;
    do {
      LiveVar lv = new LiveVar(cfg);
      lv.analyze();
      Pair<IRSeq, Boolean> deadCodePass = cfg.toLiveSeq();
      updateBody(deadCodePass.first());
      change = deadCodePass.second();
      updateCFG();
    } while (change && i++ < maxLoop);
  }

  public void propagateCopies() {
    initOrUpdateCFG();
    boolean change = false;
    int maxLoop = 25;
    int i = 0;
    do {
      AC ac = new AC(cfg);
      Pair<ArrayList<HashSet<Pair<IRTemp, IRTemp>>>, ArrayList<HashSet<Pair<IRTemp, IRTemp>>>> out =
          ac.analyze();
      change = cfg.propagateCopies(out.first());
      updateBody(cfg.toSeq());
      updateCFG();
    } while (change && i++ < maxLoop);
  }

  public void reorder() {
    // List<IRStmt> stmts = ((IRSeq) body).stmts();
    // CFG cfg = IRBlock.makeCFG(stmts);
    initCFG();

    // LiveVar lv = new LiveVar(cfg);
    // lv.analyze();
    // System.out.println(cfg.toDot());

    // eliminateDeadCode();

    // ae();

    // cse();
    // propagateCopies();
    // eliminateDeadCode();
    // System.out.println(cfg.toDot());

    // System.out.println(cfg.toDot(true));

    // AC ac = new AC(cfg);
    // ac.analyze();
    // System.out.println(cfg.toDot());
    // LiveVar lv = new LiveVar(cfg);
    // lv.analyze();
    // AE ae = new AE(cfg);
    // ae.analyze();
    // System.out.println(cfg.toDot());
    // System.out.println(body);
    // data flow analysis???

    // System.out.println("before reorder:\n" + cfg);
    ArrayList<IRBlock> ordering = cfg.greedyReordering();
    // System.out.println("after reorder:\n" + ordering);
    CFG.fixJumps(ordering);
    // System.out.println("body before:\n" + body);
    updateBody(CFG.toSeq(ordering));
    // System.out.println("body after:\n" + body);
  }

  // perform available expressions analysis on function CFG
  public Pair<ArrayList<HashSet<IRExpr>>, ArrayList<HashSet<IRExpr>>> ae() {
    initOrUpdateCFG();
    AE ae = new AE(cfg);
    Pair<ArrayList<HashSet<IRExpr>>, ArrayList<HashSet<IRExpr>>> p = ae.analyze();
    return p;
  }

  // public Dom dom() {
  //   Dom d = new Dom(cfg);
  //   d.analyze();
  //   return d;
  // }

  // perform common subexpression elimination on the CFG
  public void cse(Pair<ArrayList<HashSet<IRExpr>>, ArrayList<HashSet<IRExpr>>> ae) {

    HashMap<IRExpr, IRTemp> cache = new HashMap<>();
    ArrayList<IRStmt> newBody = new ArrayList<>();
    int currStmt = 0;

    ArrayList<HashSet<IRExpr>> ins = ae.first();
    ArrayList<HashSet<IRExpr>> outs = ae.second();

    List<IRStmt> bodyStmts = ((IRSeq) body).stmts();
    // System.out.println(bodyStmts.size());
    // System.out.println(bodyStmts);
    for (IRStmt s : bodyStmts) {
      try {
        if (s.getIndex() != -1) { // check not label
          HashSet<IRExpr> inexprs = ins.get(s.getIndex());
          HashSet<IRExpr> outexprs = outs.get(s.getIndex());
          s.replaceAvailableExprs(cache, inexprs, outexprs, newBody);
        }
        newBody.add(s);
        // currStmt++;
      } catch (Exception e) {
        System.out.println("caught exception :(");
        System.out.println(e.getStackTrace());
        System.out.println(ins.size());
        System.out.println(outs.size());
        System.out.println(((IRSeq) body).stmts().size());
        System.exit(1);
      }
    }

    // use newbody to make new cfg
    // System.out.println(newBody);
    updateBody(new IRSeq(newBody));
    eliminateDeadCode();
    updateCFG();
  }

  public void cse() {
    cse(ae());
  }

  public void updateBodyForTranslation() {
    updateBody(new IRSeq(getTranslation().getStmts()));
  }

  @Override
  public <T> T aggregateChildren(AggregateVisitor<T> v) {
    T result = v.unit();
    result = v.bind(result, v.visit(body));
    return result;
  }

  @Override
  public InsnMapsBuilder buildInsnMapsEnter(InsnMapsBuilder v) {
    v.addNameToCurrentIndex(name);
    v.addInsn(this);
    return v;
  }

  @Override
  public IRNode buildInsnMaps(InsnMapsBuilder v) {
    return this;
  }

  @Override
  public void printSExp(SExpPrinter p) {
    p.startList();
    p.printAtom("FUNC");
    p.printAtom(name);
    body.printSExp(p);
    p.endList();
  }

  public String toDot(boolean annotate) {
    initOrUpdateCFG();
    return cfg.toDot(annotate);
  }

  public String toDot() {
    initOrUpdateCFG();
    return cfg.toDot();
  }
}

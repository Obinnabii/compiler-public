package apd67.util;

public class Pair<F, S> {
  protected final F first;
  protected final S second;

  public Pair(F first, S second) {
    this.first = first;
    this.second = second;
  }

  public F first() {
    return first;
  }

  public S second() {
    return second;
  }

  @Override
  public boolean equals(Object o) {
    if (!(this.getClass().equals(o.getClass()))) return false;
    Pair p = (Pair) o;
    return first.equals(p.first()) ? second.equals(p.second()) : false;
  }

  @Override
  public int hashCode() {
    return (first.toString() + second.toString()).hashCode();
  }

  @Override
  public String toString() {
    return "<"
        + first.toString().replace("\n", "")
        + ", "
        + second.toString().replace("\n", "")
        + ">";
  }

  public boolean contains(Object o) {
    return first.equals(o) || second.equals(o);
  }
}

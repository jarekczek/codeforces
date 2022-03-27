package jarekcf.rating;

public class Result {
  public String handle;
  // ranks are equal, unless it represent a tie from-to
  public int rank1;
  public int rank2;

  public Result(String handle, int rank) {
    this(handle, rank, rank);
  }

  public Result(String handle, int rank1, int rank2) {
    this.handle = handle;
    this.rank1 = rank1;
    this.rank2 = rank2;
  }

  @Override
  public String toString() {
    return this.handle + " [" + this.rank1 + ", " + this.rank2 + "]";
  }
}

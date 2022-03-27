package jarekcf.rating;

public class Rating {
  public String handle;
  public int rank;
  public double oldRating;
  public double newRating;

  public Rating(String handle) {
    this.handle = handle;
  }

  public void addRating(double delta) {
    this.newRating = this.oldRating + delta;
  }

  public void resetNew() {
    this.oldRating = this.newRating;
    this.newRating = 0d;
  }

  @Override
  public String toString() {
    return handle + " " + oldRating + " -> " + newRating;
  }

  public static int ratingComparatorDesc(Rating r1, Rating r2) {
    return -((Double)r1.newRating).compareTo(r2.newRating);
  }
}

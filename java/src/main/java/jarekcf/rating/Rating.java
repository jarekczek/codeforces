package jarekcf.rating;

public class Rating {
  public String handle;
  public int rank;
  public double oldRating;
  public double newRating;
  public double performance;
  public int contestsCount;
  private boolean active;

  public Rating(String handle, int rating) {
    this.handle = handle;
    this.oldRating = rating;
    this.newRating = rating;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public void addRatingToNew(double delta) {
    this.newRating = this.newRating + delta;
  }

  public void resetNew() {
    this.oldRating = this.newRating;
  }

  @Override
  public String toString() {
    return handle + " " + (int)oldRating + " -> " + (int)newRating;
  }

  public static int ratingComparatorDesc(Rating r1, Rating r2) {
    return -((Double)r1.newRating).compareTo(r2.newRating);
  }

  public boolean isProvisional() {
    return !active && contestsCount < 6;
  }
}

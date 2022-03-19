package jarekcf;

public class TopUserInfo {
  public String handle;
  public int place;
  public int rating;

  public TopUserInfo(String handle) {
    this.handle = handle;
  }

  public static int ratingComparatorDesc(TopUserInfo user1, TopUserInfo user2) {
    return -ratingComparatorAsc(user1, user2);
  }

  public static int ratingComparatorAsc(TopUserInfo user1, TopUserInfo user2) {
    int ratingCompare = Integer.compare(user1.rating, user2.rating);
    if (ratingCompare != 0) {
      return ratingCompare;
    } else {
      return user1.handle.compareTo(user2.handle);
    }
  }

  public String toString() {
    return "" + place + ". " + handle + " (" + rating + ")";
  }
}

package jarekcf;

import java.time.LocalDate;

public class TopUserInfo {
  public String handle;
  public int place;
  public int rating;
  public int contestCount;
  public LocalDate lastContestDate;

  public TopUserInfo(String handle) {
    this.handle = handle;
  }

  public String getColorNatalia() {
    if (rating <= 0) {
      return "0000_gray";
    } else if (rating < 1200) {
      return "0000_gray";
    } else if (rating < 1400) {
      return "1200_green";
    } else if (rating < 1600) {
      return "1400_cyan";
    } else if (rating < 1900) {
      return "1600_blue";
    } else if (rating < 2100) {
      return "1900_violet";
    } else if (rating < 2300) {
      return "2100_orange";
    } else if (rating < 2400) {
      return "2100_orange";
    } else if (rating < 2600) {
      return "2600_red";
    } else if (rating < 3000) {
      return "2600_red";
    } else {
      return "2600_red";
    }
  }

  public String getColor2022() {
    if (rating <= 0) {
      return "black";
    } else if (rating < 1200) {
      return "gray";
    } else if (rating < 1400) {
      return "green";
    } else if (rating < 1600) {
      return "cyan";
    } else if (rating < 1900) {
      return "blue";
    } else if (rating < 2100) {
      return "purple";
    } else if (rating < 2300) {
      return "yellow";
    } else if (rating < 2400) {
      return "orange";
    } else if (rating < 2600) {
      return "pink";
    } else if (rating < 3000) {
      return "red";
    } else {
      return "dark red";
    }
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

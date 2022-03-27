package jarekcf.rating;

import java.util.List;

public class EloRatingSystemNonProvisional extends EloRatingSystem {

  public EloRatingSystemNonProvisional(int startRating, double deltaFactor) {
    super(startRating, deltaFactor);
  }

  @Override
  protected void addNewUsersToRatings(List<Result> results) {
    super.addNewUsersToRatings(results);
    this.ratingsMap.values().forEach(rating -> rating.setActive(true));
  }
}

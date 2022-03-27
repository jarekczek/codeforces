package jarekcf.rating;

import java.util.List;

public interface RatingSystem {
  void addResults(List<Result> results);
  List<Rating> getRatings();
  Rating getRating(String handle);
  default double getNewRating(String handle) {
    return getRating(handle).newRating;
  };
  void addRating(String handle, int newRating);
}

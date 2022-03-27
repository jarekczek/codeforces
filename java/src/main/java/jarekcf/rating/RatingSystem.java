package jarekcf.rating;

import java.util.List;

public interface RatingSystem {
  void addResults(List<Result> results);
  List<Rating> getRatings();
  double getRating(String handle);
}

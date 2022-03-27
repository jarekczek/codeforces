package jarekcf.rating;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EloRatingSystem implements RatingSystem {
  private static final Logger log = LoggerFactory.getLogger(EloRatingSystem.class);

  private Map<String, Rating> ratingsMap = new HashMap<>();

  @Override
  public void addResults(List<Result> results) {
    results.forEach(result -> ratingsMap.computeIfAbsent(result.handle, (key) -> new Rating(key)));
    ratingsMap.values().forEach(Rating::resetNew);

    for (int i = 0; i < results.size(); i++) {
      for (int j = i + 1; j < results.size(); j++) {
        double delta = calcRatingChange(results.get(i), results.get(j));
        ratingsMap.get(results.get(i).handle).addRating(delta);
        ratingsMap.get(results.get(j).handle).addRating(-delta);
      }
    }
  }

  private double calcRatingChange(Result result1, Result result2) {
    double result = calcDoubleResult(result1, result2);
    double expectedResult = calcExpectedResult(result1, result2);
    log.debug("result " + result1 + " vs " + result2 + ": " + result + " (" + expectedResult + ")");
    return 16d * (result - expectedResult);
  }

  private double calcExpectedResult(Result result1, Result result2) {
    double rdiff400 = (getRating(result2) - getRating(result1)) / 400d;
    double denom = 1d + Math.pow(10, rdiff400);
    return 1d / denom;
  }

  private double getRating(Result result) {
    return ratingsMap.get(result.handle).oldRating;
  }

  private double calcDoubleResult(Result result1, Result result2) {
    {
      if (result1.rank1 == result2.rank1) {
        return 0.5d;
      } else if (result1.rank1 < result2.rank2) {
        // Who has lower rank (place on scoreboard) - wins.
        return 1;
      } else {
        return 0;
      }
    }
  }

  @Override
  public List<Rating> getRatings() {
    var list = new ArrayList<Rating>();
    list.addAll(ratingsMap.values());
    list.sort(Rating::ratingComparatorDesc);
    return list;
  }

  @Override
  public double getRating(String handle) {
    return ratingsMap.get(handle).newRating;
  }
}

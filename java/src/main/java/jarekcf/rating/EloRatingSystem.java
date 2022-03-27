package jarekcf.rating;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EloRatingSystem implements RatingSystem {
  private static final Logger log = LoggerFactory.getLogger(EloRatingSystem.class);

  protected int startRating = 0;
  protected final Map<String, Rating> ratingsMap = new HashMap<>();
  private double deltaFactor;

  public EloRatingSystem(int startRating, double deltaFactor) {
    this.startRating = startRating;
    this.deltaFactor = deltaFactor;
  }

  @Override
  public void addResults(List<Result> results) {
    ratingsMap.values().forEach(Rating::resetNew);
    addNewUsersToRatings(results);
    long resultsNonProvisionalCount = results.stream()
      .filter(result -> !ratingsMap.get(result.handle).isProvisional()).count();

    for (int i = 0; i < results.size(); i++) {
      if (ratingsMap.get(results.get(i).handle).isProvisional()) {
        continue;
      }
      for (int j = i + 1; j < results.size(); j++) {
        if (ratingsMap.get(results.get(j).handle).isProvisional()) {
          continue;
        }
        double delta = calcRatingChange(results.get(i), results.get(j), deltaFactor / resultsNonProvisionalCount);
        ratingsMap.get(results.get(i).handle).addRatingToNew(delta);
        ratingsMap.get(results.get(j).handle).addRatingToNew(-delta);
      }
    }

    calcPerformance(results);
  }

  protected void addNewUsersToRatings(List<Result> results) {
    results.forEach(result -> ratingsMap.computeIfAbsent(result.handle, (key) -> {
      Rating rating = new Rating(key, startRating);
      return rating;
    }));
  }

  private void calcPerformance(List<Result> results) {
    double avg = results.stream()
      .mapToDouble((result) -> ratingsMap.get(result.handle).oldRating)
      .average().getAsDouble();
    List<Result> sorted = results.stream().sorted(Result::rankComparatorAsc).collect(Collectors.toUnmodifiableList());
    for (Result result : sorted) {
      int looses = result.rank1 - 1;
      int wins = results.size() - result.rank2;
      ratingsMap.get(result.handle).performance = avg + 400d * (wins - looses) / results.size();
    }
  }

  private double calcRatingChange(Result result1, Result result2, double k) {
    double result = calcDoubleResult(result1, result2);
    double expectedResult = calcExpectedResult(result1, result2);
    log.debug("result " + result1 + " vs " + result2 + ": " + result + " (" + expectedResult + ")");
    return k * (result - expectedResult);
  }

  private double calcExpectedResult(Result result1, Result result2) {
    double rdiff400 = (getOldRating(result2) - getOldRating(result1)) / 400d;
    double denom = 1d + Math.pow(10, rdiff400);
    return 1d / denom;
  }

  private double getOldRating(Result result) {
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
    var list = new ArrayList<>(ratingsMap.values());
    list.sort(Rating::ratingComparatorDesc);
    return list;
  }

  @Override
  public Rating getRating(String handle) {
    return ratingsMap.get(handle);
  }

  @Override
  public void addRating(String handle, int newRating) {
    ratingsMap.compute(handle, (key, oldRating) -> {
      if (oldRating == null) {
        Rating rating = new Rating(key, newRating);
        rating.setActive(true);
        return rating;
      } else {
        oldRating.oldRating = newRating;
        oldRating.newRating = newRating;
        return oldRating;
      }
    });
  }

}

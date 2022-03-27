package jarekcf.rating;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

class EloRatingSystemTest {
  public static final Logger log = LoggerFactory.getLogger(EloRatingSystemTest.class);
  private RatingSystem system;

  @BeforeEach
  public void beforeEach() {
    system = new EloRatingSystem();
  }

  @Test
  void test2EqualChancesNoDraw() {
    runSystem2PlayersWithRepetitionsAndRatio(20, 1, 1, 0);
    Assertions.assertThat(system.getRatings().get(0).newRating).isBetween(-5d, 5d);
    Assertions.assertThat(system.getRatings().get(1).newRating).isBetween(-5d, 5d);
    Assertions.assertThat(system.getRatings().get(0).newRating)
      .isEqualTo(-system.getRatings().get(1).newRating, Offset.offset(0.001d));
  }

  @Test
  void test2EqualChancesWithDraw() {
    runSystem2PlayersWithRepetitionsAndRatio(20, 1, 1, 1);
    Assertions.assertThat(system.getRatings().get(0).newRating).isBetween(-5d, 5d);
    Assertions.assertThat(system.getRatings().get(1).newRating).isBetween(-5d, 5d);
    Assertions.assertThat(system.getRatings().get(0).newRating)
      .isEqualTo(-system.getRatings().get(1).newRating, Offset.offset(0.001d));
  }

  @Test
  void test2W1L10ChancesWithDraw() {
    runSystem2PlayersWithRepetitionsAndRatio(20, 1, 10, 0);
    Assertions.assertThat(system.getNewRating("B") - system.getNewRating("A"))
      .isEqualTo(400d, Offset.offset(10d));
  }

  @Test
  public void test3Users() {
    for (int r = 0; r < 200; r++) {
      system.addResults(Arrays.asList(new Result("A", 1, 1), new Result("B1", 2, 3), new Result("B2", 2, 3)));
      system.getRatings().forEach(rating -> log.debug(rating.toString()));
      system.addResults(Arrays.asList(new Result("A", 1, 1), new Result("B1", 2, 3), new Result("B2", 2, 3)));
      system.getRatings().forEach(rating -> log.debug(rating.toString()));
      system.addResults(Arrays.asList(new Result("A", 3, 3), new Result("B1", 1, 2), new Result("B2", 1, 2)));
      system.getRatings().forEach(rating -> log.debug(rating.toString()));
    }
    Assertions.assertThat(system.getNewRating("A") - system.getNewRating("B1")).isEqualTo(120d, Offset.offset(20d));
    Assertions.assertThat(system.getNewRating("B1")).isEqualTo(system.getNewRating("B2"), Offset.offset(10d));
  }

  void runSystem2PlayersWithRepetitionsAndRatio(int repetitions, int timesWin, int timesLoose, int timesDraw) {
    for (int r = 0; r < repetitions; r++) {
      for (int i = 0; i < timesWin; i++) {
        system.addResults(Arrays.asList(new Result("A", 1), new Result("B", 2)));
        system.getRatings().forEach(rating -> log.debug(rating.toString()));
      }
      for (int i = 0; i < timesLoose; i++) {
        system.addResults(Arrays.asList(new Result("A", 2), new Result("B", 1)));
        system.getRatings().forEach(rating -> log.debug(rating.toString()));
      }
      for (int i = 0; i < timesDraw; i++) {
        system.addResults(Arrays.asList(new Result("A", 1, 2), new Result("B", 1, 2)));
        system.getRatings().forEach(rating -> log.debug(rating.toString()));
      }
    }
  }
}
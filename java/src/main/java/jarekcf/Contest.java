package jarekcf;

import jarekcf.dto.ContestDto;
import jarekcf.dto.StandingsDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class Contest extends ContestDto {
  public StandingsDto standings;
  public List<RatingChange> ratingChanges;

  public LocalDate getDate() {
    var instant = Instant.ofEpochSecond(startTimeSeconds);
    return LocalDate.from(instant.atZone(ZoneId.systemDefault()));
  }

  public static int dateComparatorDesc(Contest contest1, Contest contest2) {
    return -dateComparatorAsc(contest1, contest2);
  }

  public static int dateComparatorAsc(Contest contest1, Contest contest2) {
    int dateResult = contest1.getDate().compareTo(contest2.getDate());
    if (dateResult != 0) {
      return dateResult;
    } else {
      return Integer.compare(contest1.id, contest2.id);
    }
  }
}

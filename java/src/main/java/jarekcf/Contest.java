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
}

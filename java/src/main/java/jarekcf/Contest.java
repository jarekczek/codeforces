package jarekcf;

import jarekcf.dto.ContestDto;
import jarekcf.dto.StandingsDto;

import java.util.List;

public class Contest extends ContestDto {
  public StandingsDto standings;
  public List<RatingChange> ratingChanges;
}

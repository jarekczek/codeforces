package jarekcf.api;

import com.fasterxml.jackson.core.type.TypeReference;
import jarekcf.RatingChange;
import jarekcf.dto.Response;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ContestRatingChangesApi extends ApiBase<List<RatingChange>>{
  public List<RatingChange> get(int contestId) {
    List<RatingChange> list = get(baseUrl() + getName()
      + "?contestId=" + contestId);
    if (list == null) {
      list = Collections.emptyList();
    }
    return list;
  }

  @Override
  public String getName() {
    return "contest.ratingChanges";
  }

  @Override
  public TypeReference cfResponseJacksonType() {
    return new TypeReference<Response<List<RatingChange>>>() {};
  }
}

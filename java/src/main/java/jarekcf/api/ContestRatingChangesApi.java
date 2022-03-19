package jarekcf.api;

import com.fasterxml.jackson.core.type.TypeReference;
import jarekcf.RatingChange;
import jarekcf.dto.Response;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class ContestRatingChangesApi extends ApiBase<List<RatingChange>>{
  public List<RatingChange> get(int contestId) {
    if (Arrays.asList(1595, 1596, 1597).contains(contestId)) {
      //{"status":"FAILED","comment":"contestId: Contest with id 1597 not found"}
      return Collections.emptyList();
    }
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

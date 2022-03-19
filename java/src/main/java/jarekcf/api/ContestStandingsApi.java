package jarekcf.api;

import com.fasterxml.jackson.core.type.TypeReference;
import jarekcf.dto.Response;
import jarekcf.dto.StandingsDto;
import org.springframework.stereotype.Component;

@Component
public class ContestStandingsApi extends ApiBase<StandingsDto> {
  public StandingsDto get(int contestId) {
    return get(baseUrl() + getName()
      + "?contestId=" + contestId
      + "&from=1&count=123456&showUnofficial=false");
  }

  @Override
  public String getName() {
    return "contest.standings";
  }

  @Override
  public TypeReference cfResponseJacksonType() {
    return new TypeReference<Response<StandingsDto>>() {};
  }
}

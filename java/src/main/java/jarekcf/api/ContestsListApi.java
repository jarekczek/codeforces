package jarekcf.api;

import com.fasterxml.jackson.core.type.TypeReference;
import jarekcf.Contest;
import jarekcf.dto.Response;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContestsListApi extends ApiBase<List<Contest>> {
  public List<Contest> get() {
    return get(baseUrl() + getName());
  }

  @Override
  public String getName() {
    return "contest.list";
  }

  @Override
  public TypeReference cfResponseJacksonType() {
    return new TypeReference<Response<List<Contest>>>() {};
  }

}

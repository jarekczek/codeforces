package jarekcf;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Contests {
  private final List<Contest> contestList;
  private final Map<Integer, Contest> mapById;

  public Contests(List<Contest> contestList) {
    this.contestList = contestList;
    this.mapById = contestList.stream()
      .collect(Collectors.toUnmodifiableMap(contest -> contest.id, Function.identity()));
  }

  public List<Contest> getList() {
    return Collections.unmodifiableList(contestList);
  }

  public Contest get(int id) {
    return mapById.get(id);
  }
}

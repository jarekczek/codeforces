package jarekcf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsersStatsCalculator {
  private Map<String, TopUserInfo> userMap = new HashMap<>();

  public void addContest(Contest contest) {
    contest.ratingChanges.forEach(change -> {
      TopUserInfo userInfo;
        if (userMap.containsKey(change.handle)) {
          userInfo = userMap.get(change.handle);
        } else {
          userInfo = new TopUserInfo(change.handle);
          userMap.put(change.handle, userInfo);
        }
      userInfo.rating = change.newRating;
      }
    );
  }

  public List<TopUserInfo> getTop() {
    List<TopUserInfo> users = userMap.values().stream()
      .sorted(TopUserInfo::ratingComparatorDesc)
      .collect(Collectors.toUnmodifiableList());
    for (int i = 0; i < users.size(); i++) {
      users.get(i).place = i + 1;
    }
    return users;
  }
}

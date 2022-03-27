package jarekcf;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsersStatsCalculator {
  public static final int MINIMUM_CONTESTS = 6;
  public static final int MAX_INACTIVE_MONTHS = 6;

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
        userInfo.lastContestDate = contest.getDate();
        userInfo.contestCount++;
      }
    );
  }

  public List<TopUserInfo> getTop(LocalDate reportDate) {
    List<TopUserInfo> users = getActiveUsers(reportDate);
    for (int i = 0; i < users.size(); i++) {
      users.get(i).place = i + 1;
    }
    return users;
  }

  public Map<String, Integer> getColorCount(LocalDate reportDate) {
    List<TopUserInfo> users = getActiveUsers(reportDate);
    var colorMap = new HashMap<String, Integer>();
    users.forEach(user -> {
      colorMap.compute(user.getColorNatalia(), (s, i) -> (i == null ? 0 : i) + 1);
    });
    return colorMap;
  }

  public List<TopUserInfo> getActiveUsers(LocalDate reportDate) {
    List<TopUserInfo> allUsers = userMap.values().stream()
      .sorted(TopUserInfo::ratingComparatorDesc)
      .collect(Collectors.toUnmodifiableList());
    List<TopUserInfo> users = allUsers.stream()
      .filter(user -> user.contestCount >= MINIMUM_CONTESTS)
      .filter(user -> user.lastContestDate.plusMonths(MAX_INACTIVE_MONTHS).isAfter(reportDate))
      .collect(Collectors.toUnmodifiableList());
    return users;
  }
}

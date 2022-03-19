package jarekcf;

import jarekcf.api.ContestRatingChangesApi;
import jarekcf.api.ContestStandingsApi;
import jarekcf.api.ContestsListApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CfApi {
  @Autowired
  ContestsListApi contestsListApi;

  @Autowired
  ContestStandingsApi contestStandingsApi;

  @Autowired
  ContestRatingChangesApi ratingChangesApi;

  public Contests getContestList() {
    List<Contest> contestList = contestsListApi.get();
    contestList.sort(Contest::dateComparatorAsc);
    return new Contests(contestList);
  }

  public Contest getContest(int contestId) {
    var contest = new Contest();
    //contest.standings = contestStandingsApi.get(contestId);
    contest.ratingChanges = ratingChangesApi.get(contestId);
    return contest;
  }
}

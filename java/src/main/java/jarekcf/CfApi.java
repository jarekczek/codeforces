package jarekcf;

import jarekcf.api.ContestRatingChangesApi;
import jarekcf.api.ContestStandingsApi;
import jarekcf.api.ContestsListApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CfApi {
  private Contests contests;

  @Autowired
  ContestsListApi contestsListApi;

  @Autowired
  ContestStandingsApi contestStandingsApi;

  @Autowired
  ContestRatingChangesApi ratingChangesApi;

  public Contests getContestList() {
    if (this.contests == null) {
      List<Contest> contestList;
      contestList = contestsListApi.get();
      contestList.sort(Contest::dateComparatorAsc);
      this.contests = new Contests(contestList);
    }
    return contests;
  }

  public Contest getContest(int contestId) {
    Contest contest = this.contests.get(contestId);
    //contest.standings = contestStandingsApi.get(contestId);
    contest.ratingChanges = ratingChangesApi.get(contestId);
    return contest;
  }
}

package jarekcf;

import jarekcf.dto.ContestDto;
import jarekcf.rating.EloRatingSystem;
import jarekcf.rating.EloRatingSystemNonProvisional;
import jarekcf.rating.Rating;
import jarekcf.rating.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
@EnableCaching
public class Go implements CommandLineRunner {
  private static final Logger log = LoggerFactory.getLogger(Go.class);

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  CfApi cfApi;

  @Autowired
  Environment environment;

  List<String> lines = new ArrayList<>();

  public static void main(String[] args) {
    SpringApplication.run(Go.class, args);
  }

  @Override
  public void run(String... args) {
    Contests contests = cfApi.getContestList();
    System.out.println(contests.getList().size());
    System.out.println(contests.get(1647).type);
    contests.getList().stream()
      .filter(contest -> contest.phase.equals(ContestDto.Phase.FINISHED))
      .filter(contest -> contest.name.contains("Div. 2"))
      .filter(contest -> contest.getDate().isAfter(LocalDate.parse("2020-01-31")))
      .filter(contest -> contest.getDate().isBefore(LocalDate.parse("2020-07-31")))
      .filter((contest) -> false)
      .forEach(contest -> {
        System.out.println(contest.getDate() + " " + contest.name);
        Contest full = cfApi.getContest(contest.id);
        System.out.println(full.ratingChanges.stream().mapToInt(chg -> chg.newRating).min());
      });

    //gatherUserStats(contests);
    elo(contests);

    System.exit(0);
  }

  private void elo(Contests contests) {
    var system = new EloRatingSystem(1500, 277d);
    int iContest1 = 1;
    int iContest2 = 10;
    for (int i = 0; i < iContest1; i++) {
      System.out.println("added calculated results from " + contests.getList().get(i));
      addCalculatedResults(system, cfApi.getContest(contests.getList().get(i).id));
    }
    for (int i = iContest1; i <= iContest2; i++) {
      Contest contest = cfApi.getContest(contests.getList().get(i).id);
      system.addResults(cfContestToResults(contest));
      System.out.println("added results from " + contest);
      for (RatingChange chg : contest.ratingChanges) {
        Rating rating = system.getRating(chg.handle);
        if (rating.isProvisional()) {
          continue;
        }
        log.debug("{} {} -> {}, u mnie {}", chg.handle, chg.oldRating, chg.newRating,
          rating);
      }
    }
  }

  private void addCalculatedResults(EloRatingSystem system, Contest contest) {
    for (RatingChange chg : contest.ratingChanges) {
      system.addRating(chg.handle, chg.newRating);
    }
  }

  private List<Result> cfContestToResults(Contest contest) {
    int iSameRankStart = -1;
    int prevRank = -1;
    var results = new ArrayList<Result>();
    for (int i = 0; i < contest.ratingChanges.size(); i++) {
      RatingChange chg = contest.ratingChanges.get(i);
      int nextRank = i == contest.ratingChanges.size() - 1 ? Integer.MAX_VALUE : contest.ratingChanges.get(i + 1).rank;
      if (chg.rank != prevRank && chg.rank != nextRank) {
        results.add(new Result(chg.handle, chg.rank));
      } else if (chg.rank != prevRank && chg.rank == nextRank) {
        // This is the beginning of the draw.
        iSameRankStart = i;
      } else if (chg.rank == prevRank && chg.rank == nextRank) {
        // We are inside a draw, wait until last drawn player.
      } else if ((chg.rank == prevRank && chg.rank != nextRank) || i == contest.ratingChanges.size() - 1) {
        // Last row of a draw, add results of all the drawn players.
        int iSameRankEnd = i;
        int rankStart = contest.ratingChanges.get(iSameRankStart).rank;
        int rankEnd = chg.rank;
        for (int j = iSameRankStart; j <= iSameRankEnd; j++) {
          RatingChange chgJ = contest.ratingChanges.get(j);
          results.add(new Result(chgJ.handle, rankStart, rankEnd));
        }
      }
      prevRank = chg.rank;
    }

    return results;
  }

  private void gatherUserStats(Contests contests) {
    var stats = new UsersStatsCalculator();
    final AtomicReference<LocalDate> lastContestDate = new AtomicReference<>();
    final AtomicReference<LocalDate> reportDate = new AtomicReference(LocalDate.parse("2010-03-31"));
    lines.add("reportDate\tcolor\tcount");
    contests.getList().stream()
      .filter(cont -> cont.phase.equals(ContestDto.Phase.FINISHED))
      .filter(cont -> cont.getDate().isAfter(LocalDate.parse("2010-01-01")))
      .filter(cont -> cont.getDate().isBefore(LocalDate.parse("2023-01-01")))
      .forEach(cont -> {
        lastContestDate.set(cont.getDate());
        if (cont.getDate().isAfter(reportDate.get())) {
          writeReport(reportDate.get(), stats);
          reportDate.set(reportDate.get().plusMonths(3));
        }
        System.out.println(cont.id + " " + cont.getDate() + " " + cont.name);
        var fullContest = cfApi.getContest(cont.id);
        stats.addContest(fullContest);
      });
    writeReport(lastContestDate.get(), stats);
    //stats.getTop().stream()
    //  .limit(10)
    //  .forEach(top -> System.out.println(top));
    lines.forEach(System.out::println);
  }

  private void writeReport(LocalDate reportDate, UsersStatsCalculator stats) {
    lines.add("" + reportDate + "\t" + "total" + "\t" + stats.getTop(reportDate).size());
    stats.getColorCount(reportDate).entrySet().forEach(entry -> {
      lines.add("" + reportDate + "\t" + entry.getKey() + "\t" + entry.getValue());
    });
  }
}

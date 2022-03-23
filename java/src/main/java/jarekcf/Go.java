package jarekcf;

import jarekcf.dto.ContestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
@EnableCaching
public class Go implements CommandLineRunner {
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
      .filter(contest -> contest.getDate().isAfter(LocalDate.now().minusDays(20)))
      .forEach(contest -> System.out.println(contest.getDate() + " " + contest.name));

    gatherUserStats(contests);

    System.exit(0);
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

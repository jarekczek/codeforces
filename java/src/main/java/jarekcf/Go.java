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

@SpringBootApplication
@EnableCaching
public class Go implements CommandLineRunner {
  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  CfApi cfApi;

  @Autowired
  Environment environment;

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

    var stats = new UsersStatsCalculator();
    contests.getList().stream()
        .filter(cont -> cont.phase.equals(ContestDto.Phase.FINISHED))
        .filter(cont -> cont.getDate().isAfter(LocalDate.now().minusDays(180)))
        .forEach(cont -> {
          System.out.println(cont.id + " " + cont.getDate() + " " + cont.name);
          var fullContest = cfApi.getContest(cont.id);
          stats.addContest(fullContest);
        });
    stats.getTop().stream()
      .limit(10)
      .forEach(top -> System.out.println(top));

    System.exit(0);
  }
}

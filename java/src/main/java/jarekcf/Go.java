package jarekcf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

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
    System.out.println(contests.get(1647).name);

    var contest = cfApi.getContest(1647);
    System.out.println(contest.standings.rows.get(0).getHandle());
    System.out.println(contest.ratingChanges.get(0).handle);
    System.out.println(contest.ratingChanges.get(0).newRating);
    System.exit(0);
  }
}

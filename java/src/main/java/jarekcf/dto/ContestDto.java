package jarekcf.dto;

public class ContestDto {
  public int id;
  public String name;
  public ContestType type;
  public Phase phase;
  public boolean frozen;
  public int startTimeSeconds;

  public enum ContestType {
    CF,
    IOI,
    ICPC
  }

  public enum Phase {
    BEFORE, CODING, PENDING_SYSTEM_TEST, SYSTEM_TEST, FINISHED
  }
}

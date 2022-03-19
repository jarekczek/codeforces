package jarekcf;

import jarekcf.dto.RanklistRowDto;

public class RanklistRow extends RanklistRowDto {
  public String getHandle() {
    return party.members.get(0).handle;
  }
}

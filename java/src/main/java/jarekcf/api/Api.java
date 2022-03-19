package jarekcf.api;

import com.fasterxml.jackson.core.type.TypeReference;

public interface Api {
  String getName();
  TypeReference cfResponseJacksonType();
}

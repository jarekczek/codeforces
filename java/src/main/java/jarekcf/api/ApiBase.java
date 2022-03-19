package jarekcf.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jarekcf.WebProxy;
import jarekcf.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public abstract class ApiBase<T> implements Api {
  @Autowired
  WebProxy webProxy;

  public T get(String url) {
    byte[] rawResponseData = webProxy.getRaw(url);
    try {
      ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      Response<T> cfResponse = (Response<T>) mapper.readValue(rawResponseData, cfResponseJacksonType());
      return cfResponse.result;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String baseUrl() {
    return "https://codeforces.com/api/";
  }
}

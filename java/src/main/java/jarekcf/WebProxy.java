package jarekcf;

import jarekcf.dto.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class WebProxy {
  public static final Logger log = LoggerFactory.getLogger(WebProxy.class);
  @Cacheable(value = "localDirCache")
  public byte[] getRaw(String url) {
    log.info("Need to fetch " + url);
    WebClient webClient = WebClient.builder()
      .codecs(conf -> conf.defaultCodecs().maxInMemorySize(20*1024*1024))
      .baseUrl(url)
      .build();
    WebClient.RequestHeadersUriSpec<?> req = webClient.get();
    ResponseEntity<byte[]> rsp = req.retrieve()
      .onStatus(HttpStatus::isError, response -> Mono.empty())
      .toEntity(byte[].class)
      .block();
    byte[] body = rsp.getBody();
    try {
      JSONObject o = new JSONObject(new String(body, StandardCharsets.ISO_8859_1));
      if (treatResponseAsError(o)) {
        throw new RuntimeException(o.getString("comment"));
      }
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
    return body;
  }

  private boolean treatResponseAsError(JSONObject o) {
    if (o.getString("status").equals("OK")) {
      return false;
    } else if (o.getString("comment").equals("contestId: Rating changes are unavailable for this contest")) {
      return false;
    } else {
      return true;
    }
  }
}

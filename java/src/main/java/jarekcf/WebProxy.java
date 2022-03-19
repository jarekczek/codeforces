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
    var parametrizedType = new ParameterizedTypeReference<Response<Object>>() {};
    ResponseEntity<byte[]> rsp = req.retrieve().toEntity(byte[].class).block();
    if (rsp.getStatusCode() != HttpStatus.OK) {
      throw new RuntimeException("https status " + rsp.getStatusCode());
    }
    byte[] body = rsp.getBody();
    try {
      JSONObject o = new JSONObject(new String(body, StandardCharsets.ISO_8859_1));
      if (!o.getString("status").equals("OK")) {
        throw new RuntimeException(o.getString("comment"));
      }
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
    return body;
  }
}

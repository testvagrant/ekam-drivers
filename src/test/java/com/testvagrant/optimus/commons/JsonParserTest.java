package com.testvagrant.optimus.commons;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.testvagrant.optimus.commons.filehandlers.JsonParser;
import lombok.Getter;
import lombok.Setter;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.Objects;

@Guice()
public class JsonParserTest {

  @Inject private JsonParser jsonParser;

  @Test
  public void testFeedParserTest() {
    JsonObject testFeed = jsonParser.deserialize("sampletestfeed", JsonObject.class);
    Assert.assertTrue(Objects.nonNull(testFeed));
  }

  @Test
  public void sampleParserTest() {
    Sample sample = jsonParser.deserialize("sample", Sample.class);
    Assert.assertEquals(sample.getUsername(), "abc");
    Assert.assertEquals(sample.getPassword(), "xyz");
  }

  @Getter
  @Setter
  private static class Sample {
    private String username;
    private String password;
  }
}

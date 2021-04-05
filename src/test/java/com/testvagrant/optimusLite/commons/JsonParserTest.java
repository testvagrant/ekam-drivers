package com.testvagrant.optimusLite.commons;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.testvagrant.optimusLite.commons.utils.JsonParser;
import lombok.Getter;
import lombok.Setter;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

@Guice(modules = {})
public class JsonParserTest {

    @Inject
    JsonParser jsonParser;

    @Test
    public void testFeedParserTest() throws FileNotFoundException {
        JsonObject calculator = jsonParser.getAppJson("calculator", JsonObject.class);
        Assert.assertEquals(calculator.get("executionDetails").getAsJsonObject().get("monitoring").getAsBoolean(), true);
    }

    @Test
    public void sampleParserTest() throws FileNotFoundException {
        Sample sample = jsonParser.getAppJson("sample", Sample.class);
        Assert.assertEquals(sample.getUsername(), "abc");
        Assert.assertEquals(sample.getPassword(), "xyz");
    }
}

@Getter @Setter
class Sample {
  private String username;
  private String password;
}

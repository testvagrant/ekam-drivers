package com.testvagrant.optimus.cloudConfig;

import com.testvagrant.optimus.core.models.CloudConfig;
import com.testvagrant.optimus.core.remote.RemoteUrlBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URL;

public class CloudConfigTest {

    @Test
    public void defaultCloudConfigTest() {
        CloudConfig cloudConfig = CloudConfig.configBuilder()
                .accessKey("sjajs")
                .username("hskahs")
                .hub("hub.browserstack.com")
                .build();

        URL url = RemoteUrlBuilder.build(cloudConfig);
        Assert.assertEquals(url.getHost(), cloudConfig.getHub());
        Assert.assertEquals(url.getProtocol(), cloudConfig.getProtocol());
        Assert.assertEquals(url.getPath(), "/wd/hub");
    }

    @Test
    public void cloudConfigTestWithProtocol() {
        CloudConfig cloudConfig = CloudConfig.configBuilder()
                .accessKey("sjajs")
                .username("hskahs")
                .hub("hub.browserstack.com")
                .protocol("http")
                .build();

        URL url = RemoteUrlBuilder.build(cloudConfig);
        Assert.assertEquals(url.getHost(), cloudConfig.getHub());
        Assert.assertEquals(url.getProtocol(), cloudConfig.getProtocol());
        Assert.assertEquals(url.getPath(), "/wd/hub");
    }

    @Test
    public void cloudConfigTestWithURL() {
        CloudConfig cloudConfig = CloudConfig.configBuilder()
                .url("http://sjajs:hskahs@hub.browserstack.com/wd/hub")
                .build();

        URL url = RemoteUrlBuilder.build(cloudConfig);
        Assert.assertEquals(url.getHost(), "hub.browserstack.com");
        Assert.assertEquals(url.getProtocol(), "http");
        Assert.assertEquals(url.getPath(), "/wd/hub");
    }
}

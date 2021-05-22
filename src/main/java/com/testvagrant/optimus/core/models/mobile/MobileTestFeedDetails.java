package com.testvagrant.optimus.core.models.mobile;

import com.testvagrant.optimus.core.models.OptimusSupportedPlatforms;
import com.testvagrant.optimus.core.models.web.SiteConfig;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter @Builder
public class MobileTestFeedDetails {
    private String app;

    @Builder.Default
    private String platform = OptimusSupportedPlatforms.ANDROID.name();

    @Builder.Default
    private Map<String, Object> desiredCapabilities = new HashMap<>();
}

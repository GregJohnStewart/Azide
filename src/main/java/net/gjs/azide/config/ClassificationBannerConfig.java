package net.gjs.azide.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "classificationBanner", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface ClassificationBannerConfig {
    String classification();
    String color();
}

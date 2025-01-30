package mil.army.dcgs.azide.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;

@StaticInitSafe
@ConfigMapping(prefix = "classificationBanner", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface ClassificationBannerConfig {
    String classification();
    String color();
}

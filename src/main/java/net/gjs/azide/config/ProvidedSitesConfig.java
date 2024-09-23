package net.gjs.azide.config;

import io.smallrye.config.ConfigMapping;

import java.net.URL;
import java.util.List;

@ConfigMapping(prefix = "providedSites", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface ProvidedSitesConfig {

    List<Site> sites();

    interface Site {
        String title();
        String description();
        URL url();

        //TODO:: image
    }
}

package mil.army.dcgs.azide.config;

import io.smallrye.config.ConfigMapping;

import java.net.URI;
import java.util.List;

@ConfigMapping(prefix = "providedSites", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface ProvidedSitesConfig {

    List<Site> sites();

    interface Site {
        String title();
        String description();
        URI uri();

        //TODO:: image
    }
}

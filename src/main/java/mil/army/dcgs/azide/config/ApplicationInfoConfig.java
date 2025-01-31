package mil.army.dcgs.azide.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@ConfigMapping(prefix = "applicationInfo", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface ApplicationInfoConfig {

    List<Application> applications();

    interface Application {

        String name();

        String reference();

        Optional<String> description();

        URI location();

        Optional<URI> image();

        @WithDefault("false")
        boolean showInAppBar();

        @WithDefault("false")
        boolean defaultApp();

        @WithDefault("false")
        boolean splashApp();

        Optional<URI> splashEndpoint();
    }
}

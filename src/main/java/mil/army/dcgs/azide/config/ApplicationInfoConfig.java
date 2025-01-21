package mil.army.dcgs.azide.config;

import io.smallrye.config.ConfigMapping;

import java.util.List;
import java.util.Optional;

@ConfigMapping(prefix = "applicationInfo", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface ApplicationInfoConfig {

    List<Application> applications();

    interface Application {
        String name();
        Optional<String> description();
        String location();
        String image();
        boolean showInAppBar();
    }
}

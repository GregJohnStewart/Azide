package mil.army.dcgs.azide.config;

import io.smallrye.config.ConfigMapping;

import java.util.List;

@ConfigMapping(prefix = "applicationInfo", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface ApplicationInfoConfig {

    List<Application> applications();

    interface Application {
        String name();
        String location();
        String image();
    }
}

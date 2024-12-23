package net.gjs.azide.config;

import io.smallrye.config.ConfigMapping;

import java.util.List;

@ConfigMapping(prefix = "providedMessages", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface ProvidedMessagesConfig {

    List<Message> messages();

    interface Message {
        String title();
        String content();
        String date();
        int priority();
        

        //TODO:: image
    }
}

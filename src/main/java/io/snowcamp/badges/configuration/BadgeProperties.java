package io.snowcamp.badges.configuration;

import io.snowcamp.badges.attendees.Attendee;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;

/**
 * @author ctranxuan
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "badge")
public class BadgeProperties {
    private EnumMap<Attendee.Type, String> colors;

    public EnumMap<Attendee.Type, String> getColors() {
        return colors;
    }

    public void setColors(EnumMap<Attendee.Type, String> aColors) {
        colors = aColors;
    }


}

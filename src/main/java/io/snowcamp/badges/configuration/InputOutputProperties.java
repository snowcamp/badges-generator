package io.snowcamp.badges.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ctranxuan
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class InputOutputProperties {
    private String inputFile;
    private String outputDirectory;

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String aInputFile) {
        inputFile = aInputFile;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String aOutputDirectory) {
        outputDirectory = aOutputDirectory;
    }
}

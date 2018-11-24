package com.studysoft.smartjournal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Smartjournal
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

}

package moe.pine.est.config;

import moe.pine.est.properties.SlackProperties;
import moe.pine.est.slack.Slack;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableConfigurationProperties(SlackProperties.class)
public class SlackConfig {
    @Bean
    public Slack slack(final RestTemplateBuilder restTemplateBuilder) {
        return new Slack(restTemplateBuilder);
    }

    @Bean
    public ExecutorService slackExecutorService() {
        return Executors.newSingleThreadExecutor();
    }
}

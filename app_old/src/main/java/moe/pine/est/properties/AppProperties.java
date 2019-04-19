package moe.pine.est.properties;

import lombok.Data;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Data
public class AppProperties {
    @Inject
    @ConfigurationValue("app.site-url")
    private String siteUrl;
}

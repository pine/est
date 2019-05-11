package moe.pine.est.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("app")
public class AppProperties {
    private String siteUrl;
    private String siteTitle;
    private String username;
    private String password;
}

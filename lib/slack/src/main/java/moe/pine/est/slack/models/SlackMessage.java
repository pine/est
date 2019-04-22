package moe.pine.est.slack.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackMessage {
    private String token;
    private String channel;
    private String text;
    private String iconUrl;
}

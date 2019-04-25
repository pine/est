package moe.pine.est.log.models;

import lombok.Value;

@Value
public class MessageLogKey {
    private final String dt;
    private final String hash;
    private final long timeout;
}

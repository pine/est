package moe.pine.est.log.models;

import lombok.Value;
import org.springframework.lang.Nullable;

@Value
public class MessageLogId {
    private final String dt;

    @Nullable
    private final String hash;
}

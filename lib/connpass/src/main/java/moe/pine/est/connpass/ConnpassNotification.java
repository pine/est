package moe.pine.est.connpass;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnpassNotification {
    private final String groupId;
    private final String notificationGroupId;
}

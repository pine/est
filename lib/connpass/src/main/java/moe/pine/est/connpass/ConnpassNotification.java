package moe.pine.est.connpass;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConnpassNotification {
    private final String groupId;
    private final List<String> notificationGroupIds;
}

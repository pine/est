package moe.pine.est.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moe.pine.est.log.models.MessageLog;
import moe.pine.est.log.models.MessageLogId;
import moe.pine.est.log.models.NotifyRequestLog;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Log {
    private MessageLogId messageLogId;
    private MessageLog messageLog;
    private List<NotifyRequestLog> notifyRequestLogs;
}

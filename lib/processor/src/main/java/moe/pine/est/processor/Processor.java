package moe.pine.est.processor;

import moe.pine.est.email.models.EmailMessage;

import java.util.List;

public interface Processor {
    List<NotifyRequest> execute(EmailMessage emailMessage);
}

package moe.pine.est.services;

import lombok.extern.jbosslog.JBossLog;
import moe.pine.est.mailgun.Mailgun;
import moe.pine.est.mailgun.models.Message;
import moe.pine.est.mailgun.models.MessageRequest;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@JBossLog
public class MailgunService {
    @Inject
    private Mailgun mailgun;

    public Message receive(
        @Nonnull final MessageRequest messageRequest
    ) {
        final Message message = mailgun.receive(messageRequest);
        mailgun.verify(message);

        log.infov(
            "Message received :: from=\"{0}\", subject=\"{1}\"",
            message.getFrom(), message.getSubject());

        return message;
    }
}

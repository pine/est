package moe.pine.est.mailgun;

import moe.pine.est.mailgun.models.Message;
import moe.pine.est.mailgun.models.MessageHeaders;
import moe.pine.est.mailgun.models.MessageRequest;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;

import static com.google.common.base.Preconditions.checkNotNull;

@ApplicationScoped
public class Mailgun {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Message receive(
        @Nonnull final MessageRequest messageRequest
    ) {
        checkNotNull(messageRequest);

        return Message.builder()
            .recipient(messageRequest.getRecipient())
            .sender(messageRequest.getSender())
            .from(messageRequest.getFrom())
            .subject(messageRequest.getSubject())
            .bodyPlain(messageRequest.getBodyPlain())
            .strippedText(messageRequest.getStrippedText())
            .strippedSignature(messageRequest.getStrippedSignature())
            .bodyHtml(messageRequest.getBodyHtml())
            .strippedHtml(messageRequest.getStrippedHtml())
            .timestamp(messageRequest.getTimestamp())
            .token(messageRequest.getToken())
            .signature(messageRequest.getSignature())
            .headers(new MessageHeaders())
            .build();
    }

    public void verify(
        final Message message
    ) {
    }
}

package moe.pine.est.mailgun;

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
            .headers(new MessageHeaders())
            .build();
    }

    public void verify(
        final Message message,
        final String timestamp,
        final String token,
        final String signature
    ) {
    }
}

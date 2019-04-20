package moe.pine.est.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.email.EmailVerifier;
import moe.pine.est.email.models.EmailMessage;
import moe.pine.est.models.MessageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final EmailVerifier emailVerifier;

    @Nonnull
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public EmailMessage newMessage(
        @Nonnull final MessageRequest messageRequest
    ) {
        checkNotNull(messageRequest);

        return EmailMessage.builder()
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
            .build();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void verify(
        @Nonnull final EmailMessage emailMessage
    ) {
        checkNotNull(emailMessage);

        emailVerifier.verify(emailMessage);
    }
}

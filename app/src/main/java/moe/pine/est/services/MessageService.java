package moe.pine.est.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.mailgun.models.Message;
import moe.pine.est.models.MessageRequest;
import moe.pine.est.properties.MailgunProperties;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_256;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MailgunProperties mailgunProperties;

    @Nonnull
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Message create(
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
            .build();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void verify(@Nonnull final Message message) {
        checkNotNull(message);

        final String apiKey = mailgunProperties.getApiKey();
        final String timestamp = String.valueOf(message.getTimestamp());
        final String token = message.getToken();

        final HmacUtils hmacUtils = new HmacUtils(HMAC_SHA_256, apiKey);
        final String signature = hmacUtils.hmacHex(timestamp + token);

        if (!signature.equals(message.getSignature())) {
            log.debug(
                "Verification failed :: " +
                    "from={}, subject={}, timestamp={}, expected-signature={}, actual-signature={}",
                message.getFrom(),
                message.getSubject(),
                timestamp,
                message.getSignature(),
                signature);

            throw new InvalidSignatureException();
        }
    }
}

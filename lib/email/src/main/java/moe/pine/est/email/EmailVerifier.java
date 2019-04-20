package moe.pine.est.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.email.models.EmailMessage;
import org.apache.commons.codec.digest.HmacUtils;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_256;

@RequiredArgsConstructor
@Slf4j
public class EmailVerifier {
    private final String apiKey;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void verify(@Nonnull final EmailMessage message) {
        checkNotNull(message);

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

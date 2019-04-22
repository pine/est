package moe.pine.est.slack;

import com.google.common.annotations.VisibleForTesting;

@SuppressWarnings("WeakerAccess")
public class SlackException extends RuntimeException {
    @VisibleForTesting
    SlackException(String message) {
        super(message);
    }
}
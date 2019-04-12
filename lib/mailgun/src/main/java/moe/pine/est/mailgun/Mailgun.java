package moe.pine.est.mailgun;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Mailgun {
    public Message receive(
            final String subject
    ) {
        return Message.builder()
                .subject(subject)
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

package moe.pine.est.models;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Nonnull;
import java.util.Optional;

@Component
public class MessageRequestArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MessageRequest.class);
    }

    @Override
    public Object resolveArgument(
        @Nonnull MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        final long timestamp =
            Optional.ofNullable(webRequest.getParameter("timestamp"))
                .filter(StringUtils::isNotEmpty)
                .map(Long::valueOf)
                .orElseThrow(() -> new IllegalArgumentException("`timestamp` is required"));
        final String token =
            Optional.ofNullable(webRequest.getParameter("token"))
                .filter(StringUtils::isNotEmpty)
                .orElseThrow(() -> new IllegalArgumentException("`token` is required"));
        final String signature =
            Optional.ofNullable(webRequest.getParameter("signature"))
                .filter(StringUtils::isNotEmpty)
                .orElseThrow(() -> new IllegalArgumentException("`signature` is required"));

        return MessageRequest.builder()
            .recipient(webRequest.getParameter("recipient"))
            .sender(webRequest.getParameter("sender"))
            .from(webRequest.getParameter("from"))
            .subject(webRequest.getParameter("subject"))
            .bodyPlain(webRequest.getParameter("text-plain"))
            .strippedText(webRequest.getParameter("stripped-text"))
            .strippedSignature(webRequest.getParameter("stripped-signature"))
            .bodyHtml(webRequest.getParameter("text-html"))
            .strippedHtml(webRequest.getParameter("stripped-html"))
            .timestamp(timestamp)
            .token(token)
            .signature(signature)
            .build();
    }
}

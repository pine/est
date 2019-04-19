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
        final Long timestamp =
            Optional.ofNullable(webRequest.getParameter("timestamp"))
                .filter(StringUtils::isNotEmpty)
                .map(Long::valueOf)
                .orElse(null);

        return MessageRequest.builder()
            .recipient(webRequest.getParameter("recipient"))
            .sender(webRequest.getParameter("sender"))
            .from(webRequest.getParameter("from"))
            .bodyPlain(webRequest.getParameter("body-plain"))
            .strippedText(webRequest.getParameter("stripped-text"))
            .strippedSignature(webRequest.getParameter("stripped-signature"))
            .bodyHtml(webRequest.getParameter("body-html"))
            .strippedHtml(webRequest.getParameter("stripped-html"))
            .timestamp(timestamp)
            .token(webRequest.getParameter("token"))
            .signature(webRequest.getParameter("signature"))
            .build();
    }
}

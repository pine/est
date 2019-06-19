package moe.pine.est.processor.impl.connpass.staff;

import com.fasterxml.jackson.databind.ObjectMapper;
import moe.pine.est.email.models.EmailMessage;
import moe.pine.est.prosessor.impl.connpass.staff.MessageParser;
import moe.pine.est.prosessor.impl.connpass.staff.ParserResult;
import org.junit.Before;
import org.junit.Test;

import static moe.pine.est.prosessor.impl.connpass.staff.ParserResult.Action.JOINED;
import static moe.pine.est.prosessor.impl.connpass.staff.ParserResult.Action.LEFT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MessageParserTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private MessageParser parser;

    @Before
    public void setUp() {
        parser = new MessageParser(OBJECT_MAPPER);
    }

    @Test
    public void parseTest_null() {
        final var message = EmailMessage.builder().build();
        assertNull(parser.parse(message, "gotandajs"));
    }

    @Test
    public void parseTest_joined() {
        final var groupId = "gotandajs";
        final var message = EmailMessage.builder()
            .bodyHtml(
                "<a href=\"https://connpass.com/user/example/?utm_campaign=event_participate_to_owner&amp;utm_source=notifications&amp;utm_medium=email&amp;utm_content=user_link\" style=\"color:#000\" target=\"_blank\"><strong>Taro Suzuki</strong></a>さんが<br>\n" +
                    "<a href=\"https://gotandajs.connpass.com/event/127779/?utm_campaign=event_participate_to_owner&amp;utm_source=notifications&amp;utm_medium=email&amp;utm_content=title_link\" style=\"color:#000\" target=\"_blank\"><strong>Gotanda.js #11 in MobileFactory</strong></a>に参加登録しました。\n")
            .build();

        final ParserResult expected =
            ParserResult.builder()
                .name("Taro Suzuki")
                .event("Gotanda.js #11 in MobileFactory")
                .action(JOINED)
                .build();
        final ParserResult actual = parser.parse(message, groupId);

        assertEquals(expected, actual);
    }

    @Test
    public void parseTest_left() {
        final String groupId = "gotandajs";
        final var message = EmailMessage.builder()
            .bodyHtml("<p style=\"font-size:14px; color:#000; border-bottom:1px solid #ccc;\">\n" +
                "<a href=\"https://connpass.com/user/example/?utm_campaign=event_participate_cancel_to_owner&utm_source=notifications&utm_medium=email&utm_content=user_link\" target=\"_blank\" style=\"color:#000;\"><strong>Taro Suzuki</strong></a>さんが<br />\n" +
                "「<a href=\"https://gotandajs.connpass.com/event/132152/?utm_campaign=event_participate_cancel_to_owner&utm_source=notifications&utm_medium=email&utm_content=title_link\" target=\"_blank\" style=\"color:#000;\">Gotanda.js #12 in ZEALS</a>」への参加をキャンセルしました。</p>")
            .build();

        final ParserResult expected =
            ParserResult.builder()
                .name("Taro Suzuki")
                .event("Gotanda.js #12 in ZEALS")
                .action(LEFT)
                .build();
        final ParserResult actual = parser.parse(message, groupId);

        assertEquals(expected, actual);
    }
}

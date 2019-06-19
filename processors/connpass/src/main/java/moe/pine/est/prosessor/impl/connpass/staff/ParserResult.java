package moe.pine.est.prosessor.impl.connpass.staff;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParserResult {
    private String name;
    private String event;
    private Action action;

    public enum Action {
        JOINED, LEFT
    }
}

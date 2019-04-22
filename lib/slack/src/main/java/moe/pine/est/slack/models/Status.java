package moe.pine.est.slack.models;

import lombok.Data;

@Data
public class Status {
    private boolean ok;
    private String error;
}
package com.example.myapplication.model;

import java.util.List;

public class AuditRequestGroup {
    private final String requestId;
    private final String actionSummary;
    private final String tableSummary;
    private final String timeSummary;
    private final String actorSummary;
    private final String changeSummary;
    private final List<AuditItem> items;

    public AuditRequestGroup(
            String requestId,
            String actionSummary,
            String tableSummary,
            String timeSummary,
            String actorSummary,
            String changeSummary,
            List<AuditItem> items
    ) {
        this.requestId = requestId;
        this.actionSummary = actionSummary;
        this.tableSummary = tableSummary;
        this.timeSummary = timeSummary;
        this.actorSummary = actorSummary;
        this.changeSummary = changeSummary;
        this.items = items;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getActionSummary() {
        return actionSummary;
    }

    public String getTableSummary() {
        return tableSummary;
    }

    public String getTimeSummary() {
        return timeSummary;
    }

    public String getActorSummary() {
        return actorSummary;
    }

    public String getChangeSummary() {
        return changeSummary;
    }

    public List<AuditItem> getItems() {
        return items;
    }
}

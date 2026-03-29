package com.example.myapplication.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AuditItem {
    private final long auditId;
    private final String eventTime;
    private final String actor;
    private final String actorUsername;
    private final String requestId;
    private final String action;
    private final String tableName;
    private final String pk;
    private final String oldData;
    private final String newData;

    public AuditItem(
            long auditId,
            String eventTime,
            String actor,
            String actorUsername,
            String requestId,
            String action,
            String tableName,
            String pk,
            String oldData,
            String newData
    ) {
        this.auditId = auditId;
        this.eventTime = eventTime;
        this.actor = actor;
        this.actorUsername = actorUsername;
        this.requestId = requestId;
        this.action = action;
        this.tableName = tableName;
        this.pk = pk;
        this.oldData = oldData;
        this.newData = newData;
    }

    public static AuditItem fromJson(JSONObject obj) {
        return new AuditItem(
                obj.optLong("auditId", 0L),
                obj.optString("eventTime", "-"),
                obj.optString("actor", "-"),
                emptyToDash(obj.optString("actorUsername", "")),
                emptyToDash(obj.optString("requestId", "")),
                obj.optString("action", "-"),
                obj.optString("tableName", "-"),
                toDisplayString(obj.opt("pk")),
                toDisplayString(obj.opt("oldData")),
                toDisplayString(obj.opt("newData"))
        );
    }

    private static String emptyToDash(String value) {
        return value == null || value.trim().isEmpty() ? "-" : value;
    }

    private static String toDisplayString(Object value) {
        if (value == null || value == JSONObject.NULL) {
            return "-";
        }

        if (value instanceof JSONObject) {
            try {
                return ((JSONObject) value).toString(2);
            } catch (JSONException ignored) {
                return ((JSONObject) value).toString();
            }
        }

        if (value instanceof JSONArray) {
            try {
                return ((JSONArray) value).toString(2);
            } catch (JSONException ignored) {
                return ((JSONArray) value).toString();
            }
        }

        String text = String.valueOf(value);
        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return "-";
        }

        if ((trimmed.startsWith("{") && trimmed.endsWith("}"))
                || (trimmed.startsWith("[") && trimmed.endsWith("]"))) {
            try {
                if (trimmed.startsWith("{")) {
                    return new JSONObject(trimmed).toString(2);
                }
                return new JSONArray(trimmed).toString(2);
            } catch (JSONException ignored) {
                return trimmed;
            }
        }

        return trimmed;
    }

    public long getAuditId() {
        return auditId;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getActor() {
        return actor;
    }

    public String getActorUsername() {
        return actorUsername;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getAction() {
        return action;
    }

    public String getTableName() {
        return tableName;
    }

    public String getPk() {
        return pk;
    }

    public String getOldData() {
        return oldData;
    }

    public String getNewData() {
        return newData;
    }
}

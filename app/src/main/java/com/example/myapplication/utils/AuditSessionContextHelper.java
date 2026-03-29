package com.example.myapplication.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public final class AuditSessionContextHelper {

    private AuditSessionContextHelper() {
    }

    public static void apply(Connection connection, String actorId, String actorName, String requestId) throws SQLException {
        String normalizedRequestId = (requestId == null || requestId.trim().isEmpty())
                ? UUID.randomUUID().toString()
                : requestId.trim();

        setValue(connection, "request_id", normalizedRequestId);

        if (actorId != null && !actorId.trim().isEmpty()) {
            setValue(connection, "actor_id", actorId.trim());
        }

        if (actorName != null && !actorName.trim().isEmpty()) {
            setValue(connection, "actor", actorName.trim());
        }
    }

    private static void setValue(Connection connection, String key, String value) throws SQLException {
        if (value == null || value.trim().isEmpty()) {
            return;
        }

        String query = "EXEC sys.sp_set_session_context @key = ?, @value = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, key);
            statement.setString(2, value);
            statement.execute();
        }
    }
}

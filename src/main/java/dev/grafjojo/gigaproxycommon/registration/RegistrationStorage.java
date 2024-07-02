package dev.grafjojo.gigaproxycommon.registration;

import dev.grafjojo.gigacraftcore.database.postgres.PostgresDatabase;
import dev.grafjojo.gigacraftcore.database.postgres.PostgresStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class RegistrationStorage extends PostgresStorage {
    public RegistrationStorage(PostgresDatabase database) {
        super("gigacraft_registrations", database);
    }

    public boolean isProcessed(UUID uuid) {
        try (Connection connection = getDatabase().getConnection()) {

            PreparedStatement statement = connection.prepareStatement("SELECT processed FROM " + getTable() + " WHERE user_uuid = ?;");
            statement.setString(1, uuid.toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) return resultSet.getBoolean("processed");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void initialize() {

    }
}

package dev.grafjojo.gigaproxycommon.whitelist;

import dev.grafjojo.gigacraftcore.database.postgres.PostgresDatabase;
import dev.grafjojo.gigacraftcore.database.postgres.PostgresStorage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class WhitelistStorage extends PostgresStorage {
    public WhitelistStorage(PostgresDatabase database) {
        super("gigacraft_whitelist", database);
    }

    public void unWhitelist(UUID uuid) {
        try (Connection connection = getDatabase().getConnection()) {

            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + getTable() + " WHERE uuid = ?;");
            statement.setString(1, uuid.toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void whitelist(UUID uuid) {
        try (Connection connection = getDatabase().getConnection()) {

            PreparedStatement statement = connection.prepareStatement("INSERT INTO " + getTable() + " (uuid,added_at) VALUES (?, ?);");
            statement.setString(1, uuid.toString());
            statement.setDate(2, new Date(System.currentTimeMillis()));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isWhitelisted(UUID uuid) {
        try (Connection connection = getDatabase().getConnection()) {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + getTable() + " WHERE uuid = ?;");
            statement.setString(1, uuid.toString());

            if (statement.executeQuery().next()) {
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void initialize() {
        try (Connection connection = getDatabase().getConnection()) {

            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS " + getTable() + " (uuid VARCHAR(36) PRIMARY KEY, added_at DATE NOT NULL);");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

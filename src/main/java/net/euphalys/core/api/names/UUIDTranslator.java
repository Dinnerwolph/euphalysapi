package net.euphalys.core.api.names;

import net.euphalys.api.names.IUUIDTranslator;
import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.plugin.IEuphalysPlugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 * @author Dinnerwolph
 */

public final class UUIDTranslator implements IUUIDTranslator {
    private final Pattern UUID_PATTERN = Pattern.compile("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}");
    private final Pattern MOJANGIAN_UUID_PATTERN = Pattern.compile("[a-fA-F0-9]{32}");
    private final IEuphalysPlugin plugin;
    private final DataSource dataSource;

    public UUIDTranslator(IEuphalysPlugin plugin) {
        this.plugin = plugin;
        this.dataSource = plugin.getDatabaseManager().getDataSource();
    }

    @Override
    public UUID getUUID(String name, boolean allowMojangCheck) {
        // If the player is online, give them their UUID.
        // Remember, local data > remote data.
        IEuphalysPlayer player = plugin.getPlayer(name);
        if (player != null && player.isOnline())
            return player.getUUID();

        // Check if we can exit early
        if (UUID_PATTERN.matcher(name).find()) {
            return UUID.fromString(name);
        }

        if (MOJANGIAN_UUID_PATTERN.matcher(name).find()) {
            // Reconstruct the UUID
            return UUIDFetcher.getUUID(name);
        }


        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `uuid` FROM `user` WHERE `playerName`=?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet != null) {
                if (resultSet.next()) {
                    String stored = resultSet.getString("uuid");
                    if (stored != null) {
                        connection.close();
                        return UUID.fromString(stored);
                    }
                }
            }
            connection.close();

            // That didn't work. Let's ask Mojang.
            if (!allowMojangCheck) {
                return null;
            }

            Map<String, UUID> uuidMap1;
            try {
                uuidMap1 = new UUIDFetcher(Collections.singletonList(name)).call();
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Unable to fetch UUID from Mojang for " + name, e);
                return null;
            }
            for (Map.Entry<String, UUID> entry : uuidMap1.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(name)) {
                    return entry.getValue();
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Unable to fetch UUID for " + name, e);
        }

        return null; // Nope, game over!
    }

    @Override
    public String getName(UUID uuid, boolean allowMojangCheck) {
        IEuphalysPlayer player = plugin.getPlayer(uuid);
        if (player.isOnline())
            return player.getName();


        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `playerName` FROM `user` WHERE `uuid`=?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String stored = resultSet.getString("playerName");
            connection.close();
            if (stored != null) {
                return stored;
            }

            if (!allowMojangCheck) {
                return null;
            }

            // That didn't work. Let's ask Mojang. This call may fail, because Mojang is insane.
            String name;
            try {
                name = NameFetcher.nameHistoryFromUuid(uuid).get(0);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Unable to fetch name from Mojang for " + uuid);
                return null;
            }

            if (name != null) {
                return name;
            }
            return null;
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Unable to fetch name for " + uuid, e);
            return null;
        }
    }

    @Override
    public UUID getUUID(int azoid) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `uuid` FROM `user` WHERE `id`=?");
            statement.setInt(1, azoid);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            UUID uuid = UUID.fromString(resultSet.getString("uuid"));
            connection.close();
            return uuid;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Deprecated
    public String getName(int azoid) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `playerName` FROM `user` WHERE `id`=?");
            statement.setInt(1, azoid);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String name = resultSet.getString("playerName");
            connection.close();

            return name;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}

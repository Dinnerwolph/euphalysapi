package net.euphalys.core.api.player;

import net.euphalys.api.player.IGroup;
import net.euphalys.api.player.IPlayerManager;
import net.euphalys.api.plugin.IEuphalysPlugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class PlayerManager implements IPlayerManager {

    private final DataSource dataSource;
    private final IEuphalysPlugin api;

    public PlayerManager(IEuphalysPlugin api) {
        this.api = api;
        this.dataSource = api.getDatabaseManager().getDataSource();
    }

    public boolean createUser(UUID uuid, String name) {
        if (exist(uuid))
            return false;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `user`(`uuid`, `playerName`) VALUES (?,?)");
            statement.setString(1, uuid.toString());
            statement.setString(2, name);
            statement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean exist(UUID uuid) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `id` FROM `user` WHERE `uuid`=?");
            statement.setString(1, uuid.toString());
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                connection.close();
                return true;
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void loadAllGroup() {
        try {
            Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.prepareStatement("SELECT * FROM `group` WHERE 1").executeQuery();
            while (resultSet.next()) {
                api.addGroup(new Group(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("prefix"), resultSet.getString("suffix"), resultSet.getString("chat"), resultSet.getInt("ladder"), getPermissions(resultSet.getInt("id")), resultSet.getString("score")));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IGroup getGroup(UUID uuid) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `groupId` FROM `user` WHERE `uuid`=?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int groupid = resultSet.getInt("groupId");
                connection.close();
                return api.getGroup(groupid);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getAzonaryaId(UUID uuid) {
        try {
            if (uuid == null)
                return -1;
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `id` FROM `user` WHERE `uuid`=?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet == null) {
                connection.close();
                return -1;
            }
            resultSet.next();
            int id = resultSet.getInt("id");
            connection.close();
            return id;
        } catch (SQLException e) {
        }
        return -1;
    }

    @Override
    public String getLastAddress(UUID uuid) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `ip` FROM `user` WHERE `uuid`=?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String ip = resultSet.getString("ip");
            connection.close();
            return ip;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setLastConnection(UUID uuid, long timestamp) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE `user` SET `lastConnected`=? WHERE `uuid`=?");
            statement.setLong(1, timestamp);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setLastAddress(UUID uuid, String ip) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE `user` SET `ip`=? WHERE `uuid`=?");
            statement.setString(1, ip);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getPermissions(int euphaId) {
        List<String> permissions = new ArrayList();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `permission` FROM `permissions` WHERE `id`=?");
            statement.setInt(1, euphaId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                permissions.add(resultSet.getString("permission"));
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return permissions;
    }

    @Override
    public boolean isVanished(int euphaId) {
        int returnint = 0;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `vanished` FROM `user` WHERE `id`=?");
            statement.setInt(1, euphaId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            returnint = resultSet.getInt("vanished");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (returnint == 1)
            return true;
        return false;
    }

    @Override
    public void setVanish(int euphaId, int vanish) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE `user` SET `vanished`=? WHERE `id`=?");
            statement.setInt(1, vanish);
            statement.setInt(2, euphaId);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getTimePlayed(int euphaId) {
        int returnint = 0;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `time_played` FROM `user` WHERE `id`=?");
            statement.setInt(1, euphaId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            returnint = resultSet.getInt("time_played");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnint;
    }

    @Override
    public void setTimePlayed(int euphaId, int timeplayed) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE `user` SET `time_played`=? WHERE `id`=?");
            statement.setInt(2, euphaId);
            statement.setInt(1, timeplayed);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UUID getUUID(String name) {
        UUID returnUUID = null;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `uuid` FROM `user` WHERE `PlayerName`=?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            returnUUID = UUID.fromString(resultSet.getString("uuid"));
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnUUID;
    }

    @Override
    public String getLastName(UUID uuid) {
        String returnName = null;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `PlayerName` FROM `user` WHERE `uuid`=?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            returnName = resultSet.getString("PlayerName");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnName;
    }

    @Override
    public boolean isOnline(int id) {
        boolean returnboolean = false;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `lastConnected` FROM `user` WHERE `id`=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            float value = resultSet.getFloat("lastConnected");
            if (value == 0)
                returnboolean = true;
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnboolean;
    }

    @Override
    public String getServer(int id) {
        String server = "";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `server` FROM `user` WHERE `id`=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            server = resultSet.getString("server");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return server;
    }

    @Override
    public void setServer(int id, String server) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE `user` SET `server`=? WHERE `id`=?");
            statement.setString(1, server);
            statement.setInt(2, id);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setNickName(int id, String nickName) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE `user` SET `nickName`=? WHERE `id`=?");
            statement.setString(1, nickName);
            statement.setInt(2, id);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNickName(int id) {
        String nickname = "";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `nickName` FROM `user` WHERE `id`=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            nickname = resultSet.getString("nickName");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nickname;
    }
}

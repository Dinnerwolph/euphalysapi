package net.euphalys.core.api.friends;


import net.euphalys.api.friends.IFriendsManager;
import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.plugin.IEuphalysPlugin;
import net.euphalys.core.api.player.OfflineEuphalysPlayer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Dinnerwolph
 */

public class FriendsManager implements IFriendsManager {

    private HashMap<UUID, FriendPlayer> cache;
    private final IEuphalysPlugin plugin;
    private final DataSource dataSource;

    public FriendsManager(IEuphalysPlugin plugin) {
        this.cache = new HashMap();
        this.plugin = plugin;
        this.dataSource = plugin.getDatabaseManager().getDataSource();
    }

    public void loadPlayer(UUID player) {
        FriendPlayer friendPlayer = new FriendPlayer(player);
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `p2i`FROM `friend` WHERE `p1i`=? AND `status`=?");
            statement.setInt(1, plugin.getPlayer(player).getEuphalysId());
            statement.setInt(2, 2);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                friendPlayer.addFriend(plugin.getUUUIDTranslator().getUUID(resultSet.getInt("p2i")));
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cache.put(player, friendPlayer);
    }

    //TODO deco
    public void unloadPlayer(UUID player) {
        cache.remove(player);
    }

    public boolean areFriends(UUID p1, UUID p2) {
        if (cache.containsKey(p1))
            return cache.get(p1).areFriend(p2);
        return false;
    }

    public List<String> namesFriendsList(UUID asking) {
        if (cache.containsKey(asking)) {
            List<String> names = new ArrayList();
            FriendPlayer friendPlayer = cache.get(asking);
            friendPlayer.getFriends().forEach(uuid -> {
                names.add(plugin.getUUUIDTranslator().getName(uuid));
            });
            return names;
        }
        return null;
    }

    public List<UUID> uuidFriendsList(UUID asking) {
        if (cache.containsKey(asking))
            return cache.get(asking).getFriends();
        return null;
    }

    public Map<UUID, String> associatedFriendsList(UUID asking) {
        if (cache.containsKey(asking)) {
            HashMap<UUID, String> names = new HashMap<>();
            FriendPlayer friendPlayer = cache.get(asking);

            friendPlayer.getFriends().forEach(uuid -> {
                String name;
                if ((name = plugin.getUUUIDTranslator().getName(uuid)) != null)
                    names.put(uuid, name);
            });

            return names;
        }
        return null;
    }

    public FriendPlayer getFriendPlayer(UUID asking) {
        return cache.get(asking);
    }

    /**
     * list des demande d'amis reçu
     *
     * @param asking Player
     * @return
     */

    public List<String> requests(IEuphalysPlayer asking) {
        List<String> names = new ArrayList();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `p2i` FROM `friend` WHERE `status`= ? AND `p1i`= ?");
            statement.setInt(1, 1);
            statement.setInt(2, asking.getEuphalysId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                names.add(plugin.getUUUIDTranslator().getName(resultSet.getInt("p2i")));
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    /**
     * liste de demande d'amis envoyé
     *
     * @param asking Player
     * @return
     */

    public List<String> sentRequests(IEuphalysPlayer asking) {
        List<String> names = new ArrayList();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `p2i` FROM `friend` WHERE `status`= ? AND `p1i`=?");
            statement.setInt(1, 0);
            statement.setInt(2, asking.getEuphalysId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
                names.add(plugin.getUUUIDTranslator().getName(resultSet.getInt("p2i")));
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    public boolean removeFriend(IEuphalysPlayer asking, IEuphalysPlayer target) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE `friend` SET `status`=? WHERE `p1i`=? AND `p2i`=?");
            statement.setInt(1, 3);
            statement.setInt(2, asking.getEuphalysId());
            statement.setInt(3, target.getEuphalysId());
            statement.executeUpdate();
            statement.close();

            statement = connection.prepareStatement("UPDATE `friend` SET `status`=? WHERE `p1i`=? AND `p2i`=?");
            statement.setInt(1, 3);
            statement.setInt(3, asking.getEuphalysId());
            statement.setInt(2, target.getEuphalysId());
            statement.executeUpdate();
            cache.get(asking.getUUID()).removeFriend(target.getUUID());
            if (target.isOnline())
                cache.get(target.getUUID()).removeFriend(asking.getUUID());
            asking.updateFriends();
            target.updateFriends();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean request(IEuphalysPlayer asking, IEuphalysPlayer target) {
        try {
            PreparedStatement statement;
            Connection connection = dataSource.getConnection();
            if (!hasline(asking, target)) {
                statement = connection.prepareStatement("INSERT INTO `friend`(`p1i`, `p2i`, `status`) VALUES (?,?,?)");
                statement.setInt(1, asking.getEuphalysId());
                statement.setInt(2, target.getEuphalysId());
                statement.setInt(3, 0);
                statement.executeUpdate();
                statement.setInt(2, asking.getEuphalysId());
                statement.setInt(1, target.getEuphalysId());
                statement.setInt(3, 1);
                statement.executeUpdate();
                connection.close();
            } else {
                statement = connection.prepareStatement("UPDATE `friend` SET `status`=? WHERE `p1i`=? AND `p2i`=?");
                statement.setInt(1, 0);
                statement.setInt(2, asking.getEuphalysId());
                statement.setInt(3, target.getEuphalysId());
                statement.executeUpdate();
                statement.setInt(1, 1);
                statement.setInt(3, asking.getEuphalysId());
                statement.setInt(2, target.getEuphalysId());
                statement.executeUpdate();
                connection.close();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean hasline(IEuphalysPlayer asking, IEuphalysPlayer target) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `friend` WHERE `p1i`=? AND `p2i`=?");
            statement.setInt(1, asking.getEuphalysId());
            statement.setInt(2, target.getEuphalysId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                connection.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean accept(IEuphalysPlayer asking, IEuphalysPlayer target) {
        if (!hasdemand(asking, target))
            return false;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE `friend` SET `status`=? WHERE `p1i`=? AND `p2i`=?");
            statement.setInt(1, 2);
            statement.setInt(2, asking.getEuphalysId());
            statement.setInt(3, target.getEuphalysId());
            statement.executeUpdate();
            statement.setInt(3, asking.getEuphalysId());
            statement.setInt(2, target.getEuphalysId());
            statement.executeUpdate();
            cache.get(asking.getUUID()).addFriend(target.getUUID());
            if (target.isOnline())
                cache.get(target.getUUID()).addFriend(asking.getUUID());
            asking.updateFriends();
            target.updateFriends();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean hasdemand(IEuphalysPlayer asking, IEuphalysPlayer target) {
        if (!hasline(asking, target))
            return false;
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `id` FROM `friend` WHERE `p1i`=? AND `p2i`=? AND `status`=?");
            statement.setInt(1, asking.getEuphalysId());
            statement.setInt(2, target.getEuphalysId());
            statement.setInt(3, 1);
            ResultSet resultSet = statement.executeQuery();
            connection.close();
            if (resultSet != null)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean decline(IEuphalysPlayer asking, IEuphalysPlayer target) {
        if (!hasdemand(asking, target))
            try {
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE `friend` SET `status`= WHERE `p1i`=? AND `p2i`=?");
                statement.setInt(1, 3);
                statement.setInt(2, asking.getEuphalysId());
                statement.setInt(3, target.getEuphalysId());
                statement.executeUpdate();
                statement.setInt(3, asking.getEuphalysId());
                statement.setInt(2, target.getEuphalysId());
                statement.executeUpdate();
                connection.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return false;
    }
}

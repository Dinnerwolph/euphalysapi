package net.euphalys.core.api.player;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.player.IGroup;
import net.euphalys.api.player.IPlayerManager;
import net.euphalys.api.plugin.IEuphalysPlugin;
import net.euphalys.api.sanctions.ISanctions;
import net.euphalys.core.api.EuphalysApi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class EuphalysPlayer implements IEuphalysPlayer {

    private final IEuphalysPlugin api;
    private final IPlayerManager playerManager;
    private final UUID uuid;
    private final String name;
    private final int euphalysId;
    private String lastAddress;
    private IGroup group;
    private List<String> friends = new ArrayList();
    private List<ISanctions> sanctions = new ArrayList();
    private List<String> permissions = new ArrayList();
    private boolean vanished;
    private int time_played;
    private long connect;
    private String nickName;
    private IGroup realGroup;

    public EuphalysPlayer(UUID uuid, String name, IEuphalysPlugin api) {
        this.api = api;
        this.playerManager = api.getPlayerManager();
        this.uuid = uuid;
        this.name = name;
        if (!playerManager.exist(uuid))
            playerManager.createUser(uuid, name);
        this.euphalysId = playerManager.getAzonaryaId(uuid);
        playerManager.updateUserName(euphalysId, name);
        this.load();
    }

    private void load() {
        group = playerManager.getGroup(uuid);
        realGroup = playerManager.getGroup(uuid);
        this.lastAddress = playerManager.getLastAddress(uuid);
        this.sanctions = api.getSanctionsManager().getSanction(this);
        //TODO permissions solo ?
        //this.permissions.addAll(playerManager.getPermissions(euphalysId));
        this.permissions.addAll(group.getPermissions());
        this.vanished = playerManager.isVanished(euphalysId);
        this.time_played = playerManager.getTimePlayed(euphalysId);
        this.api.getFriendsManager().loadPlayer(uuid);
        this.friends = this.api.getFriendsManager().namesFriendsList(uuid);
        this.nickName = playerManager.getNickName(euphalysId);
        connect = System.currentTimeMillis();
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public Integer getEuphalysId() {
        return euphalysId;
    }

    @Override
    public IGroup getGroup() {
        return group;
    }

    @Override
    public boolean hasPermission(String permission) {
        if (permissions.contains("*"))
            return true;
        String[] perms = permission.split("\\.");
        String perm = "";
        for (int i = 0; i < perms.length; i++) {
            if (i == 0) {
                perm = perms[i];
                if (permissions.contains(perms[i] + ".*"))
                    return true;
            } else {
                if (permissions.contains(perm + "." + perms[i] + ".*"))
                    return true;
                perm = perm + "." + perms[i];
            }
            if (permissions.contains(perm))
                return true;
        }
        return false;
    }

    @Override
    public List<String> getFriends() {
        return friends;
    }

    @Override
    public String getLastAddress() {
        return lastAddress;
    }

    @Override
    public List<ISanctions> getSanctions() {
        return sanctions;
    }

    @Override
    public boolean isVanished() {
        return vanished;
    }

    @Override
    public void setTimePlayed() {
        time_played += System.currentTimeMillis() - connect;
        this.playerManager.setTimePlayed(euphalysId, time_played);
    }

    @Override
    public int getTimePlayed() {
        return time_played += System.currentTimeMillis() - connect;
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public void updateFriends() {
        this.friends = new ArrayList();
        this.friends = this.api.getFriendsManager().namesFriendsList(uuid);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getServer() {
        return EuphalysApi.getInstance().getSProperty("name");
    }

    @Override
    public void sendToServer(String server) {
        api.sendToServer(server, getUUID());
    }

    @Override
    public void setNickName(String nickName) {
        this.nickName = nickName;
        if (nickName.equals(name))
            group = realGroup;
        else
            group = EuphalysApi.getInstance().getGroup(0);
    }

    @Override
    public String getNickName() {
        if (nickName != null)
            return nickName;
        else
            return name;
    }
}

package net.euphalys.core.api.player;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.player.IGroup;
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
    private final UUID uuid;
    private final String name;
    private IGroup realGroup;
    private final int euphalysId;
    private String lastAddress;
    private IGroup group;
    private List<String> friends;
    private List<ISanctions> sanctions;
    private final List<String> permissions;
    private boolean vanished;
    private int time_played;
    private long connect;
    private String nickName;
    private int plotsId;

    public EuphalysPlayer(UUID uuid, String name, IEuphalysPlugin api) {
        this.api = api;
        this.uuid = uuid;
        this.name = name;
        this.permissions = new ArrayList<>();

        if (!api.getPlayerManager().exist(uuid))
            api.getPlayerManager().createUser(uuid, name);
        this.euphalysId = api.getPlayerManager().getAzonaryaId(uuid);
        api.getPlayerManager().updateUserName(euphalysId, name);
        this.load();
    }

    private void load() {
        group = api.getPlayerManager().getGroup(uuid);
        realGroup = api.getGroup(group.getGroupId());
        this.lastAddress = api.getPlayerManager().getLastAddress(uuid);
        this.sanctions = api.getSanctionsManager().getSanction(this);
        //TODO permissions solo ?
        //this.permissions.addAll(playerManager.getPermissions(euphalysId));
        this.permissions.addAll(group.getPermissions());
        this.vanished = api.getPlayerManager().isVanished(euphalysId);
        this.time_played = api.getPlayerManager().getTimePlayed(euphalysId);
        this.api.getFriendsManager().loadPlayer(uuid);
        this.friends = this.api.getFriendsManager().namesFriendsList(uuid);
        this.nickName = api.getPlayerManager().getNickName(euphalysId);
        connect = System.currentTimeMillis();
        this.plotsId = api.getPlayerManager().getPlotsID(euphalysId);
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
    public void setVanish(boolean vanish) {
        this.vanished = vanish;
        this.api.getPlayerManager().setVanish(getEuphalysId(), vanish ? 1 : 0);
    }

    @Override
    public void setTimePlayed() {
        time_played += System.currentTimeMillis() - connect;
        this.api.getPlayerManager().setTimePlayed(euphalysId, time_played);
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
        this.friends.clear();
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
        return nickName.isEmpty() ? name : nickName;
    }

    @Override
    public boolean hasNickName() {
        return !nickName.isEmpty();
    }

    public void setGroup(IGroup group) {
        this.group = group;
    }

    @Override
    public IGroup getRealGroup() {
        return realGroup;
    }

    @Override
    public int getPlotsId() {
        return plotsId;
    }

    @Override
    public void setPlotsId(int plotsId) {
        this.plotsId = plotsId;
        this.api.getPlayerManager().setPlotsId(getEuphalysId(), plotsId);
    }
}

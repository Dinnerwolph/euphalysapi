package net.euphalys.core.api.player;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.player.IGroup;
import net.euphalys.api.player.IPlayerManager;
import net.euphalys.api.plugin.IEuphalysPlugin;
import net.euphalys.api.sanctions.ISanctions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class OfflineEuphalysPlayer implements IEuphalysPlayer {

    private final UUID uuid;
    private final IEuphalysPlugin plugin;
    private final int epyId;
    private final String name;
    private List<String> permissions = new ArrayList<>();

    public OfflineEuphalysPlayer(UUID uuid, IEuphalysPlugin api) {
        this.uuid = uuid;
        this.plugin = api;
        name = api.getPlayerManager().getLastName(uuid);
        this.epyId = getEuphalysId();
        //TODO permissions solo ?
        //this.permissions.addAll(playerManager.getPermissions(getEuphalysId()));
        this.permissions.addAll(getGroup().getPermissions());
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public Integer getEuphalysId() {
        return plugin.getPlayerManager().getAzonaryaId(uuid);
    }

    @Override
    public IGroup getGroup() {
        return plugin.getPlayerManager().getGroup(uuid);
    }

    @Override
    public List<String> getFriends() {
        return this.plugin.getFriendsManager().namesFriendsList(uuid);
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
    public String getLastAddress() {
        return plugin.getPlayerManager().getLastAddress(uuid);
    }

    @Override
    public List<ISanctions> getSanctions() {
        return this.plugin.getSanctionsManager().getSanction(this);
    }

    @Override
    public boolean isVanished() {
        return this.plugin.getPlayerManager().isVanished(getEuphalysId());
    }

    @Override
    public void setVanish(boolean vanish) {
        this.plugin.getPlayerManager().setVanish(getEuphalysId(), vanish ? 1 : 0);
    }

    @Override
    public void setTimePlayed() {

    }

    @Override
    public int getTimePlayed() {
        return this.plugin.getPlayerManager().getTimePlayed(getEuphalysId());
    }

    @Override
    public boolean isOnline() {
        return this.plugin.getPlayerManager().isOnline(this.epyId);
    }

    @Override
    public void updateFriends() {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getServer() {
        return this.plugin.getPlayerManager().getServer(this.epyId);
    }

    @Override
    public void sendToServer(String server) {

    }

    @Override
    public void setNickName(String nickName) {
        plugin.getPlayerManager().setNickName(getEuphalysId(), nickName);
    }

    @Override
    public String getNickName() {
        return plugin.getPlayerManager().getNickName(getEuphalysId());
    }

    @Override
    public boolean hasNickName() {
        return !getNickName().isEmpty();
    }

    @Override
    public IGroup getRealGroup() {
        return getGroup();
    }
}

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

public class OfflineEuphalysPlayer implements IEuphalysPlayer {

    private final UUID uuid;
    private final IEuphalysPlugin plugin;
    private final IPlayerManager playerManager;
    private final int epyId;
    private final String name;
    private List<String> permissions = new ArrayList<>();

    public OfflineEuphalysPlayer(UUID uuid, IEuphalysPlugin api) {
        this.uuid = uuid;
        this.plugin = api;
        this.playerManager = api.getPlayerManager();
        name = playerManager.getLastName(uuid);
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
        return playerManager.getAzonaryaId(uuid);
    }

    @Override
    public IGroup getGroup() {
        return playerManager.getGroup(uuid);
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
        return playerManager.getLastAddress(uuid);
    }

    @Override
    public List<ISanctions> getSanctions() {
        return this.plugin.getSanctionsManager().getSanction(this);
    }

    @Override
    public boolean isVanished() {
        return this.plugin.getPlayerManager().isVanished(epyId);
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
        return playerManager.isOnline(this.epyId);
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
        return EuphalysApi.getInstance().getPlayerManager().getServer(this.epyId);
    }

    @Override
    public void sendToServer(String server) {

    }
}

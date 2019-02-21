package net.euphalys.core.api.player;

import net.euphalys.api.player.IGroup;

import java.util.List;

/**
 * @author Dinnerwolph
 */

public class Group implements IGroup {

    int id;
    String name;
    String displayName;
    String suffix;
    String chatFormat;
    int ladder;
    List<String> permissions;

    public Group(int id, String name, String displayName, String suffix, String chatFormat, int ladder, List<String> permissions) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.suffix = suffix;
        this.chatFormat = chatFormat;
        this.ladder = ladder;
        this.permissions = permissions;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPrefix() {
        return displayName;
    }

    @Override
    public String getSuffix() {
        return null;
    }

    @Override
    public String getChatFormat() {
        return null;
    }

    @Override
    public Integer getGroupId() {
        return id;
    }

    @Override
    public Integer getLadder() {
        return ladder;
    }

    @Override
    public List<String> getPermissions() {
        return permissions;
    }
}

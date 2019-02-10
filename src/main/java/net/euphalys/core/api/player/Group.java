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
    int ladder;
    List<String> permissions;

    public Group(int id, String name, String displayName, int ladder, List<String> permissions) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.ladder = ladder;
        this.permissions = permissions;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
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

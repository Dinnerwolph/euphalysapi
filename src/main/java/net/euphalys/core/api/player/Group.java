package net.euphalys.core.api.player;

import net.euphalys.api.player.IGroup;
import org.bukkit.scoreboard.Score;

import java.util.List;

/**
 * @author Dinnerwolph
 */

public class Group implements IGroup {

    private int id;
    private String name;
    private String prefix;
    private String suffix;
    private String chatFormat;
    private String scoreboard;
    private int ladder;
    private List<String> permissions;

    public Group(int id, String name, String prefix, String suffix, String chatFormat, int ladder, List<String> permissions, String scoreboard) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.chatFormat = chatFormat;
        this.ladder = ladder;
        this.permissions = permissions;
        this.scoreboard = scoreboard;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

    @Override
    public String getChatFormat() {
        return chatFormat;
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

    @Override
    public String getScore() {
        return scoreboard;
    }
}

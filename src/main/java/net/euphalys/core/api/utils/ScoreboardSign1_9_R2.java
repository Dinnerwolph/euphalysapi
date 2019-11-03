package net.euphalys.core.api.utils;

import net.euphalys.api.utils.IScoreboardSign;
import net.euphalys.core.api.EuphalysApi;
import net.minecraft.server.v1_9_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dinnerwolph from zyuiop
 */

public class ScoreboardSign1_9_R2 implements IScoreboardSign {
    private boolean created = false;
    private final VirtualTeam[] lines = new VirtualTeam[15];
    private final Player player;
    private final String name;
    private String objectiveName;
    private BukkitTask task;

    /**
     * Create a scoreboard sign for a given player and using a specifig objective name
     *
     * @param player        the player viewing the scoreboard sign
     * @param objectiveName the name of the scoreboard sign (displayed at the top of the scoreboard)
     */
    public ScoreboardSign1_9_R2(Player player, String objectiveName) {
        this.player = player;
        this.name = player.getName();
        this.objectiveName = objectiveName;
        task = new LastLine().runTaskTimer(EuphalysApi.getInstance(), 15 * 20, 15 * 20);
    }

    /**
     * Send the initial creation packets for this scoreboard sign. Must be called at least once.
     */
    public void create() {
        if (created)
            return;

        PlayerConnection player = getPlayer();
        player.sendPacket(createObjectivePacket(0, objectiveName));
        player.sendPacket(setObjectiveSlot());
        int i = 0;
        while (i < lines.length)
            sendLine(i++);

        created = true;
        setLine(14, "§7⋙ §bplay.euphalys.net");
    }

    /**
     * Send the packets to remove this scoreboard sign. A destroyed scoreboard sign must be recreated using {@link ScoreboardSign1_9_R2#create()} in order
     * to be used again
     */
    public void destroy() {
        if (!created)
            return;
        Bukkit.getScheduler().cancelTask(task.getTaskId());
        getPlayer().sendPacket(createObjectivePacket(1, null));
        for (VirtualTeam team : lines)
            if (team != null)
                getPlayer().sendPacket(team.removeTeam());

        created = false;
    }

    /**
     * Change the name of the objective. The name is displayed at the top of the scoreboard.
     *
     * @param name the name of the objective, max 32 char
     */
    public void setObjectiveName(String name) {
        this.objectiveName = name;
        if (created)
            getPlayer().sendPacket(createObjectivePacket(2, name));
    }

    /**
     * Change a scoreboard line and send the packets to the player. Can be called async.
     *
     * @param line  the number of the line (0 <= line < 15)
     * @param value the new value for the scoreboard line
     */
    public void setLine(int line, String value) {
        VirtualTeam team = getOrCreateTeam(line);
        String old = team.getCurrentPlayer();
        team.setValue(value);
        if (old != null && created && team.playerChanged)
            getPlayer().sendPacket(removeLine(old));


        sendLine(line);
    }

    /**
     * Remove a given scoreboard line
     *
     * @param line the line to remove
     */
    public void removeLine(int line) {
        VirtualTeam team = getOrCreateTeam(line);
        String old = team.getCurrentPlayer();

        if (old != null && created) {
            getPlayer().sendPacket(removeLine(old));
            getPlayer().sendPacket(team.removeTeam());
        }

        lines[line] = null;
    }

    /**
     * Get the current value for a line
     *
     * @param line the line
     * @return the content of the line
     */
    public String getLine(int line) {
        if (line > 14)
            return null;
        if (line < 0)
            return null;
        return getOrCreateTeam(line).getValue();
    }

    /**
     * Get the team assigned to a line
     *
     * @return the {@link VirtualTeam} used to display this line
     */
    public VirtualTeam getTeam(int line) {
        if (line > 14)
            return null;
        if (line < 0)
            return null;
        return getOrCreateTeam(line);
    }

    private PlayerConnection getPlayer() {
        return ((CraftPlayer) player).getHandle().playerConnection;
    }

    private void sendLine(int line) {
        if (line > 14)
            return;
        if (line < 0)
            return;
        if (!created)
            return;

        int score = (15 - line);
        VirtualTeam val = getOrCreateTeam(line);
        for (Packet packet : val.sendLine())
            getPlayer().sendPacket(packet);
        getPlayer().sendPacket(sendScore(val.getCurrentPlayer(), score));
        val.reset();
    }

    private VirtualTeam getOrCreateTeam(int line) {
        if (lines[line] == null)
            lines[line] = new VirtualTeam("__fakeScore" + line);

        return lines[line];
    }

    /*
        Factories
         */
    private PacketPlayOutScoreboardObjective createObjectivePacket(int mode, String displayName) {
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
        // Nom de l'objectif
        setField(packet, "a", name);

        // Mode
        // 0 : créer
        // 1 : Supprimer
        // 2 : Mettre à jour
        setField(packet, "d", mode);

        if (mode == 0 || mode == 2) {
            setField(packet, "b", displayName);
            setField(packet, "c", IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
        }

        return packet;
    }

    private PacketPlayOutScoreboardDisplayObjective setObjectiveSlot() {
        PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();
        // Slot
        setField(packet, "a", 1);
        setField(packet, "b", name);

        return packet;
    }

    private PacketPlayOutScoreboardScore sendScore(String line, int score) {
        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(line);
        setField(packet, "b", name);
        setField(packet, "c", score);
        setField(packet, "d", PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);

        return packet;
    }

    private PacketPlayOutScoreboardScore removeLine(String line) {
        return new PacketPlayOutScoreboardScore(line);
    }

    /**
     * This class is used to manage the content of a line. Advanced users can use it as they want, but they are encouraged to read and understand the
     * code before doing so. Use these methods at your own risk.
     */
    public class VirtualTeam {
        private final String name;
        private String prefix;
        private String suffix;
        private String currentPlayer;
        private String oldPlayer;

        private boolean prefixChanged, suffixChanged, playerChanged = false;
        private boolean first = true;

        private VirtualTeam(String name, String prefix, String suffix) {
            this.name = name;
            this.prefix = prefix;
            this.suffix = suffix;
        }

        private VirtualTeam(String name) {
            this(name, "", "");
        }

        public String getName() {
            return name;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            if (this.prefix == null || !this.prefix.equals(prefix))
                this.prefixChanged = true;
            this.prefix = prefix;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            if (this.suffix == null || !this.suffix.equals(prefix))
                this.suffixChanged = true;
            this.suffix = suffix;
        }

        private PacketPlayOutScoreboardTeam createPacket(int mode) {
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            setField(packet, "a", name);
            setField(packet, "i", mode);
            setField(packet, "b", "");
            setField(packet, "c", prefix);
            setField(packet, "d", suffix);
            setField(packet, "g", 0);
            setField(packet, "e", "always");
            setField(packet, "j", 0);

            return packet;
        }

        public PacketPlayOutScoreboardTeam createTeam() {
            return createPacket(0);
        }

        public PacketPlayOutScoreboardTeam updateTeam() {
            return createPacket(2);
        }

        public PacketPlayOutScoreboardTeam removeTeam() {
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            setField(packet, "a", name);
            setField(packet, "i", 1);
            first = true;
            return packet;
        }

        public void setPlayer(String name) {
            if (this.currentPlayer == null || !this.currentPlayer.equals(name))
                this.playerChanged = true;
            this.oldPlayer = this.currentPlayer;
            this.currentPlayer = name;
        }

        public Iterable<PacketPlayOutScoreboardTeam> sendLine() {
            List<PacketPlayOutScoreboardTeam> packets = new ArrayList<>();

            if (first) {
                packets.add(createTeam());
            } else if (prefixChanged || suffixChanged) {
                packets.add(updateTeam());
            }

            if (first || playerChanged) {
                /**if (oldPlayer != null)                                        // remove these two lines ?
                    packets.add(addOrRemovePlayer(4, oldPlayer));*/    //
                packets.add(changePlayer());
            }

            if (first)
                first = false;

            return packets;
        }

        public void reset() {
            prefixChanged = false;
            suffixChanged = false;
            playerChanged = false;
            oldPlayer = null;
        }

        public PacketPlayOutScoreboardTeam changePlayer() {
            return addOrRemovePlayer(3, currentPlayer);
        }

        public PacketPlayOutScoreboardTeam addOrRemovePlayer(int mode, String playerName) {
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            setField(packet, "a", name);
            setField(packet, "i", mode);

            try {
                Field f = packet.getClass().getDeclaredField("h");
                f.setAccessible(true);
                ((List<String>) f.get(packet)).add(playerName);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            return packet;
        }

        public String getCurrentPlayer() {
            return currentPlayer;
        }

        public String getValue() {
            return getPrefix() + getCurrentPlayer() + getSuffix();
        }

        public void setValue(String value) {
            if (value.length() <= 16) {
                setPrefix("");
                setSuffix("");
                setPlayer(value);
            } else if (value.length() <= 32) {
                setPrefix(value.substring(0, 16));
                setPlayer(value.substring(16));
                setSuffix("");
            } else if (value.length() <= 48) {
                setPrefix(value.substring(0, 16));
                setPlayer(value.substring(16, 32));
                setSuffix(value.substring(32));
            } else {
                throw new IllegalArgumentException("Too long value ! Max 48 characters, value was " + value.length() + " !");
            }
        }
    }

    private static void setField(Object edit, String fieldName, Object value) {
        try {
            Field field = edit.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(edit, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private class LastLine extends BukkitRunnable {
        private final String line[] = {"§7⋙ §bplay.euphalys.net", "§7⋙ §9p§blay.euphalys.net", "§7⋙ §bp§9l§bay.euphalys.net",
                "§7⋙ §bpl§9a§by.euphalys.net", "§7⋙ §bpla§9y§b.euphalys.net", "§7⋙ §bplay§9.§beuphalys.net",
                "§7⋙ §bplay.§9e§buphalys.net", "§7⋙ §bplay.e§9u§bphalys.net", "§7⋙ §bplay.eu§9p§bhalys.net",
                "§7⋙ §bplay.eup§9h§balys.net", "§7⋙ §bplay.euph§9a§blys.net", "§7⋙ §bplay.eupha§9l§bys.net",
                "§7⋙ §bplay.euphal§9y§bs.net", "§7⋙ §bplay.euphaly§9s§b.net", "§7⋙ §bplay.euphalys§9.§bnet",
                "§7⋙ §bplay.euphalys.§9n§bet", "§7⋙ §bplay.euphalys.n§9e§bt", "§7⋙ §bplay.euphalys.ne§9t", "§7⋙ §bplay.euphalys.net"};
        int i = 0;

        @Override
        public void run() {
            new BukkitRunnable() {
                @Override
                public void run() {
                    setLine(14, line[i++]);
                    if (i >= line.length) {
                        this.cancel();
                        i = 0;

                    }
                }
            }.runTaskTimer(EuphalysApi.getInstance(), 5L, 5L);
        }
    }
}
package net.euphalys.core.api;

import fr.dinnerwolph.otl.bukkit.BukkitOTL;
import io.netty.channel.ChannelHandlerContext;
import net.euphalys.api.database.IDatabaseManager;
import net.euphalys.api.friends.IFriendsManager;
import net.euphalys.api.names.IUUIDTranslator;
import net.euphalys.api.otl.GameState;
import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.player.IGroup;
import net.euphalys.api.player.IPlayerManager;
import net.euphalys.api.plugin.IEuphalysPlugin;
import net.euphalys.api.report.IReportManager;
import net.euphalys.api.sanctions.ISanctionsManager;
import net.euphalys.core.api.commands.*;
import net.euphalys.core.api.commands.chat.ClearChatCommands;
import net.euphalys.core.api.commands.chat.CloseChatCommands;
import net.euphalys.core.api.commands.chat.OpenChatCommands;
import net.euphalys.core.api.database.SQLDatabaseManager;
import net.euphalys.core.api.friends.FriendsManager;
import net.euphalys.core.api.listener.ListenerManager;
import net.euphalys.core.api.names.UUIDTranslator;
import net.euphalys.core.api.player.OfflineEuphalysPlayer;
import net.euphalys.core.api.player.PlayerManager;
import net.euphalys.core.api.report.ReportManager;
import net.euphalys.core.api.sanctions.SanctionsManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * @author Dinnerwolph
 */

public class EuphalysApi extends JavaPlugin implements IEuphalysPlugin {

    private static EuphalysApi instance;
    private IDatabaseManager databaseManager;
    private IUUIDTranslator uuidTranslator;
    private IFriendsManager friendsManager;
    private IPlayerManager playerManager;
    private ISanctionsManager sanctionsManager;
    private boolean hasRankInTabList = true;
    private IReportManager reportManager;
    private GameState gameState;

    public boolean hasChat = true;
    public List<UUID> freezeList = new ArrayList<>();
    public List<UUID> vanishList = new ArrayList();
    public Map<UUID, Integer> clickList = new HashMap();
    public Map<UUID, Integer> cps = new HashMap();

    private Map<Integer, IGroup> groupMap = new HashMap();
    private Map<UUID, IEuphalysPlayer> playerMap = new HashMap();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        instance = this;
        if (getConfig().getString("bdd.type").equalsIgnoreCase("sql")) {
            String host = getConfig().getString("bdd.sql.host", "localhost");
            int port = getConfig().getInt("bdd.sql.port", 3306);
            String database = getConfig().getString("bdd.sql.database", "azonarya");
            String username = getConfig().getString("bdd.sql.username", "username");
            String password = getConfig().getString("bdd.sql.password", "password");
            this.databaseManager = new SQLDatabaseManager("jdbc:mysql://" + host + ":" + port + "/" + database, username, password, 0, 200);
        }
        this.uuidTranslator = new UUIDTranslator(instance);
        this.friendsManager = new FriendsManager(instance);
        this.playerManager = new PlayerManager(instance);
        this.sanctionsManager = new SanctionsManager(this.getDatabaseManager().getDataSource(), instance);
        this.reportManager = new ReportManager(this.getDatabaseManager().getDataSource(), instance);
        new ListenerManager();
        registerCommands();
        playerManager.loadAllGroup();
        cpstask();

    }

    @Override
    public void onDisable() {
        for (Team team : this.getServer().getScoreboardManager().getMainScoreboard().getTeams()) {
            team.unregister();
        }
    }

    public IDatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public IUUIDTranslator getUUUIDTranslator() {
        return uuidTranslator;
    }

    @Override
    public IFriendsManager getFriendsManager() {
        return friendsManager;
    }

    public IPlayerManager getPlayerManager() {
        return playerManager;
    }

    public IReportManager getReportManager() {
        return reportManager;
    }

    public void addPlayer(IEuphalysPlayer player) {
        playerMap.put(player.getUUID(), player);
    }

    public void removePlayer(UUID uuid) {
        playerMap.remove(uuid);
    }

    public void addGroup(IGroup group) {
        groupMap.put(group.getGroupId(), group);
    }

    public void resetGroup() {
        this.groupMap = new HashMap();
    }

    @Override
    public IEuphalysPlayer getPlayer(UUID uuid) {
        IEuphalysPlayer player = playerMap.get(uuid);
        if (player == null)
            player = new OfflineEuphalysPlayer(uuid, instance);
        return player;
    }

    @Override
    public IEuphalysPlayer getPlayer(String name) {
        if (Bukkit.getPlayer(name) != null)
            return getPlayer(Bukkit.getPlayer(name).getUniqueId());
        return getPlayer(getPlayerManager().getUUID(name));
    }

    public IGroup getGroup(int id) {
        return groupMap.get(id);
    }

    public String getServerName() {
        return getConfig().getString("servername");
    }

    public ISanctionsManager getSanctionsManager() {
        return sanctionsManager;
    }

    public boolean hasRankInTabList() {
        return hasRankInTabList;
    }

    public void disableRankInTabList() {
        this.hasRankInTabList = false;
    }

    public static EuphalysApi getInstance() {
        return instance;
    }

    @Override
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        format("GAME_STATUS", gameState.getState());
    }

    public GameState getGameState() {
        return gameState;
    }

    private void registerCommands() {
        new BanCommands();
        new BanIpCommands();
        new MuteCommands();
        new WarnCommands();
        new BlackListCommands();
        new TempBanCommands();
        new TempMuteCommands();
        new InfoCommands();
        new GBanCommands();
        new GTempBanCommands();
        new GBanIpCommands();
        new GMuteCommands();
        new GTempMuteCommands();
        new CloseChatCommands();
        new OpenChatCommands();
        new ClearChatCommands();
        new SpectatorCommands();
        new Invcommands();
        new TpHereCommands();
        new KillCommands();
        new RTpCommands();
        new FreezeCommands();
        new VanishCommands();
        new SpeedCommands();
        new CPSCommands();
        new CloseReportCommands();
        new ListReportCommands();
        new InfoReportCommands();
        new GroupCommands();
    }

    private void cpstask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                for (UUID id : clickList.keySet()) {
                    cps.put(id, clickList.get(id));
                    clickList.remove(id);
                }
            }
        }, 0, 20);
    }

    private void format(String type, String data) {
        JSONObject object = new JSONObject();
        object.put("server", BukkitOTL.getInstance().getSProperty("name"));
        object.put("type", type);
        object.put("data", data);
        String s = object.toString();
        try {
            BukkitOTL.getInstance().context.writeAndFlush(s);
        } catch (Exception e) {
            e.printStackTrace();
            BukkitOTL.getInstance().reconnect();
        }
    }

    public String getSProperty(String property) {
        return System.getProperty(property);
    }

    public ChannelHandlerContext getContext() {
        return BukkitOTL.getInstance().context;
    }
}
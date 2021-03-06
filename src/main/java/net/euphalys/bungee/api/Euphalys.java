package net.euphalys.bungee.api;

import com.google.common.io.ByteStreams;
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
import net.euphalys.bungee.api.commands.*;
import net.euphalys.bungee.api.commands.sanctions.*;
import net.euphalys.bungee.api.listener.ListenerManager;
import net.euphalys.core.api.database.SQLDatabaseManager;
import net.euphalys.core.api.friends.FriendsManager;
import net.euphalys.core.api.names.UUIDTranslator;
import net.euphalys.core.api.player.OfflineEuphalysPlayer;
import net.euphalys.core.api.player.PlayerManager;
import net.euphalys.core.api.report.ReportManager;
import net.euphalys.core.api.sanctions.SanctionsManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dinnerwolph
 */

public class Euphalys extends Plugin implements IEuphalysPlugin {

    private static Euphalys instance;
    private Configuration configuration = null;
    private IDatabaseManager databaseManager;
    private IPlayerManager playerManager;
    private ISanctionsManager sanctionsManager;
    private IReportManager reportManager;
    private IFriendsManager friendsManager;
    private IUUIDTranslator uuidTranslator;
    private Map<Integer, IGroup> groupMap = new ConcurrentHashMap<>();
    private Map<UUID, IEuphalysPlayer> playerMap = new ConcurrentHashMap<>();
    private Map<ProxiedPlayer, ProxiedPlayer> partyMap = new HashMap<>();
    private boolean maintenance;

    @Override
    public void onEnable() {
        instance = this;
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = getResourceAsStream("config.yml");
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }

            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (configuration.getString("bdd.type").equalsIgnoreCase("sql")) {
            String host = configuration.getString("bdd.sql.host", "localhost");
            int port = configuration.getInt("bdd.sql.port", 3306);
            String database = configuration.getString("bdd.sql.database", "azonarya");
            String username = configuration.getString("bdd.sql.username", "username");
            String password = configuration.getString("bdd.sql.password", "password");
            this.databaseManager = new SQLDatabaseManager("jdbc:mysql://" + host + ":" + port + "/" + database, username, password, 2, 20);
        }
        this.maintenance = configuration.getBoolean("maintenance");
        this.playerManager = new PlayerManager(instance);
        this.playerManager.loadAllGroup();
        this.sanctionsManager = new SanctionsManager(databaseManager.getDataSource(), instance);
        this.reportManager = new ReportManager(databaseManager.getDataSource(), instance);
        this.friendsManager = new FriendsManager(instance);
        this.uuidTranslator = new UUIDTranslator(instance);
        new ListenerManager(configuration);
        getProxy().getPluginManager().registerCommand(this, new GRTpCommands());
        getProxy().getPluginManager().registerCommand(this, new SeeModsCommands());
        getProxy().getPluginManager().registerCommand(this, new HubCommands());
        getProxy().getPluginManager().registerCommand(this, new ReportCommands());
        getProxy().getPluginManager().registerCommand(this, new ServerCommands());
        getProxy().getPluginManager().registerCommand(this, new BanCommands());
        getProxy().getPluginManager().registerCommand(this, new BanIpCommands());
        getProxy().getPluginManager().registerCommand(this, new MuteCommands());
        getProxy().getPluginManager().registerCommand(this, new TempMuteCommands());
        getProxy().getPluginManager().registerCommand(this, new TempBanCommands());
        getProxy().getPluginManager().registerCommand(this, new RankCommands());
        getProxy().getPluginManager().registerCommand(this, new CISCommand());
        getProxy().getPluginManager().registerCommand(this, new AAISCommand());
        getProxy().getPluginManager().registerCommand(this, new SAISCommand());
        getProxy().getPluginManager().registerCommand(this, new GkickCommands());
        getProxy().getPluginManager().registerCommand(this, new MaintenanceCommand());
        getProxy().getPluginManager().registerCommand(this, new BlackListCommands());
        getProxy().getPluginManager().registerCommand(this, new TempBanIpCommands());
        getProxy().getPluginManager().registerCommand(this, new WarnCommands());
        getProxy().getPluginManager().registerCommand(this, new GUnbanCommands());
        getProxy().getPluginManager().registerCommand(this, new GUnmuteCommands());
        getProxy().getPluginManager().registerCommand(this, new GUnwarnCommands());
        getProxy().getPluginManager().registerCommand(this, new UnblacklistCommands());
    }

    public static Euphalys getInstance() {
        return instance;
    }

    public IEuphalysPlayer getPlayer(UUID uuid) {
        IEuphalysPlayer player = playerMap.get(uuid);
        if (player == null)
            player = new OfflineEuphalysPlayer(uuid, instance);
        return player;
    }

    @Override
    public IEuphalysPlayer getPlayer(String name) {
        if (getProxy().getPlayer(name) != null)
            return getPlayer(getProxy().getPlayer(name).getUniqueId());
        return getPlayer(getPlayerManager().getUUID(name));
    }

    public void addPlayer(IEuphalysPlayer player) {
        playerMap.put(player.getUUID(), player);
    }

    public void removePlayer(UUID uuid) {
        playerMap.remove(uuid);
    }

    public IPlayerManager getPlayerManager() {
        return playerManager;
    }

    public ISanctionsManager getSanctionsManager() {
        return sanctionsManager;
    }

    public IReportManager getReportManager() {
        return reportManager;
    }

    @Override
    public IDatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public void addGroup(IGroup group) {
        groupMap.put(group.getGroupId(), group);
    }

    @Override
    public IGroup getGroup(int id) {
        return groupMap.get(id);
    }

    @Override
    public String getServerName() {
        return "Bungeecord";
    }

    @Override
    public void setGameState(GameState gameState) {

    }

    @Override
    public IFriendsManager getFriendsManager() {
        return friendsManager;
    }

    @Override
    public IUUIDTranslator getUUUIDTranslator() {
        return uuidTranslator;
    }

    @Override
    public GameState getGameState() {
        return null;
    }

    @Override
    public void sendToServer(String server, UUID uuid) {
        getProxy().getPlayer(uuid).connect(getProxy().getServerInfo(server));
    }

    public boolean isMaintenance() {
        return maintenance;
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    public boolean partyContains(ProxiedPlayer player) {
        return partyMap.containsKey(player);
    }

    public boolean isPartyLeader(ProxiedPlayer player) {
        if (partyMap.containsKey(player))
            return partyMap.get(player).equals(player);
        else
            return true;
    }

    public Collection<ProxiedPlayer> getPartyMembers(ProxiedPlayer player) {
        Collection<ProxiedPlayer> players = new HashSet<>();
        if (isPartyLeader(player)) {
            for (ProxiedPlayer proxiedPlayer : partyMap.keySet())
                if (partyMap.get(proxiedPlayer).equals(player))
                    players.add(proxiedPlayer);
            return players;
        } else {
            return null;
        }
    }
}

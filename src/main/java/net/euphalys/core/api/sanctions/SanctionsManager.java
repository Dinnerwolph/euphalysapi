package net.euphalys.core.api.sanctions;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.plugin.IEuphalysPlugin;
import net.euphalys.api.sanctions.ISanctions;
import net.euphalys.api.sanctions.ISanctionsManager;
import net.euphalys.api.sanctions.SanctionsType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dinnerwolph
 */

public class SanctionsManager implements ISanctionsManager {

    private final DataSource dataSource;
    private final IEuphalysPlugin api;

    public SanctionsManager(DataSource dataSource, IEuphalysPlugin api) {
        this.dataSource = dataSource;
        this.api = api;
    }

    @Override
    public void addsanction(IEuphalysPlayer target, SanctionsType type, long duration, String message, IEuphalysPlayer player) {
        addsanction(player, type, duration, api.getServerName(), message, player);
    }

    @Override
    public void addsanction(IEuphalysPlayer target, SanctionsType type, long duration, String server, String message, IEuphalysPlayer player) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `sanctions`(`epyId`, `type`, `duration`, `server`, `message`, `ip`, `by`, `enable`) VALUES (?,?,?,?,?,?,?,?)");
            statement.setInt(1, target.getEuphalysId());
            statement.setInt(2, type.id);
            statement.setLong(3, duration);
            statement.setString(4, server);
            statement.setString(5, message);
            statement.setString(6, target.getLastAddress());
            statement.setInt(7, player.getEuphalysId());
            statement.setInt(8, 1);
            statement.executeUpdate();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addGlobalSanction(IEuphalysPlayer target, SanctionsType type, long duration, String message, IEuphalysPlayer player) {
        addsanction(target, type, duration, "global", message, player);
    }

    @Override
    public void removesanction(int sanctionsId) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE `sanctions` SET `enable`=? WHERE `id`=?");
            statement.setInt(1, 0);
            statement.setInt(2, sanctionsId);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ISanctions> getSanction(IEuphalysPlayer player) {
        List<ISanctions> returnmap = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `sanctions` WHERE `epyId`=? AND `enable`=?");
            statement.setInt(1, player.getEuphalysId());
            statement.setInt(2, 1);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Sanctions sanctions = new Sanctions(resultSet.getInt("id"), SanctionsType.getSanctionType(resultSet.getInt("type")), resultSet.getLong("duration"), resultSet.getString("server"), resultSet.getString("message"));
                returnmap.add(sanctions);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnmap;
    }
}

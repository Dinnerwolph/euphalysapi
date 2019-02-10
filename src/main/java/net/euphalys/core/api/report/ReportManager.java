package net.euphalys.core.api.report;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.api.plugin.IEuphalysPlugin;
import net.euphalys.api.report.IReport;
import net.euphalys.api.report.IReportManager;

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

public class ReportManager implements IReportManager {

    private final DataSource dataSource;
    private final IEuphalysPlugin api;

    public ReportManager(DataSource dataSource, IEuphalysPlugin api) {
        this.dataSource = dataSource;
        this.api = api;
    }

    @Override
    public void addReport(IEuphalysPlayer target, IEuphalysPlayer player, String message) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `report`(`azoid`, `by`, `reason`, `enable`) VALUES (?,?,?,?)");
            statement.setInt(1, target.getEuphalysId());
            statement.setInt(2, player.getEuphalysId());
            statement.setString(3, message);
            statement.setInt(4, 1);
            statement.executeUpdate();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeReport(int reportId) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE `report` SET `enable`=? WHERE `id`=?");
            statement.setInt(1, 0);
            statement.setInt(2, reportId);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<IReport> getReports(IEuphalysPlayer player) {
        List<IReport> returnmap = new ArrayList();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `report` WHERE `azoid`=? AND `enable`=?");
            statement.setInt(1, player.getEuphalysId());
            statement.setInt(2, 1);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Report report = new Report(resultSet.getInt("id"), resultSet.getInt("by"), resultSet.getInt("azoid"), resultSet.getString("reason"));
                returnmap.add(report);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnmap;
    }

    @Override
    public List<IReport> getAllsReports() {
        List<IReport> returnmap = new ArrayList();
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `report` WHERE `enable`=?");
            statement.setInt(1, 1);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Report report = new Report(resultSet.getInt("id"), resultSet.getInt("by"), resultSet.getInt("azoid"), resultSet.getString("reason"));
                returnmap.add(report);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnmap;
    }

    @Override
    public IReport getReport(int reportId) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `report` WHERE `id`=? AND `enable`=?");
            statement.setInt(1, reportId);
            statement.setInt(2, 1);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            IReport report = new Report(resultSet.getInt("id"), resultSet.getInt("by"), resultSet.getInt("azoid"), resultSet.getString("reason"));
            connection.close();
            return report;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

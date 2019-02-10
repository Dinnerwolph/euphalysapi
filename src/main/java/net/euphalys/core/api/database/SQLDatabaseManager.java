package net.euphalys.core.api.database;

import net.euphalys.api.database.IDatabaseManager;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

/**
 * @author Dinnerwolph
 */

public class SQLDatabaseManager implements IDatabaseManager {

    public static volatile SQLDatabaseManager instance = null;
    public DataSource dataSource = null;
    private String url;
    private String name;
    private String password;
    private int minPoolSize;
    private int maxPoolSize;

    public SQLDatabaseManager(String url, String name, String password, int minPoolSize, int maxPoolSize) {
        this.url = url;
        this.name = name;
        this.password = password;
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;
        this.setupDataSource();
    }

    public final static SQLDatabaseManager setInstance(String url, String name, String password, int minPoolSize, int maxPoolSize) {
        if (SQLDatabaseManager.instance == null) {
            synchronized (SQLDatabaseManager.class) {
                if (SQLDatabaseManager.instance == null) {
                    SQLDatabaseManager.instance = new SQLDatabaseManager(url, name, password, minPoolSize, maxPoolSize);
                }
            }
        }
        return SQLDatabaseManager.instance;
    }

    public void setupDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(this.url);
        dataSource.setUsername(this.name);
        dataSource.setPassword(this.password);
        dataSource.setInitialSize(this.minPoolSize);
        dataSource.setMaxTotal(this.maxPoolSize);
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void getSourcesStats(DataSource dataSource)
    {
        BasicDataSource basicDataSource = (BasicDataSource) dataSource;
        System.out.println("Number of active: " + basicDataSource.getNumActive());
        System.out.println("Number of idle: " + basicDataSource.getNumIdle());
        System.out.println("================================================================================");
    }

    public void shutdownDataSource(DataSource dataSource) throws Exception
    {
        BasicDataSource basicDataSource = (BasicDataSource) dataSource;
        basicDataSource.close();
    }
}

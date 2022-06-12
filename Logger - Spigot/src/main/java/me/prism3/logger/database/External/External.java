package me.prism3.logger.database.external;

import com.zaxxer.hikari.HikariDataSource;
import me.prism3.logger.Main;
import me.prism3.logger.utils.Data;

public class External {

    private final Main main = Main.getInstance();

    private String jdbc;
    private static final String DATATYPE = Data.dbType.toLowerCase();
    private static final String HOST = Data.dbHost;
    private static final int PORT = Data.dbPort;
    private static final String USERNAME = Data.dbUserName;
    private static final String PASSWORD = Data.dbPassword;
    private static final String DATABASE = Data.dbName;
    private HikariDataSource hikari;

    public boolean isConnected() { return this.hikari != null; }

    public void connect() {

        String jdbcDriver;
        final String mySQL = "MySQL";
        final String mySQLDriver = "com.mysql.jdbc.jdbc2.optional.MysqlDataSource";
        final String mariaDB = "mariadb";
        final String mariaDBDriver = "org.mariadb.jdbc.MariaDbDataSource";

        switch (DATATYPE) {

            case "mysql":
                this.jdbc = mySQL;
                jdbcDriver = mySQLDriver;
                break;

            case "mariadb":
                this.jdbc = mariaDB;
                jdbcDriver = mariaDBDriver;
                this.main.getLogger().warning(this.jdbc + " Type is still in Development. Report any issues on Discord or Github!");
                break;

            default:
                this.main.getLogger().severe("Unknown Database Type. Available ones are: MySQL and MariaDB.");
                return;

        }

        if (!isConnected()) {

            hikari = new HikariDataSource();
            hikari.setDataSourceClassName(jdbcDriver);
            hikari.addDataSourceProperty("serverName", HOST);
            hikari.addDataSourceProperty("port", PORT);
            hikari.addDataSourceProperty("databaseName", DATABASE);
            hikari.addDataSourceProperty("user", USERNAME);
            hikari.addDataSourceProperty("password", PASSWORD);
            this.main.getLogger().info(this.jdbc + " Connection has been established!");

        }
    }

    public void disconnect() {

        if (isConnected()) {

            this.hikari.close();
            this.main.getLogger().info(this.jdbc + " Connection has been closed!");

        }
    }
    public HikariDataSource getHikari() { return this.hikari; }
}
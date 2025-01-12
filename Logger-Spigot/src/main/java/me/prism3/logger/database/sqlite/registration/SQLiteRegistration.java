package me.prism3.logger.database.sqlite.registration;

import me.prism3.logger.Logger;
import me.prism3.logger.utils.Log;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteRegistration {

    private final Logger main = Logger.getInstance();

    private Connection connection;

    private final File databaseFile = new File(this.main.getDataFolder(), "." + File.separator + "Logs" + File.separator + "Registration" + File.separator + "Registration_Data" + File.separator + "Registration.db");

    public boolean isConnected() {
        return (this.connection != null);
    }

    public void connect() {

        if (!isConnected()) {

            try {

                Class.forName("org.sqlite.JDBC");
                this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.databaseFile.getAbsolutePath());

            } catch (ClassNotFoundException | SQLException e) {

                Log.severe("Couldn't load Registration Database, if the issue persists contact the Authors!");
                e.printStackTrace();

            }
        }
    }

    public void disconnect() {

        if (isConnected()) {

            try {

                this.connection.close();

            } catch (SQLException e) {

                Log.severe("Registration Database couldn't be closed safely, if the issue persists contact the Authors!");
                e.printStackTrace();

            }
        }
    }
    public Connection getConnection() { return this.connection; }
}

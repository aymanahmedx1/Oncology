/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author ayman
 */
public class DBConnection {

    private static String serverAdrress = "jdbc:mysql://localhost:3306/oncology";
    private static String url;
    private static String username;
    private static String password;

    public static Connection createConnection() throws SQLException {
        try {
            File file = new File("jdbc.properties");
            if (file.exists()) {
                FileInputStream fs = new FileInputStream(file);
                Properties prop = new Properties();
                prop.load(fs);
                url = prop.getProperty("url");
                username = prop.getProperty("username");
                password = prop.getProperty("password");
                return DriverManager.getConnection(url, username, password);
            } else {
                System.out.println("not found");
            }
        } catch (SQLException ex) {
            sm(ex.getMessage());
            throw new SQLException();
        } catch (FileNotFoundException ex) {
            sm(ex.getMessage());
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            sm(ex.getMessage());
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static void sm(String m) {

        Alert alert = new Alert(Alert.AlertType.ERROR,"Error is \n"+ m, ButtonType.OK);
        alert.show();
    }
}

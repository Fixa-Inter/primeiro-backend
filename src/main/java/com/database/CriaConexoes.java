package com.database;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CriaConexoes {

    private static final Dotenv dotenv = Dotenv.configure().load();

    public Connection getConnection() throws SQLException, ClassNotFoundException {

        String url = dotenv.get("DB_URL");

        Class.forName("org.postgresql.Driver");

        if (url == null){
            throw new IllegalStateException("Variavel de ambiente 'DB_URL (.env)' nao encontrada");
        }

        return DriverManager.getConnection(url, dotenv.get("DB_USER"), dotenv.get("DB_PASSWORD"));
    }

    public void closeConnection(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed()){
            conn.close();
        }
    }

}

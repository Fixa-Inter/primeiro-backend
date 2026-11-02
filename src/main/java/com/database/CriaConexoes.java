package com.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.SQLException;

// Pool de conexões com o banco (HikariCP)
public class CriaConexoes {

    private static final HikariDataSource dataSource;

    static {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        String url = dotenv.get("DB_URL");
        if (url == null) {
            throw new IllegalStateException("Variavel de ambiente 'DB_URL (.env)' nao encontrada");
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(dotenv.get("DB_USER"));
        config.setPassword(dotenv.get("DB_PASSWORD"));
        config.setDriverClassName("org.postgresql.Driver");

        config.setMaximumPoolSize(5);  // Máximo de conexões simultâneas
        config.setMinimumIdle(2);  // Conexões mantidas livres no pool
        config.setConnectionTimeout(30000);  // Espera máxima por uma conexão livre (30 s)
        config.setMaxLifetime(1800000);  // Renova cada conexão a cada 30 minutos
        config.setAutoCommit(false);  // O commit e o rollback são feitos no DAO

        dataSource = new HikariDataSource(config);
    }

    // Empresta uma conexão do pool
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();  // Empresta uma conexão do pool
    }

    // Devolve a conexão ao pool
    public void closeConnection(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();  // Não fecha de fato, devolve ao pool
        }
    }
}
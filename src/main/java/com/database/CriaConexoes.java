package com.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.SQLException;

public class CriaConexoes {

    private static final HikariDataSource dataSource;

    static {
        Dotenv dotenv = Dotenv.configure().load();

        String url = dotenv.get("DB_URL");
        if (url == null) {
            throw new IllegalStateException("Variavel de ambiente 'DB_URL (.env)' nao encontrada");
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(dotenv.get("DB_USER"));
        config.setPassword(dotenv.get("DB_PASSWORD"));
        config.setDriverClassName("org.postgresql.Driver");

        config.setMaximumPoolSize(15);        // quantidade maxima de conexoes simultaneas
        config.setMinimumIdle(2);             // quantidade de conexoes que sistema tenta manter disponivel sempre
        config.setConnectionTimeout(30000);   // quanto tempo uma requisicao espera por uma conexao livre, se passar de 30s lança uma excecao
        config.setMaxLifetime(1800000);       // troca uma conexao livre por uma nova a cada 30 minutos para evitar erros
        config.setAutoCommit(false);          // definimos no DAO se vai dar commit ou rollback

        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();   // empresta do pool
    }

    public void closeConnection(Connection conn) throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();   // NÃO fecha de verdade — devolve ao pool
        }
    }
}
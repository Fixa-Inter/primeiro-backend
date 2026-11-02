package com.DAO;

import com.database.CriaConexoes;

import java.sql.Connection;
import java.sql.SQLException;

// Base dos DAOs: abre e fecha a conexão com o banco
public abstract class DAO implements AutoCloseable{

    protected static final CriaConexoes criaConn = new CriaConexoes();
    protected Connection conn;

    // Abre a conexão com o banco
    protected DAO() throws SQLException {

        conn = criaConn.getConnection();
    }

    // Devolve a conexão ao pool
    @Override
    public void close() throws SQLException{
        criaConn.closeConnection(conn);
    }
}

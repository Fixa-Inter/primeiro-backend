package com.DAO;

import com.database.CriaConexoes;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DAO implements AutoCloseable{

    protected static final CriaConexoes criaConn = new CriaConexoes();
    protected Connection conn;

    protected DAO() throws SQLException {

        conn = criaConn.getConnection();
    }

    @Override
    public void close() throws SQLException{
        criaConn.closeConnection(conn);
    }
}

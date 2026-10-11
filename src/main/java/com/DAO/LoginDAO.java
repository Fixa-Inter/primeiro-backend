package com.DAO;

import com.DTO.LoginDTO;
import com.DTO.SuperAdminDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO extends DAO{

    public LoginDAO() throws SQLException {
        super();
    }

    public SuperAdminDTO login(LoginDTO credenciais) throws SQLException {
        String sql = "SELECT id, nome, senha_hash FROM super_administrador WHERE email = ?";

        String senhaHash, nome, cargo;
        SuperAdminDTO superAdm;
        int id;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, credenciais.getEmail());

            try (ResultSet rs = pstmt.executeQuery()) {

                // Se não encontrar retorna null (o login falhou)
                if (!rs.next()) {
                    return null;
                }

                // Caso contrário, busca o hash da senha no banco
                senhaHash = rs.getString("senha_hash");

                // Se a senha não for compatível retorna null (login falhou)
                if (!credenciais.getSenha().equals(senhaHash)) {
                    return null;
                }

                id = rs.getInt("id");
                nome = rs.getString("nome");

                superAdm = new SuperAdminDTO(id, nome, credenciais.getEmail());
            }
        }

        return superAdm;
    }

}

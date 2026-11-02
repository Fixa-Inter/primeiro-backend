package com.DAO;

import com.DTO.LoginDTO;
import com.DTO.SuperAdminDTO;
import com.service.HashSenha;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Valida as credenciais do super administrador
public class LoginDAO extends DAO{

    // Abre a conexão com o banco
    public LoginDAO() throws SQLException {
        super();
    }

    // Devolve o super administrador se e-mail e senha conferem, ou null
    public SuperAdminDTO login(LoginDTO credenciais) throws SQLException {
        String sql = "SELECT id, nome, senha_hash FROM super_administrador WHERE email = ?";

        String senhaHash;
        String nome;
        String cargo;
        SuperAdminDTO superAdm;
        int id;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, credenciais.getEmail());

            try (ResultSet rs = pstmt.executeQuery()) {

                // Sem usuário com esse e-mail, o login falha
                if (!rs.next()) {
                    return null;
                }

                // Busca o hash da senha salvo
                senhaHash = rs.getString("senha_hash");

                // Senha incorreta, o login falha
                if (!HashSenha.conferir(credenciais.getSenha(), senhaHash)) {
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

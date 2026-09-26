package com.DAO;

import com.model.Filtro;
import com.model.Usuario;
import com.model.enums.OperacaoFiltro;
import com.model.enums.TipoAcesso;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UsuarioDAO extends DAO{

    // map dos campos que sao filtraveis
    public static final Map<String, String> camposFiltraveis = Map.of(
            "ID","ID",
            "NOME", "Nome",
            "ESTA_ATIVO", "Esta Ativo",
            "EMAIL", "Email",
            "DATA_CRIACAO", "Data Criacao",
            "CARGO", "Cargo",
            "FK_ENDERECO_ID", "Fk endereco ID",
            "TIPO_ACESSO", "Tipo De Acesso",
            "DATA_NASCIMENTO", "Data de nascimento",
            "PRIMEIRO_ACESSO", "Data acesso"
    );

    public static final Map<String, List<OperacaoFiltro>> operacoesPorCampo = Map.of(
            "ID", List.of(
                    OperacaoFiltro.IGUAL
            ),
            "NOME", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),
            "ESTA_ATIVO", List.of(
                    OperacaoFiltro.IGUAL
            ),
            "EMAIL", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),
            "DATA_CRIACAO", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.MAIOR_QUE,
                    OperacaoFiltro.MAIOR_OU_IGUAL,
                    OperacaoFiltro.MENOR_QUE,
                    OperacaoFiltro.MENOR_OU_IGUAL
            ),
            "CARGO", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.CONTEM
            ),
            "FK_ENDERECO_ID", List.of(
                    OperacaoFiltro.IGUAL
            ),
            "TIPO_ACESSO", List.of(
                    OperacaoFiltro.IGUAL
            ),
            "DATA_NASCIMENTO", List.of(
                    OperacaoFiltro.IGUAL,
                    OperacaoFiltro.MAIOR_QUE,
                    OperacaoFiltro.MAIOR_OU_IGUAL,
                    OperacaoFiltro.MENOR_QUE,
                    OperacaoFiltro.MENOR_OU_IGUAL
            ),
            "PRIMEIRO_ACESSO", List.of(
                    OperacaoFiltro.IGUAL
            )
    );

    // convertendo String recebida do Servlet
    public Object converterValor(String campo, String valor){
        try {
            return switch (campo) {
                case "id", "fk_endereco_id" -> Integer.parseInt(valor);
                case "nome", "senha_hash", "email", "cargo" -> valor;
                case "esta_ativo", "primeiro_acesso" -> Boolean.parseBoolean(valor);
                case "data_criacao" -> LocalDateTime.parse(valor);
                case "tipoAcesso" -> TipoAcesso.converterEnum(valor);
                case "data_nascimento" -> LocalDate.parse(valor);
                default -> throw new IllegalArgumentException();
            };
        } catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    // construtor
    public UsuarioDAO() throws SQLException, ClassNotFoundException {
        super();
    }

    // insert

    public void cadastrar(Usuario usuario) throws SQLException{

        String nome = usuario.getNome();
        String senhaHash = usuario.getSenhaHash();
        String email = usuario.getEmail();
        String cargo = usuario.getCargo();
        int fkEndereco = usuario.getFkEndereco();
        LocalDate dataAniversario = usuario.getDataAniversario();
        Integer tipoAcesso = usuario.getTipoDeAcesso().getCodigo();

        // tira id, data_cricao, esta_ativo e primeiro_acesso pois o default do BD já define eles automáticamente

        String sql = """
                     INSERT INTO usuario (nome, senha_hash, email, cargo, tipo_acesso, fk_endereco_id, data_nascimento)
                     VALUES (?, ?, ?, ?, ?, ?, ?)
                     """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, nome);
            pstmt.setString(2, senhaHash);
            pstmt.setString(3, email);
            pstmt.setString(4, cargo);
            pstmt.setInt(5, tipoAcesso);
            pstmt.setInt(6, fkEndereco);
            pstmt.setDate(7, Date.valueOf(dataAniversario));

            pstmt.execute();

            conn.commit();
        }  catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    // select
    public List<Usuario> listar(List<Filtro> filtros, String campoSequencia, String direcaoSequencia) throws SQLException {

        List<Usuario> resultado = new ArrayList<>();

        String sql = "SELECT id, nome, esta_ativo, senha_hash, email, data_criacao, cargo, tipo_acesso, fk_endereco_id, data_nascimento, primeiro_acesso FROM usuario";

        if (filtros != null && !filtros.isEmpty()) {

            sql += " WHERE ";

            for (int i = 0; i < filtros.size(); i++) {

                Filtro filtro = filtros.get(i);

                // Verifica se o campo existe
                if (!camposFiltraveis.containsKey(filtro.getCampoFiltravel())) {
                    throw new IllegalArgumentException("Campo inválido: " + filtro.getCampoFiltravel());
                }

                // Verifica se a operação é permitida para esse campo
                if (!operacoesPorCampo
                        .get(filtro.getCampoFiltravel())
                        .contains(filtro.getOperacaoFiltro())) {

                    throw new IllegalArgumentException(
                            "Operação inválida para o campo: " + filtro.getCampoFiltravel()
                    );
                }

                // Coloca AND a partir do segundo filtro
                if (i > 0) {
                    sql += " AND ";
                }

                // Adiciona a condição
                if (filtro.getOperacaoFiltro() == OperacaoFiltro.CONTEM) {

                    sql += "REPLACE(unaccent(" + filtro.getCampoFiltravel() + "), ' ', '') "
                            + filtro.getOperacaoFiltro().getOperadorSQL()
                            + " REPLACE(unaccent(?), ' ', '')";

                } else {

                    sql += filtro.getCampoFiltravel() + " "
                            + filtro.getOperacaoFiltro().getOperadorSQL() + " ?";
                }
            }
        }

        if (campoSequencia != null && camposFiltraveis.containsKey(campoSequencia)){
            sql += " ORDER BY %s %s".formatted(campoSequencia, direcaoSequencia);
        } else {
            sql += " ORDER BY ID ASC";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Verifica se tem filtro, se sim define a variável do comando SQL
            if (filtros != null && !filtros.isEmpty()) {
                for (int i = 0; i < filtros.size(); i++) {
                    Filtro filtro = filtros.get(i);

                    if (filtro.getOperacaoFiltro() == OperacaoFiltro.CONTEM) {
                        pstmt.setObject(i + 1, "%" + filtro.getValor() + "%");
                    } else {
                        pstmt.setObject(i + 1, filtro.getValor());
                    }
                }
            }

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){

                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String senhaHash = rs.getString("senha_hash");
                    boolean estaAtivo = rs.getBoolean("esta_ativo");
                    String email = rs.getString("email");
                    Timestamp dataCriacaoSQL = rs.getTimestamp("data_criacao");
                    LocalDateTime dataCriacao = (dataCriacaoSQL == null ? null : dataCriacaoSQL.toLocalDateTime());
                    String cargo = rs.getString("cargo");
                    int tipoDeAcesso = rs.getInt("tipo_acesso");
                    int fkEnderecoID = rs.getInt("fk_endereco_id");
                    Date dataAniversarioAcessoBD =  rs.getDate("data_nascimento");
                    LocalDate dataAniversario = (dataAniversarioAcessoBD == null ? null : dataAniversarioAcessoBD.toLocalDate());
                    Boolean primeiroAcesso = rs.getBoolean("primeiro_acesso");


                    resultado.add(new Usuario(id, nome, senhaHash, estaAtivo, email, dataCriacao, cargo, TipoAcesso.converterEnum(tipoDeAcesso), fkEnderecoID, dataAniversario, primeiroAcesso));
                }
            }

        }
        conn.commit();
        return resultado;
    }

    // select id
    public Usuario pesquisarPorId(int idUsuario) throws SQLException{


        String sql = "SELECT id, nome, esta_ativo, senha_hash, email, data_criacao, cargo, tipo_acesso, fk_endereco_id, data_nascimento, primeiro_acesso FROM usuario WHERE id = ?";

        Usuario u;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, idUsuario);

            try (ResultSet rs = pstmt.executeQuery()){

                if (!rs.next()){
                    throw new SQLException("Erro ao encontrar aluno");
                }

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String senhaHash = rs.getString("senha_hash");
                boolean estaAtivo = rs.getBoolean("esta_ativo");
                String email = rs.getString("email");
                Timestamp dataCriacaoSQL = rs.getTimestamp("data_criacao");
                LocalDateTime dataCriacao = (dataCriacaoSQL == null ? null : dataCriacaoSQL.toLocalDateTime());
                String cargo = rs.getString("cargo");
                int tipoDeAcesso = rs.getInt("tipo_acesso");
                int fkEnderecoID = rs.getInt("fk_endereco_id");
                Date dataAniversarioAcessoBD =  rs.getDate("data_nascimento");
                LocalDate dataAniversario = (dataAniversarioAcessoBD == null ? null : dataAniversarioAcessoBD.toLocalDate());
                Boolean primeiroAcesso = rs.getBoolean("primeiro_acesso");


                u = new Usuario(id, nome, senhaHash, estaAtivo, email, dataCriacao, cargo, TipoAcesso.converterEnum(tipoDeAcesso), fkEnderecoID, dataAniversario, primeiroAcesso);

            }

        }
        conn.commit();
        return u;
    }


    // select nome
    public Usuario pesquisarPorNome(String nomeUsuario) throws SQLException{

        String sql = "SELECT id, nome, esta_ativo, senha_hash, email, data_criacao, cargo, tipo_acesso, fk_endereco_id, data_nascimento, primeiro_acesso FROM usuario WHERE nome = ?";

        Usuario u;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, nomeUsuario);

            try (ResultSet rs = pstmt.executeQuery()){

                if (!rs.next()){
                    throw new SQLException("Erro ao encontrar aluno");
                }

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String senhaHash = rs.getString("senha_hash");
                boolean estaAtivo = rs.getBoolean("esta_ativo");
                String email = rs.getString("email");
                Timestamp dataCriacaoSQL = rs.getTimestamp("data_criacao");
                LocalDateTime dataCriacao = (dataCriacaoSQL == null ? null : dataCriacaoSQL.toLocalDateTime());
                String cargo = rs.getString("cargo");
                int tipoDeAcesso = rs.getInt("tipo_acesso");
                int fkEnderecoID = rs.getInt("fk_endereco_id");
                Date dataAniversarioAcessoBD =  rs.getDate("data_nascimento");
                LocalDate dataAniversario = (dataAniversarioAcessoBD == null ? null : dataAniversarioAcessoBD.toLocalDate());
                Boolean primeiroAcesso = rs.getBoolean("primeiro_acesso");


                u = new Usuario(id, nome, senhaHash, estaAtivo, email, dataCriacao, cargo, TipoAcesso.converterEnum(tipoDeAcesso), fkEnderecoID, dataAniversario, primeiroAcesso);
            }

        }
        conn.commit();
        return u;
    }

    public void atualizar(Usuario original, Usuario alterado) throws SQLException{

        Integer id = alterado.getId();
        String nome = alterado.getNome();
        String senhaHash = alterado.getSenhaHash();
        Boolean estaAtivo = alterado.getEstaAtivo();
        String email = alterado.getEmail();
        String cargo = alterado.getCargo();
        Integer tipoDeAcesso = alterado.getTipoDeAcesso().getCodigo();
        Integer fkEndereco = alterado.getFkEndereco();
        LocalDate dataAniversario = alterado.getDataAniversario();
        Boolean primeiroAcesso = alterado.getPrimeiroAcesso();

        StringBuilder sql = new StringBuilder("UPDATE usuario SET ");
        List<Object> valores = new ArrayList<>();

        if (!Objects.equals(nome, original.getNome())){
            sql.append("nome = ?, ");
            valores.add(nome);
        }

        if (!Objects.equals(senhaHash, original.getSenhaHash())){
            sql.append("senha_hash = ?, ");
            valores.add(senhaHash);
        }

        if (!Objects.equals(estaAtivo, original.getEstaAtivo())){
            sql.append("esta_ativo = ?, ");
            valores.add(estaAtivo);
        }

        if (!Objects.equals(email, original.getEmail())){
            sql.append("email = ?, ");
            valores.add(email);
        }

        // tira data_criacao pois é campo que não deve ser atualizado

        if (!Objects.equals(cargo, original.getCargo())){
            sql.append("cargo = ?, ");
            valores.add(cargo);
        }

        if (!Objects.equals(tipoDeAcesso, original.getTipoDeAcesso().getCodigo())){
            sql.append("tipo_acesso = ?, ");
            valores.add(tipoDeAcesso);
        }

        if (!Objects.equals(fkEndereco, original.getFkEndereco())){
            sql.append("fk_endereco_id = ?, ");
            valores.add(fkEndereco);
        }

        if (!Objects.equals(dataAniversario, original.getDataAniversario())){
            sql.append("data_nascimento = ?, ");
            valores.add(dataAniversario);
        }

        if (!Objects.equals(primeiroAcesso, original.getPrimeiroAcesso())){
            sql.append("primeiro_acesso = ?, ");
            valores.add(primeiroAcesso);
        }

        if (valores.isEmpty()){
            return;
        }

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE id = ?");
        valores.add(id);

        try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())){
            for (int i = 0; i < valores.size(); i++) {
                pstmt.setObject(i + 1, valores.get(i));
            }

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e){
            conn.rollback();
            throw e;
        }
    }

    // delete
    public void remover(int id) throws SQLException{

        String sql = "DELETE from usuario where id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);

            pstmt.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }

    }
}
<%@ page import="com.model.Usuario" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Usuario usuario = (Usuario) request.getAttribute("usuario");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Editar Usuário</title>
</head>

<body>

<h1>Editar Usuário</h1>

<form action="${pageContext.request.contextPath}/usuarios"
      method="post">

    <input type="hidden"
           name="action"
           value="update">

    <input type="hidden"
           name="id"
           value="<%= usuario.getId() %>">


    <label for="nome">
        Nome:
    </label>

    <input type="text"
           id="nome"
           name="nome"
           value="<%= usuario.getNome() %>"
           required>

    <br><br>


    <label for="senhaHash">
        Senha:
    </label>

    <input type="password"
           id="senhaHash"
           name="senhaHash"
           value="<%= usuario.getSenhaHash() %>"
           required>

    <br><br>


    <label for="estaAtivo">
        Está ativo:
    </label>

    <select id="estaAtivo"
            name="estaAtivo"
            required>

        <option value="true"
                <%= Boolean.TRUE.equals(usuario.getEstaAtivo()) ? "selected" : "" %>>
            Sim
        </option>

        <option value="false"
                <%= Boolean.FALSE.equals(usuario.getEstaAtivo()) ? "selected" : "" %>>
            Não
        </option>

    </select>

    <br><br>


    <label for="email">
        Email:
    </label>

    <input type="email"
           id="email"
           name="email"
           value="<%= usuario.getEmail() %>"
           required>

    <br><br>


    <p>
        Data de criação:
        <%= usuario.getDataCriacao() %>
    </p>

    <br><br>


    <label for="cargo">
        Cargo:
    </label>

    <input type="text"
           id="cargo"
           name="cargo"
           value="<%= usuario.getCargo() %>"
           required>

    <br><br>


    <label for="tipoDeAcesso">
        Tipo de acesso:
    </label>

    <select id="tipoDeAcesso"
            name="tipoDeAcesso"
            required>

        <option value="ADMINISTRADOR"
                <%= usuario.getTipoDeAcesso() != null
                        && usuario.getTipoDeAcesso().name().equals("ADMINISTRADOR")
                        ? "selected" : "" %>>
            Administrador
        </option>

        <option value="GESTOR"
                <%= usuario.getTipoDeAcesso() != null
                        && usuario.getTipoDeAcesso().name().equals("GESTOR")
                        ? "selected" : "" %>>
            Gestor
        </option>

        <option value="TECNICO"
                <%= usuario.getTipoDeAcesso() != null
                        && usuario.getTipoDeAcesso().name().equals("TECNICO")
                        ? "selected" : "" %>>
            Técnico
        </option>

        <option value="SOLICITANTE"
                <%= usuario.getTipoDeAcesso() != null
                        && usuario.getTipoDeAcesso().name().equals("SOLICITANTE")
                        ? "selected" : "" %>>
            Solicitante
        </option>

    </select>

    <br><br>


    <label for="fkEndereco">
        ID do endereço:
    </label>

    <input type="number"
           id="fkEndereco"
           name="fkEndereco"
           value="<%= usuario.getFkEndereco() %>"
           required>

    <br><br>


    <label for="dataAniversario">
        Data de nascimento:
    </label>

    <input type="date"
           id="dataAniversario"
           name="dataAniversario"
           value="<%= usuario.getDataAniversario() %>"
           required>

    <br><br>


    <label for="primeiroAcesso">
        Primeiro acesso:
    </label>

    <select id="primeiroAcesso"
            name="primeiroAcesso"
            required>

        <option value="true"
                <%= Boolean.TRUE.equals(usuario.getPrimeiroAcesso()) ? "selected" : "" %>>
            Sim
        </option>

        <option value="false"
                <%= Boolean.FALSE.equals(usuario.getPrimeiroAcesso()) ? "selected" : "" %>>
            Não
        </option>

    </select>

    <br><br>


    <button type="submit">
        Salvar alterações
    </button>

</form>

<br>

<a href="${pageContext.request.contextPath}/usuarios">
    Voltar
</a>

</body>
</html>
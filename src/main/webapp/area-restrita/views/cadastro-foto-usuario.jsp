<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List,com.model.Usuario" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Cadastrar Foto</title>
</head>

<body>

<h1>Cadastrar Foto</h1>

<% String erro = (String) request.getAttribute("erro"); %>
<form
        action="${pageContext.request.contextPath}/fotoUsuario"
        method="post"
>
            <% if (erro != null && !erro.isBlank()) { %>
            <p>
                <%= erro %>
            </p>
            <% } %>


    <input
            type="hidden"
            name="action"
            value="create"
    >

    <label for="url">
        URL da foto:
    </label>

    <input
            type="text"
            id="url"
            name="url"
            maxlength="255"
            required
    >

    <br><br>

    <label for="fkUsuario">Usuário:</label>
    <select id="fkUsuario" name="fkUsuario" required>
        <option value="">Selecione um usuário</option>
        <% for (Usuario usuario : (List<Usuario>) request.getAttribute("usuariosDisponiveis")) { %>
        <option value="<%= usuario.getId() %>"><%= usuario.getNome() %> — <%= usuario.getEmail() %></option>
        <% } %>
    </select>

    <br><br>

    <br><br>

    <button type="submit">
        Cadastrar
    </button>

    <a href="${pageContext.request.contextPath}/fotoUsuario">
        Cancelar
    </a>

</form>

</body>

</html>

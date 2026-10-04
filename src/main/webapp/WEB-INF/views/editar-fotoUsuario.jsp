<%@ page import="com.model.FotoUsuario" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    FotoUsuario foto =
            (FotoUsuario) request.getAttribute("fotoUsuario");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Editar Foto</title>
</head>

<body>

<h1>Editar Foto</h1>

<form
        action="${pageContext.request.contextPath}/fotoUsuario"
        method="post"
>

    <input
            type="hidden"
            name="action"
            value="update"
    >

    <input
            type="hidden"
            name="id"
            value="<%= foto.getId() %>"
    >

    <p>
        <strong>ID:</strong>
        <%= foto.getId() %>
    </p>

    <p>
        <strong>Data de registro:</strong>
        <%= foto.getDataRegistro() %>
    </p>

    <label for="url">
        URL da foto:
    </label>

    <input
            type="text"
            id="url"
            name="url"
            value="<%= foto.getUrl() %>"
            maxlength="255"
            required
    >

    <br><br>

    <label for="fkUsuario">
        ID do usuário:
    </label>

    <input
            type="number"
            id="fkUsuario"
            name="fkUsuario"
            value="<%= foto.getFkUsuario() %>"
            min="1"
            required
    >

    <br><br>

    <button type="submit">
        Salvar alterações
    </button>

    <a href="${pageContext.request.contextPath}/fotoUsuario">
        Cancelar
    </a>

</form>

</body>

</html>
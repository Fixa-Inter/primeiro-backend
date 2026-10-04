<%@ page import="com.model.SuperAdministrador" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    SuperAdministrador superAdmin =
            (SuperAdministrador) request.getAttribute("superAdmin");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Editar Super Administrador</title>
</head>

<body>

<h1>Editar Super Administrador</h1>

<form
        action="${pageContext.request.contextPath}/superAdmin"
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
            value="<%= superAdmin.getId() %>"
    >

    <label for="nome">
        Nome:
    </label>

    <input
            type="text"
            id="nome"
            name="nome"
            value="<%= superAdmin.getNome() %>"
            maxlength="100"
            required
    >

    <br><br>

    <label for="email">
        Email:
    </label>

    <input
            type="email"
            id="email"
            name="email"
            value="<%= superAdmin.getEmail() %>"
            maxlength="100"
            required
    >

    <br><br>

    <label for="senha">
        Nova senha:
    </label>

    <input
            type="password"
            id="senha"
            name="senha"
            minlength="8"
    >

    <br><br>

    <button type="submit">
        Salvar alterações
    </button>

    <a href="${pageContext.request.contextPath}/superAdmin">
        Cancelar
    </a>

</form>

</body>

</html>
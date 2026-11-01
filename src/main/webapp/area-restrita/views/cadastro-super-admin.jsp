<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Cadastrar Super Administrador</title>
</head>

<body>

<h1>Cadastrar Super Administrador</h1>

<% String erro = (String) request.getAttribute("erro"); %>
<form
        action="${pageContext.request.contextPath}/superAdmin"
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

    <label for="nome">
        Nome:
    </label>

    <input
            type="text"
            id="nome"
            name="nome"
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
            maxlength="100"
            required
    >

    <br><br>

    <label for="senha">
        Senha:
    </label>

    <input
            type="password"
            id="senha"
            name="senha"
            minlength="8"
            required
    >

    <br><br>

    <button type="submit">
        Cadastrar
    </button>

    <a href="${pageContext.request.contextPath}/superAdmin">
        Cancelar
    </a>

</form>

</body>

</html>